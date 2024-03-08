package com.demo.flowable.config.flowable.listener.event;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableActivityCancelledEvent;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 监听 {@link org.flowable.task.api.Task} 的开始与完成，创建与更新对应的 {@link BpmTaskExtDO} 记录
 *
 * @author jason
 */
@Component
@Order
@Slf4j
public class FlwTaskEventListener extends AbstractFlowableEngineEventListener {


    public static final Set<FlowableEngineEventType> TASK_EVENTS = ImmutableSet.<FlowableEngineEventType>builder()
            .add(FlowableEngineEventType.TASK_CREATED)
            .add(FlowableEngineEventType.TASK_ASSIGNED)
            .add(FlowableEngineEventType.TASK_COMPLETED)
            .add(FlowableEngineEventType.ACTIVITY_CANCELLED)
            .build();

    public FlwTaskEventListener() {
        super(TASK_EVENTS);
    }

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        TaskEntity entity = (TaskEntity) event.getEntity();
        //修改模型时已经修改了xml 分类 不需要修改
//        if (StrUtil.isBlank(entity.getCategory())) {
//            //前提是Deployment已经设置过Category
//            ProcessDefinitionEntity processDefinitionEntity = CommandContextUtil.getProcessDefinitionEntityManager().findById(entity.getProcessDefinitionId());
//            DeploymentEntity deploymentEntity = CommandContextUtil.getDeploymentEntityManager().findById(processDefinitionEntity.getDeploymentId());
//            //直接修改act_ru_task实体的属性
//            //因为事件是同步事件，所以会包裹在整体事务中提交
//            entity.setCategory(deploymentEntity.getCategory());
//        }
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
    }

    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
    }

    @Override
    protected void activityCancelled(FlowableActivityCancelledEvent event) {

    }

}
