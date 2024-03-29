package com.demo.flowable.controller.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author : gr
 * @date : 2024/3/5 14:55
 */
@ApiModel(value = "管理后台 - 流程实例的创建 Request VO")
@Data
public class FlwInstanceCreateReqVO {

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "1024")
    @NotEmpty(message = "流程定义编号不能为空")
    private String processDefinitionId;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;

}