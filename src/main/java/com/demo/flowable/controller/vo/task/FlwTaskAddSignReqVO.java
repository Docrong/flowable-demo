package com.demo.flowable.controller.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author : gr
 * @date : 2024/3/7 12:51
 */
@ApiModel(description = "管理后台 - 加签流程任务的 Request VO")
@Data
public class FlwTaskAddSignReqVO {

    @ApiModelProperty(value = "需要加签的任务 ID")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "加签的用户 ID")
    @NotEmpty(message = "加签用户 ID 不能为空")
    private Set<Long> userIdList;

    @ApiModelProperty(value = "加签类型，before 向前加签，after 向后加签")
    @NotEmpty(message = "加签类型不能为空")
    private String type;

    @ApiModelProperty(value = "加签原因")
    @NotEmpty(message = "加签原因不能为空")
    private String reason;

}
