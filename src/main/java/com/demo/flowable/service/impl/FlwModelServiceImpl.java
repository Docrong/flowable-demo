package com.demo.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.demo.flowable.controller.vo.definition.FlwDefinitionCreateReqDTO;
import com.demo.flowable.controller.vo.model.ModelCreateReqVO;
import com.demo.flowable.controller.vo.model.ModelPageItemRespVO;
import com.demo.flowable.controller.vo.model.ModelRespVO;
import com.demo.flowable.controller.vo.model.ModelUpdateReqVO;
import com.demo.flowable.convert.model.FlwModelConvert;
import com.demo.flowable.service.IFlwModelService;
import com.demo.flowable.service.IFlwDefinitionService;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author : gr
 * @date : 2024/3/1 9:02
 */
@Service
public class FlwModelServiceImpl implements IFlwModelService {

    @Resource
    private RepositoryService repositoryService;
    
    @Resource
    private IFlwDefinitionService processDefinitionService;
    
    @Override
    public List<ModelPageItemRespVO> getModelList(){
        ModelQuery modelQuery = repositoryService.createModelQuery();
        List<Model> models = modelQuery.orderByCreateTime().desc().list();
        System.out.println(JSONObject.toJSONString(models));


        Set<String> deploymentIds = new HashSet<>();
        models.forEach(model -> CollUtil.addIfAbsent(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = CollUtil.toMap(processDefinitions, new HashMap<>(processDefinitions.size()), ProcessDefinition::getDeploymentId);
        List<ModelPageItemRespVO> modelPageItemRespVOS = FlwModelConvert.INSTANCE.convertList(models, deploymentMap, processDefinitionMap);
        return modelPageItemRespVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createModel(ModelCreateReqVO createReqVO, String bpmnXml) {
        // 校验流程标识已经存在
        Model keyModel = getModelByKey(createReqVO.getKey());
        Assert.isNull(keyModel,"模型已存在");

        // 创建流程定义
        Model model = repositoryService.newModel();
        FlwModelConvert.INSTANCE.copy(model, createReqVO);
        // 保存流程定义
        repositoryService.saveModel(model);
        // 保存 BPMN XML
        saveModelBpmnXml(model, bpmnXml);
        return model.getId();
    }

    @Override
    public ModelRespVO getModel(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            return null;
        }
        ModelRespVO modelRespVO = FlwModelConvert.INSTANCE.convert(model);
        // 拼接 bpmn XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        modelRespVO.setBpmnXml(StrUtil.utf8Str(bpmnBytes));
        return modelRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deployModel(String id) {
        Model model = repositoryService.getModel(id);
        Assert.notNull(model, "模型不存在");

        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        Assert.notNull(bpmnBytes,"找不到资源");

        ProcessDefinition oldProcessDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
//        Assert.isNull(oldProcessDefinition,"已部署,请不要重复部署");

        FlwDefinitionCreateReqDTO definitionCreateReqDTO = FlwModelConvert.INSTANCE.convert2(model).setBpmnBytes(bpmnBytes);

        // 2.1 创建流程定义
        String definitionId = processDefinitionService.createProcessDefinition(definitionCreateReqDTO);
        
        // 2.2 将老的流程定义进行挂起。也就是说，只有最新部署的流程定义，才可以发起任务。
        updateProcessDefinitionSuspended(model.getDeploymentId());

        // 2.3 更新 model 的 deploymentId，进行关联
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(definitionId);
        model.setDeploymentId(definition.getDeploymentId());
        repositoryService.saveModel(model);
        
        return true;
    }

    /**
     * 挂起 deploymentId 对应的流程定义。 这里一个deploymentId 只关联一个流程定义
     *
     * @param deploymentId 流程发布Id.
     */
    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        processDefinitionService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteModel(String id) {
        // 校验流程模型存在
        Model model = repositoryService.getModel(id);
        Assert.notNull(model, "模型不存在");
        // 执行删除
        repositoryService.deleteModel(id);
        // 禁用流程定义
        updateProcessDefinitionSuspended(model.getDeploymentId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateModel(ModelUpdateReqVO updateReqVO) {
        // 校验流程模型存在
        Model model = repositoryService.getModel(updateReqVO.getId());
        Assert.notNull("模型不存在");

        // 修改流程定义
        FlwModelConvert.INSTANCE.copy(model, updateReqVO);
        // 更新模型
        repositoryService.saveModel(model);

        if(StrUtil.isNotBlank(updateReqVO.getBpmnXml())){
            BpmnXMLConverter converter = new BpmnXMLConverter();
            BpmnModel bpmnModel = converter.convertToBpmnModel(new BytesStreamSource(StrUtil.utf8Bytes(updateReqVO.getBpmnXml())), true, true);
            bpmnModel.getProcesses()
                    .forEach(process -> process.getFlowElements()
                            .forEach(flowElement ->{
                                if (flowElement instanceof UserTask){
                                    UserTask userTask = (UserTask) flowElement;
                                    if(!model.getCategory().equals(userTask.getCategory())){
                                        userTask.setCategory(model.getCategory());
                                    }
                                }
                            } ));
            // 更新 BPMN XML
            repositoryService.addModelEditorSource(model.getId(), converter.convertToXML(bpmnModel));
        }
        return true;
    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery().modelKey(key).singleResult();
    }

    private void saveModelBpmnXml(Model model, String bpmnXml) {
        if (StrUtil.isEmpty(bpmnXml)) {
            return;
        }
        repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(bpmnXml));
    }
}
