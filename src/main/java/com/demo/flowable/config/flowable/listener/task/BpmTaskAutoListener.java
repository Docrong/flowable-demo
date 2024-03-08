package com.demo.flowable.config.flowable.listener.task;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * @author : gr
 * @date : 2024/3/5 15:22
 */
@Component
@Slf4j
public class BpmTaskAutoListener implements TaskListener {

    public static final String AUTO = "auto";
    public static final String AUTO_REASON_DEFAULT = "系统自动通过";
    
    private Expression reason;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        Object value = reason.getValue(delegateTask);
        if (value != null && StringUtils.isNotBlank(value.toString())) {
            delegateTask.setVariableLocal(AUTO, value.toString());
        } else {
            delegateTask.setVariableLocal(AUTO, AUTO_REASON_DEFAULT);
        }
        
        log.info(JSONObject.toJSONString(delegateTask));
//        throw new RuntimeException("运行异常");
    }
}
