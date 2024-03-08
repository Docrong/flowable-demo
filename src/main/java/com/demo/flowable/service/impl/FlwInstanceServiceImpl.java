package com.demo.flowable.service.impl;

import cn.hutool.core.lang.Assert;
import com.demo.flowable.controller.vo.instance.FlwInstanceActiveReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceSuspendReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceCreateReqVO;
import com.demo.flowable.service.IFlwDefinitionService;
import com.demo.flowable.service.IFlwInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : gr
 * @date : 2024/2/29 15:10
 */
@Service
public class FlwInstanceServiceImpl implements IFlwInstanceService {

    @Resource
    private HistoryService historyService;

    @Resource
    private IFlwDefinitionService processDefinitionService;

    @Resource
    private RuntimeService runtimeService;

    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processInstanceId(id);
        HistoricProcessInstance processInstance = historicProcessInstanceQuery.singleResult();
        return processInstance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(@Valid FlwInstanceCreateReqVO createReqVO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        // 发起流程
        return createProcessInstance0(definition, createReqVO.getVariables(), null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean suspendInstance(FlwInstanceSuspendReqVO reqVO) {
        for (String id : reqVO.getIds()) {
            runtimeService.suspendProcessInstanceById(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean activeInstance(FlwInstanceActiveReqVO reqVO) {
        for (String id : reqVO.getIds()) {
            runtimeService.activateProcessInstanceById(id);
        }
        return true;
    }

    private String createProcessInstance0(ProcessDefinition definition,
                                          Map<String, Object> variables, String businessKey, String businessName) {
        // 校验流程定义
        Assert.notNull(definition, "找不到流程定义");
        Assert.isFalse(definition.isSuspended(), "该流程已挂起");
        if (StringUtils.isBlank(businessName)) {
            businessName = definition.getName().trim();
        }

        // 创建流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(definition.getId())
                .businessKey(businessKey)
                .name(businessName)
                .variables(variables)
                .start();
        // 设置流程名字
        //runtimeService.setProcessInstanceName(instance.getId(), definition.getName());

        return instance.getId();
    }

    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }
}
