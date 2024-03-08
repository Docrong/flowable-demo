package com.demo.flowable.controller;

import com.demo.flowable.controller.vo.model.*;
import com.demo.flowable.service.IFlwModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/3/1 8:57
 */
@Api("模型")
@RestController
@RequestMapping("/model")
public class FlwModelController {

    @Resource
    private IFlwModelService modelService;

    @ApiOperation("获取模型列表")
    @PostMapping("/list")
    public List<ModelPageItemRespVO> list(@RequestBody ModelPageReqVO reqVO) {
        return modelService.getModelList();
    }

    @ApiOperation(value = "新建模型")
    @PostMapping("/create")
    public String createModel(@Valid @RequestBody ModelCreateReqVO createRetVO) {
        return modelService.createModel(createRetVO, null);
    }

    @ApiOperation("删除模型")
    @GetMapping("/delete/{id}")
    public Boolean deleteModel(@PathVariable(value = "id") String id) {
        return modelService.deleteModel(id);
    }

    @ApiOperation(value = "获得模型")
    @GetMapping("/get/{id}")
    public ModelRespVO getModel(@PathVariable(value = "id") String id) {
        return modelService.getModel(id);
    }

    @ApiOperation(value = "修改模型")
    @PostMapping("/update")
    public Boolean updateModel(@Valid @RequestBody ModelUpdateReqVO updateReqVO) {
        return modelService.updateModel(updateReqVO);
    }

    @ApiOperation("部署模型")
    @GetMapping("/deploy/{id}")
    public Boolean deploy(@PathVariable(value = "id") String id) {
        return modelService.deployModel(id);
    }

}
