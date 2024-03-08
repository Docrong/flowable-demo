package com.demo.flowable.service;

import com.demo.flowable.controller.vo.model.ModelCreateReqVO;
import com.demo.flowable.controller.vo.model.ModelPageItemRespVO;
import com.demo.flowable.controller.vo.model.ModelRespVO;
import com.demo.flowable.controller.vo.model.ModelUpdateReqVO;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/1 9:02
 */
public interface IFlwModelService {

    /**
     * 获取模型列表
     * 
     * @return 流程模型 列表信息
     */
    List<ModelPageItemRespVO> getModelList();

    /**
     * 创建流程模型
     *
     * @param modelVO 创建信息
     * @param bpmnXml BPMN XML
     * @return 创建的流程模型的编号
     */
    String createModel(@Valid ModelCreateReqVO modelVO, String bpmnXml);

    /**
     * 获得流程模块
     *
     * @param id 编号
     * @return 流程模型
     */
    ModelRespVO getModel(String id);

    /**
     * 部署
     * @param id
     * @return
     */
    Boolean deployModel(String id);

    /**
     * 删除模型
     *
     * @param id 编号
     */
    Boolean deleteModel(String id);

    /**
     * 修改流程模型
     *
     * @param updateReqVO 更新信息
     */
    Boolean updateModel(ModelUpdateReqVO updateReqVO);
}
