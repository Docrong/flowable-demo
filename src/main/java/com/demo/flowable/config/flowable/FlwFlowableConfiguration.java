package com.demo.flowable.config.flowable;

import cn.hutool.core.collection.ListUtil;
import com.demo.flowable.config.flowable.behavior.FlwActivityBehaviorFactory;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : gr
 * @date : 2024/3/5 16:47
 */
@Configuration(proxyBeanMethods = false)
public class FlwFlowableConfiguration {

    /**
     * BPM 模块的 ProcessEngineConfigurationConfigurer 实现类：
     * <p>
     * 1. 设置各种监听器
     * 2. 设置自定义的 ActivityBehaviorFactory 实现
     */
    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> bpmProcessEngineConfigurationConfigurer(
            ObjectProvider<FlowableEventListener> listeners,
            FlwActivityBehaviorFactory flwActivityBehaviorFactory) {
        return configuration -> {
            // 注册监听器，例如说 BpmActivityEventListener
            configuration.setEventListeners(ListUtil.toList(listeners.iterator()));
            // 设置 ActivityBehaviorFactory 实现类，用于流程任务的审核人的自定义
            configuration.setActivityBehaviorFactory(flwActivityBehaviorFactory);
        };
    }

    @Bean
    public FlwActivityBehaviorFactory bpmActivityBehaviorFactory() {
        FlwActivityBehaviorFactory flwActivityBehaviorFactory = new FlwActivityBehaviorFactory();
        return flwActivityBehaviorFactory;
    }

}