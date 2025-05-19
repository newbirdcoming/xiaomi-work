package com.xiaomi.interfaces.controller;


import com.xiaomi.application.service.WarningRuleApplicationService;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.interfaces.vo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sign")
public class SignController {

    private final BatterySignalRepository batterySignalRepository;

//    增
    @PostMapping("/addSign")
    public ResponseResult<BatterySignal> addRule(@RequestBody BatterySignal batterySignal) {
        int insert = batterySignalRepository.saveSign(batterySignal);
        if (insert>0)
            return ResponseResult.success("规则新增成功");
        else
            return ResponseResult.error("添加规则失败");
    }

//删
    @DeleteMapping("/delete/{id}")
    public ResponseResult<BatterySignal> delete(@PathVariable("id") Long id) {
        int res= batterySignalRepository.delete(id);
        if (res>0)
            return ResponseResult.success("删除成功");
        else
            return ResponseResult.error("删除失败");
    }

//改
    @PostMapping("/updateSign")
    public ResponseResult<BatterySignal> updateSign(@RequestBody BatterySignal batterySignal) {
        int i = batterySignalRepository.updateSign(batterySignal);
        if (i>0)
            return ResponseResult.success("规则更新成功");
        else
            return ResponseResult.error("更新规则失败");
    }
    //查
    @GetMapping("/selectByCarId/{carId}")
    public ResponseResult<List<BatterySignal>> selectByCarId(@PathVariable("carId") Long carId) {
        List<BatterySignal> batterySignal=batterySignalRepository.selectByCarId(carId);
        if (batterySignal!=null)
            return ResponseResult.success(batterySignal,"查询成功");
        else
            return ResponseResult.error("更新规则失败");
    }

    @GetMapping("/selectById/{id}")
    public ResponseResult<BatterySignal> selectById(@PathVariable("id") Long carId) {
        BatterySignal batterySignal=batterySignalRepository.selectById(carId);
        if (batterySignal!=null)
            return ResponseResult.success(batterySignal,"查询成功");
        else
            return ResponseResult.error("更新规则失败");
    }


}
