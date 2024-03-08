package com.demo.flowable.controller.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author : gr
 * @date : 2024/3/2 15:58
 */
@ApiModel(value = "管理后台 - 流程模型的创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModelRespVO extends ModelBaseVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "BPMN XML", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

}

