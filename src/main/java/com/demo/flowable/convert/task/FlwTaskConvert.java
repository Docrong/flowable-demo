package com.demo.flowable.convert.task;

import com.demo.flowable.controller.vo.task.FlwTaskSimpleRespVO;
import com.demo.flowable.controller.vo.task.FlwTaskRespVO;
import com.demo.flowable.controller.vo.task.FlwTaskTodoPageItemRespVO;
import com.demo.flowable.utils.CollectionUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/7 14:56
 */
@Mapper
public interface FlwTaskConvert {
    FlwTaskConvert INSTANCE = Mappers.getMapper(FlwTaskConvert.class);


    default List<FlwTaskRespVO> convertList3(List<HistoricTaskInstance> tasks, HistoricProcessInstance processInstance){

        return CollectionUtils.convertList(tasks, task -> {
            FlwTaskRespVO respVO = convert3(task);
            if (processInstance != null) {
                FlwTaskTodoPageItemRespVO.ProcessInstance processInstance1 = new FlwTaskTodoPageItemRespVO.ProcessInstance();
                processInstance1.setId(processInstance.getId());
                processInstance1.setName(processInstance.getName());
                processInstance1.setStartUserId(processInstance.getStartUserId());
                processInstance1.setProcessDefinitionId(processInstance.getProcessDefinitionId());
                respVO.setProcessInstance(processInstance1);
            }
            return respVO;
        });
        
    }

    FlwTaskRespVO convert3(HistoricTaskInstance task);


    default List<FlwTaskSimpleRespVO> convertList(List<? extends FlowElement> elementList) {
        return CollectionUtils.convertList(elementList, element -> new FlwTaskSimpleRespVO()
                .setName(element.getName())
                .setDefinitionKey(element.getId()));
    }
    
    
    
}
