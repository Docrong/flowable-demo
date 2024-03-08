package com.demo.flowable.controller.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : gr
 * @date : 2024/3/7 12:50
 */
@Schema(description = "管理后台 - 流程任务的精简 Response VO")
@Data
@Accessors(chain = true)
public class FlwTaskSimpleRespVO {

    @Schema(description = "任务定义的标识", example = "Activity_one")
    private String definitionKey;

    @Schema(description = "任务名词", example = "经理审批")
    private String name;

}
