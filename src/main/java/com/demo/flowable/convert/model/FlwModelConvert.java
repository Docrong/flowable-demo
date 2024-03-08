package com.demo.flowable.convert.model;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.demo.flowable.controller.vo.definition.FlwDefinitionCreateReqDTO;
import com.demo.flowable.controller.vo.model.*;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author : gr
 * @date : 2024/3/2 17:02
 */
@Mapper
public interface FlwModelConvert {

    FlwModelConvert INSTANCE = Mappers.getMapper(FlwModelConvert.class);

    ModelRespVO convert(Model model);

    default FlwDefinitionCreateReqDTO convert2(Model model) {
        FlwDefinitionCreateReqDTO createReqDTO = new FlwDefinitionCreateReqDTO();
        createReqDTO.setModelId(model.getId());
        createReqDTO.setName(model.getName());
        createReqDTO.setKey(model.getKey());
        createReqDTO.setCategory(model.getCategory());
        ModelMetaInfoRespDTO metaInfo = JSONObject.parseObject(model.getMetaInfo(), ModelMetaInfoRespDTO.class);
        // metaInfo
        copyTo(metaInfo, createReqDTO);

        return createReqDTO;
    }

    void copyTo(ModelMetaInfoRespDTO from, @MappingTarget FlwDefinitionCreateReqDTO to);

    default void copy(Model model, ModelCreateReqVO bean) {
        model.setName(bean.getName());
        model.setKey(bean.getKey());
        model.setMetaInfo(buildMetaInfoStr(null, bean.getDescription(), null, null,
                null, null));
    }

    default void copy(Model model, ModelUpdateReqVO bean) {
        model.setName(bean.getName());
        model.setCategory(bean.getCategory());
        model.setMetaInfo(buildMetaInfoStr(JSONObject.parseObject(model.getMetaInfo(), ModelMetaInfoRespDTO.class),
                bean.getDescription(), bean.getFormType(), bean.getFormId(),
                bean.getFormCustomCreatePath(), bean.getFormCustomViewPath()));
    }

    default String buildMetaInfoStr(ModelMetaInfoRespDTO metaInfo, String description, Integer formType,
                                    String formId, String formCustomCreatePath, String formCustomViewPath) {
        if (metaInfo == null) {
            metaInfo = new ModelMetaInfoRespDTO();
        }
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StrUtil.isNotEmpty(description)) {
            metaInfo.setDescription(description);
        }
        if (Objects.nonNull(formType)) {
            metaInfo.setFormType(formType);
            metaInfo.setFormId(formId);
            metaInfo.setFormCustomCreatePath(formCustomCreatePath);
            metaInfo.setFormCustomViewPath(formCustomViewPath);
        }
        return JSONObject.toJSONString(metaInfo);
    }

    default List<ModelPageItemRespVO> convertList(List<Model> list,
                                                  Map<String, Deployment> deploymentMap,
                                                  Map<String, ProcessDefinition> processDefinitionMap) {
//       return CollUtil.fi(list, model -> {
//            ModelMetaInfoRespDTO metaInfo = JSONObject.parseObject(model.getMetaInfo(), ModelMetaInfoRespDTO.class);
//            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
//            ProcessDefinition processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
//            return convert(model, deployment, processDefinition);
//        });

       return list.stream().filter(Objects::nonNull).map(model -> {
            ModelMetaInfoRespDTO metaInfo = JSONObject.parseObject(model.getMetaInfo(), ModelMetaInfoRespDTO.class);
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            return convert(model, deployment, processDefinition);
        }).collect(Collectors.toList());
    }

    default ModelPageItemRespVO convert(Model model, Deployment deployment, ProcessDefinition processDefinition) {
        ModelPageItemRespVO modelRespVO = new ModelPageItemRespVO();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(DateUtil.toLocalDateTime(model.getCreateTime()));
        // 通用 copy
        copyTo(model, modelRespVO);
        
        // ProcessDefinition
        modelRespVO.setProcessDefinition(this.convert(processDefinition));
        if (modelRespVO.getProcessDefinition() != null) {
            modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.isSuspended() ?
                    SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
            modelRespVO.getProcessDefinition().setDeploymentTime(DateUtil.toLocalDateTime(deployment.getDeploymentTime()));
        }
        return modelRespVO;
    }

    default void copyTo(Model model, ModelBaseVO to) {
        to.setName(model.getName());
        to.setKey(model.getKey());
        to.setCategory(model.getCategory());
        // metaInfo
        ModelMetaInfoRespDTO metaInfo = JSONObject.parseObject(model.getMetaInfo(), ModelMetaInfoRespDTO.class);
        copyTo(metaInfo, to);
    }

    void copyTo(ModelMetaInfoRespDTO from, @MappingTarget ModelBaseVO to);

    ModelPageItemRespVO.ProcessDefinition convert(ProcessDefinition bean);
    
}
