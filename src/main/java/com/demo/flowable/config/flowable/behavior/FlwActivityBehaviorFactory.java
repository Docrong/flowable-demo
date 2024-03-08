package com.demo.flowable.config.flowable.behavior;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 自定义的 ActivityBehaviorFactory 实现类，目的如下：
 * 1. 自定义 {@link #createUserTaskActivityBehavior(UserTask)}：实现自定义的流程任务的 assignee 负责人的分配
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlwActivityBehaviorFactory extends DefaultActivityBehaviorFactory {


    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        FlwUserTaskActivityBehavior flwUserTaskActivityBehavior = new FlwUserTaskActivityBehavior(userTask);
        return flwUserTaskActivityBehavior;
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity,
                                                                             AbstractBpmnActivityBehavior innerActivityBehavior) {
        FlwParallelMultiInstanceBehavior flwParallelMultiInstanceBehavior = new FlwParallelMultiInstanceBehavior(activity, innerActivityBehavior);
        return flwParallelMultiInstanceBehavior;
    }
    

}
