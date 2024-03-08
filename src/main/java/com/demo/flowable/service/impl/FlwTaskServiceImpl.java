package com.demo.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.demo.flowable.controller.vo.task.*;
import com.demo.flowable.convert.task.FlwTaskConvert;
import com.demo.flowable.enums.BpmCommentTypeEnum;
import com.demo.flowable.service.IFlwInstanceService;
import com.demo.flowable.service.IFlwTaskService;
import com.demo.flowable.utils.BpmnModelUtils;
import com.demo.flowable.utils.StringUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.demo.flowable.utils.CollectionUtils.convertList;

/**
 * @author : gr
 * @date : 2024/2/29 14:45
 */
@Service
public class FlwTaskServiceImpl implements IFlwTaskService {

    public static final String assigneeId = "FLOW_ASSIGNEE";

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService; 

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private IFlwInstanceService processInstanceService;

    @Override
    public List<TaskEntityImpl> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveTask(FlwTaskApproveReqVO reqVO) {

        //校验assignee是否存在
        Assert.notBlank(StringUtil.nullObject2Str(reqVO.getVariables().get(assigneeId)), "派往对象不能为空");

        // 校验任务存在
        Task task = checkTask(reqVO.getOperatorId(), reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        Assert.notNull(instance, "流程实例不存在");

        List<String> hisKeys = getHisKeys(task);
        //判断Variables 是否被修改 没修改跳回原节点 有修改正常进行流程
        Boolean flag = true;
        if (reqVO.getVariables() != null) {
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            for (Map.Entry<String, Object> stringObjectEntry : reqVO.getVariables().entrySet()) {
                Object o = processVariables.get(stringObjectEntry.getKey());
                if (o == null || !o.equals(stringObjectEntry.getValue())) {
                    flag = false;
                    break;
                }
            }
            taskService.removeVariable(task.getId(), HIS_ACTIVITY_KEY);
            taskService.setVariables(task.getId(), reqVO.getVariables());
            //提交设置全局variables
            reqVO.getVariables().put(TASK_ID_, task.getId());
            runtimeService.setVariables(task.getExecutionId(), reqVO.getVariables());
        }

        //有历史任务标识 跳回原任务
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(hisKeys) && flag) {
            String targetActivityId = hisKeys.get(hisKeys.size() - 1);
            hisKeys.remove(hisKeys.size() - 1);
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), targetActivityId)
                    .processVariable(HIS_ACTIVITY_KEY, hisKeys)
                    .changeState();
//                List<String> collect = list.stream().map(TaskInfo::getId).collect(Collectors.toList());
//                taskService.deleteTasks(collect, true);
//                taskExtMapper.deleteByTaskIds(collect);
        } else {
            // 完成任务，审批通过
            taskService.complete(task.getId(), instance.getProcessVariables());
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rejectTask(FlwTaskRejectReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(reqVO.getOperatorId(), reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        Assert.notNull(instance, "流程实例不存在");

        // 更新流程实例为不通过
        runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), reqVO.getReason());
        runtimeService.setVariable(task.getExecutionId(), TASK_ID_, task.getId());

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean returnTask(FlwTaskReturnReqVO reqVO) {
        // 1.1 当前任务 task
        Task task = checkTask(reqVO.getId(), reqVO.getId());
        
        // 1.2 校验源头和目标节点的关系，并返回目标元素
        FlowElement targetElement = validateTargetTaskCanReturn(task.getTaskDefinitionKey(), reqVO.getTargetDefinitionKey(), task.getProcessDefinitionId());


        // 1. 获得所有需要回撤的任务 taskDefinitionKey，用于稍后的 moveActivityIdsToSingleActivityId 回撤
        // 1.1 获取所有正常进行的任务节点 Key
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = convertList(taskList, Task::getTaskDefinitionKey);
        // 1.2 通过 targetElement 的出口连线，计算在 runTaskKeyList 有哪些 key 需要被撤回
        // 为什么不直接使用 runTaskKeyList 呢？因为可能存在多个审批分支，例如说：A -> B -> C 和 D -> F，而只要 C 撤回到 A，需要排除掉 F
        List<UserTask> returnUserTaskList = BpmnModelUtils.iteratorFindChildUserTasks(targetElement, runTaskKeyList, null, null);
        List<String> returnTaskKeyList = convertList(returnUserTaskList, UserTask::getId);

        // 2. 给当前要被回退的 task 数组，设置回退意见
        taskList.forEach(t -> {
            // 需要排除掉，不需要设置回退意见的任务
            if (!returnTaskKeyList.contains(t.getTaskDefinitionKey())) {
                return;
            }
            taskService.addComment(t.getId(), t.getProcessInstanceId(),
                    BpmCommentTypeEnum.BACK.getType().toString(), reqVO.getReason());
        });

        // 3. 执行驳回
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdsToSingleActivityId(returnTaskKeyList, // 当前要跳转的节点列表( 1 或多)
                        reqVO.getTargetDefinitionKey()) // targetKey 跳转到的节点(1)
                .changeState();
        
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FlwTaskSimpleRespVO> getReturnTaskList(String taskId) {
        // 校验任务存在
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, String.format("任务%s不存在", taskId));
        
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement source = bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        
        Assert.notNull(source, "model找不到flow元素");

        // 2.1 查询该任务的前置任务节点的 key 集合
        List<UserTask> previousUserList = BpmnModelUtils.getPreviousUserTaskList(source, null, null);
        if (CollUtil.isEmpty(previousUserList)) {
            return Collections.emptyList();
        }
        // 2.2 过滤：只有串行可到达的节点，才可以回退。类似非串行、子流程无法退回
        previousUserList.removeIf(userTask -> !BpmnModelUtils.isSequentialReachable(source, userTask, null));
        return FlwTaskConvert.INSTANCE.convertList(previousUserList);
    }

    /**
     * 回退流程节点时，校验目标任务节点是否可回退
     *
     * @param sourceKey           当前任务节点 Key
     * @param targetKey           目标任务节点 key
     * @param processDefinitionId 当前流程定义 ID
     * @return 目标任务节点元素
     */
    private FlowElement validateTargetTaskCanReturn(String sourceKey, String targetKey, String processDefinitionId) {
        // 1.1 获取流程模型信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 1.3 获取当前任务节点元素
        FlowElement source = BpmnModelUtils.getFlowElementById(bpmnModel, sourceKey);
        // 1.3 获取跳转的节点元素
        FlowElement target = BpmnModelUtils.getFlowElementById(bpmnModel, targetKey);

        Assert.notNull(target, "无法退回到目标节点");

        // 2.2 只有串行可到达的节点，才可以回退。类似非串行、子流程无法退回
        if (!BpmnModelUtils.isSequentialReachable(source, target, null)) {
            throw new RuntimeException("回退任务失败，目标节点是在并行网关上或非同一路线上，不可跳转");
        }
        return target;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createSignTask(FlwTaskAddSignReqVO reqVO) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handover(FlwTaskHandoverReqVO reqVO) {
        return null;
    }

    @Override
    public List<FlwTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Map
        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<String> userIds = tasks.stream().map(TaskInfo::getAssignee).filter(Objects::nonNull).collect(Collectors.toSet());
        userIds.add(processInstance.getStartUserId());
        // 拼接数据
        return FlwTaskConvert.INSTANCE.convertList3(tasks, processInstance);
    }


    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(String userId, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "审批任务失败，原因：该任务不处于未审批");
        Assert.equals(userId, task.getAssignee(), "审批任务失败，原因：该任务的审批人不一致");
        return task;
    }

    public static final String HIS_ACTIVITY_KEY = "HIS_ACTIVITY_KEY";

    public static final String TASK_ID_ = "DONE_TASK_ID_";

    private List<String> getHisKeys(Task task) {
        Object obj = taskService.getVariable(task.getId(), HIS_ACTIVITY_KEY);
        List<String> keys = new ArrayList<>();
        if (obj != null) {
            if (obj instanceof ArrayList<?>) {
                for (Object o : (List<?>) obj) {
                    keys.add((String) o);
                }
            }
        }
        return keys;
    }
}
