package com.demo.flowable.controller.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/5 22:17
 */
@ApiModel(value = "管理后台 - 流程实例的挂起 Request VO")
@Data
public class FlwInstanceSuspendReqVO {


    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    @NotEmpty(message = "流程实例的编号不能为空")
    private List<String> ids;

    @ApiModelProperty(value = "作废原因", required = true, example = "不请假了！")
    @NotEmpty(message = "作废原因不能为空")
    private String reason;

    @ApiModelProperty(value = "IP")
    private String ip;

}
