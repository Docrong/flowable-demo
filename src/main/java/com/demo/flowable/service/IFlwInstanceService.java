package com.demo.flowable.service;

import com.demo.flowable.controller.vo.instance.FlwInstanceActiveReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceSuspendReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceCreateReqVO;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author : gr
 * @date : 2024/2/29 15:10
 */
public interface IFlwInstanceService {

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    HistoricProcessInstance getHistoricProcessInstance(String id);

    /**
     * 创建流程实例
     *
     * @param createReqVO 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(@Valid FlwInstanceCreateReqVO createReqVO);

    /**
     * 挂起流程实例
     * 
     * @param reqVO 挂起信息
     * @return 成功标志
     */
    Boolean suspendInstance(FlwInstanceSuspendReqVO reqVO);

    /**
     * 激活流程实例
     * @param reqVO 激活信息
     * @return 成功标志
     */
    Boolean activeInstance(FlwInstanceActiveReqVO reqVO);

    /**
     * 获得流程实例
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String id);

    /**
     * 获得流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表
     */
    List<ProcessInstance> getProcessInstances(Set<String> ids);
}
