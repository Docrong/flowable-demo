package com.demo.flowable.controller.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author : gr
 * @date : 2024/3/7 12:52
 */
@ApiModel(value = "管理后台 - 不通过流程任务的 Request VO")
@Data
public class FlwTaskRejectReqVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "审批意见", required = true, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;

    @ApiModelProperty(name = "操作人", required = true, example = "1")
    @NotEmpty(message = "操作人不能为空")
    private String operatorId;
}