package com.demo.flowable.controller.vo.instance;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/5 22:35
 */
@ApiModel(value = "管理后台 - 流程实例的激活 Request VO")
@Data
public class FlwInstanceActiveReqVO {

    private List<String> ids;

    private String message;
}
