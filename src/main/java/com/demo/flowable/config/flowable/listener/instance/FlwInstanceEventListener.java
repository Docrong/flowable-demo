package com.demo.flowable.config.flowable.listener.instance;

import com.google.common.collect.ImmutableSet;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 监听 {@link ProcessInstance} 的开始与完成，创建与更新对应的 {@link } 记录
 *
 * @author jason
 */
@Component
public class FlwInstanceEventListener extends AbstractFlowableEngineEventListener {
    
    public static final Set<FlowableEngineEventType> PROCESS_INSTANCE_EVENTS = ImmutableSet.<FlowableEngineEventType>builder()
            .add(FlowableEngineEventType.PROCESS_CREATED)
            .add(FlowableEngineEventType.PROCESS_CANCELLED)
            .add(FlowableEngineEventType.PROCESS_COMPLETED)
            .build();

    public FlwInstanceEventListener(){
        super(PROCESS_INSTANCE_EVENTS);
    }

    @Override
    protected void processCreated(FlowableEngineEntityEvent event) {
    }

    @Override
    protected void processCancelled(FlowableCancelledEvent event) {
    }

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
    }
}
