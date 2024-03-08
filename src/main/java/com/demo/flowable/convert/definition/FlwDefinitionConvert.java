package com.demo.flowable.convert.definition;

import com.demo.flowable.controller.vo.definition.FlwDefinitionRespVO;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/2 15:09
 */
@Mapper
public interface FlwDefinitionConvert {

    FlwDefinitionConvert INSTANCE = Mappers.getMapper(FlwDefinitionConvert.class);

    FlwDefinitionRespVO convert(ProcessDefinition processDefinition);
    
    List<FlwDefinitionRespVO> convert(List<ProcessDefinition> list);
}
