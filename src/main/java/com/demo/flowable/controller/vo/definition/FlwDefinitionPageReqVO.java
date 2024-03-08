package com.demo.flowable.controller.vo.definition;

import com.demo.flowable.controller.vo.page.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author : gr
 * @date : 2024/3/2 15:51
 */
@ApiModel(value = " 流程定义分页 Request VO")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlwDefinitionPageReqVO extends BasePageQuery {

    @ApiModelProperty(value = "标识-精准匹配", example = "process1641042089407")
    private String key;

}
