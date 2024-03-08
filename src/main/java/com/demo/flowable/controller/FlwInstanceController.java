package com.demo.flowable.controller;

import com.demo.flowable.controller.vo.instance.FlwInstanceActiveReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceSuspendReqVO;
import com.demo.flowable.controller.vo.instance.FlwInstanceCreateReqVO;
import com.demo.flowable.service.IFlwInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author : gr
 * @date : 2024/2/29 15:09
 */
@Api("流程实例")
@RestController
@RequestMapping("instance")
public class FlwInstanceController {

    @Resource
    private IFlwInstanceService processInstanceService;

    @ApiOperation("获取实例")
    @GetMapping("/get/{id}")
    public HistoricProcessInstance get(@PathVariable("id") String processInstanceId) {
        return processInstanceService.getHistoricProcessInstance(processInstanceId);
    }
    
    @ApiOperation("新建实例")
    @PostMapping("/create")
    public String create(@Valid @RequestBody FlwInstanceCreateReqVO createReqVO ){
       return processInstanceService.createProcessInstance(createReqVO);
    }
    
    @ApiOperation("挂起")
    @PostMapping("/suspend")
    public Boolean suspend(@Valid @RequestBody FlwInstanceSuspendReqVO reqVO ){
        return processInstanceService.suspendInstance(reqVO);
    }
    
    @ApiOperation("激活")
    @PostMapping("/active")
    public Boolean active(@RequestBody FlwInstanceActiveReqVO reqVO ){
        return processInstanceService.activeInstance(reqVO);
    }
    
}
