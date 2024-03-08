package com.demo.flowable.controller;

import com.demo.flowable.controller.vo.definition.FlwDefinitionPageReqVO;
import com.demo.flowable.service.IFlwDefinitionService;
import com.demo.flowable.controller.vo.definition.FlwDefinitionRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/1 10:56
 */
@Api("流程定义")
@RestController
@RequestMapping("/definition")
public class FlwDefinitionController {

    @Resource
    private IFlwDefinitionService processDefinitionService;

    @ApiOperation("获取流程定义列表")
    @PostMapping("/list")
    public List<FlwDefinitionRespVO> list(@RequestBody(required = false) FlwDefinitionPageReqVO reqVO) {
        return processDefinitionService.list();
    }
}
