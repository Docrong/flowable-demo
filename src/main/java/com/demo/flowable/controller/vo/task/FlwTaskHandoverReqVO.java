package com.demo.flowable.controller.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : gr
 * @date : 2024/3/7 13:52
 */
@ApiModel(value = "管理后台 - 流程任务的更新负责人的 Request VO")
@Data
public class FlwTaskHandoverReqVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "新审批人的用户编号", required = true, example = "2048")
    @NotNull(message = "新审批人的用户编号不能为空")
    private String assigneeId;
}
