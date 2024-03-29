package com.demo.flowable.controller.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : gr
 * @date : 2024/3/7 14:48
 */
@ApiModel(description = "管理后台 - 流程任务的 Running 进行中的分页项 Response VO")
@Data
public class FlwTaskTodoPageItemRespVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "任务名字", required = true, example = "芋道")
    private String name;

    @ApiModelProperty(value = "类型", required = true, example = "类型")
    private String category;

    @ApiModelProperty(value = "接收时间", required = true)
    private LocalDateTime claimTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "激活状态-参见 SuspensionState 枚举", required = true, example = "1")
    private Integer suspensionState;

    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    @Data
    @ApiModel(value = "流程实例")
    public static class ProcessInstance {

        @ApiModelProperty(value = "流程实例编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "流程实例名称", required = true, example = "芋道")
        private String name;

        @ApiModelProperty(value = "发起人的用户编号", required = true, example = "1024")
        private String startUserId;

        @ApiModelProperty(value = "发起人的用户昵称", required = true, example = "芋艿")
        private String startUserNickname;

        @ApiModelProperty(value = "流程定义的编号", required = true, example = "2048")
        private String processDefinitionId;

    }

}