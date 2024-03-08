package com.demo.flowable.controller.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author : gr
 * @date : 2024/3/7 12:51
 */
@ApiModel(value = "管理后台 - 通过流程任务的 Request VO")
@Data
public class FlwTaskApproveReqVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "审批意见", required = true, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;

    @ApiModelProperty(name = "", notes = "")
    private String operatorId;
    
}