package com.demo.flowable.controller;

import com.demo.flowable.controller.vo.task.*;
import com.demo.flowable.service.IFlwTaskService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author : gr
 * @date : 2024/2/29 14:40
 */
@Api("任务 控制器")
@RestController
@RequestMapping("/task")
public class FlwTaskController {

    @Resource
    private IFlwTaskService taskService;

    @ApiOperation("获取任务列表")
    @PostMapping("/getTasksByProcessInstanceIds")
    public List<TaskEntityImpl> getTasksByProcessInstanceIds(@RequestBody(required = false) FlwTaskPageReqVO reqVO) {
        return taskService.getTasksByProcessInstanceIds(Lists.newArrayList("8f0f92c0-daf1-11ee-9189-00ff39783d35"));
    }

    @GetMapping("/list-by-process-instance-id")
    @ApiOperation(value = "获得指定流程实例的任务列表", notes = "包括完成的、未完成的")
    public List<FlwTaskRespVO> getTaskListByProcessInstanceId(@RequestParam("processInstanceId") String processInstanceId) {
        return taskService.getTaskListByProcessInstanceId(processInstanceId);
    }

    @ApiOperation("通过任务")
    @PostMapping("/approveTask")
    public Boolean approveTask(@RequestBody FlwTaskApproveReqVO reqVO) {
        return taskService.approveTask(reqVO);
    }

    @ApiOperation("否决任务")
    @PostMapping("/rejectTask")
    public Boolean rejectTask(@RequestBody FlwTaskRejectReqVO reqVO) {
        return taskService.rejectTask(reqVO);
    }
    
    @ApiOperation("通过taskId作废实例")
    @PostMapping("/cancel")
    public String cancelTask(@RequestBody String str ){
        return null;
    }

    @ApiOperation("移交任务")
    @PostMapping("/handover")
    public Boolean handover(@RequestBody FlwTaskHandoverReqVO reqVO) {
        return taskService.handover(reqVO);
    }

    @ApiOperation(value = "跳转任务")
    @PostMapping("/returnTask")
    public Boolean returnTask(@Valid @RequestBody FlwTaskReturnReqVO reqVO) {
        return taskService.returnTask(reqVO);
    }

    @ApiOperation("获取可以跳转的节点")
    @GetMapping("/returnTaskList/{taskId}")
    public List<FlwTaskSimpleRespVO> getReturnTaskList(@PathVariable("taskId") String taskId) {
        return taskService.getReturnTaskList(taskId);
    }

    @ApiOperation(value = "加签", notes = "before 前加签，after-new 后加签")
    @PostMapping("/createSign")
    public Boolean createSign(@RequestBody FlwTaskAddSignReqVO reqVO) {
        return taskService.createSignTask(reqVO);
    }

    
    @ApiOperation("diagram")
    @PostMapping("/diagram")
    public ResponseEntity<byte[]> diagram(String str, HttpServletResponse response){
        return null;
    }
}
