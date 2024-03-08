package com.demo.flowable.service;

import com.demo.flowable.controller.vo.task.*;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/2/29 14:45
 */
public interface IFlwTaskService {

    List<TaskEntityImpl> getTasksByProcessInstanceIds(List<String> processInstanceIds);

    /**
     * 通过任务
     *
     * @param reqVO 通过请求
     * @return 成功标志
     */
    Boolean approveTask(@Valid FlwTaskApproveReqVO reqVO);

    /**
     * 不通过任务
     *
     * @param reqVO 不通过请求
     * @return 成功标志
     */
    Boolean rejectTask(@Valid FlwTaskRejectReqVO reqVO);

    /**
     * 回退节点
     *
     * @param reqVO 回退参数 
     * @return
     */
    boolean returnTask(FlwTaskReturnReqVO reqVO);

    /**
     * 获取可跳转的节点
     *
     * @param taskId 任务id
     * @return
     */
    List<FlwTaskSimpleRespVO> getReturnTaskList(String taskId);

    /**
     * 加签
     *
     * @param reqVO 加签理由,派往对象
     */
    Boolean createSignTask(FlwTaskAddSignReqVO reqVO);

    /**
     * 移交
     *
     * @param reqVO 移交对象
     * @return
     */
    Boolean handover(FlwTaskHandoverReqVO reqVO);

    /**
     * 获得指令流程实例的流程任务列表，包括所有状态的
     *
     * @param processInstanceId 流程实例的编号
     *
     * @return 流程任务列表
     */
    List<FlwTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId);
}
