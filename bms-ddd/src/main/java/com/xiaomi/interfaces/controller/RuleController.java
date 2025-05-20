package com.xiaomi.interfaces.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xiaomi.application.service.WarningRuleApplicationService;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.interfaces.exception.DMSException;
import com.xiaomi.interfaces.exception.ExceptionType;
import com.xiaomi.interfaces.vo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xiaomi.interfaces.exception.ExceptionType.SYSTEM_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rule")
public class RuleController {
    private final WarningRuleRepository warningRuleRepository;

    private final WarningRuleApplicationService warningRuleApplicationService;
    @PostMapping("/addRule")
    public ResponseResult addRule(@RequestBody WarningRule rule) {
        int insert = warningRuleRepository.insert(rule);
        if (insert>0)
            return ResponseResult.success("新增成功");
        else
            return ResponseResult.error("新增失败");
    }


    /**
     * 根据规则编号获取规则并解析
     * @param ruleId
     * @return
     */
    @GetMapping("/findByRuleId/{ruleId}")
    public List<WarningRule> findByRuleId(@PathVariable("ruleId") Integer ruleId) {
        List<WarningRule> byRuleId = warningRuleApplicationService.getWarningRuleByRuleId(ruleId);
        return byRuleId;
    }


    /**
     * 根据电池类型获取规则
     * @param
     * @return
     */
    @GetMapping("/findRuleByBatteryType")
    public List<WarningRule> findRuleByBatteryType(@RequestParam String  batteryType) {
        List<WarningRule> byBatteryType = warningRuleRepository.findByBatteryType(batteryType);
        return byBatteryType;
    }


    @PostMapping("/addRuleBatch")
    @Transactional // 使用事务管理
    public ResponseResult<String> addRuleBatch(@RequestBody String rules) {
        // 参数校验
        if (StringUtils.isEmpty(rules)) {
            return ResponseResult.error("规则数据不能为空");
        }

        try {
            // 解析 JSON
            List<WarningRule> warningRuleList = JSON.parseArray(rules, WarningRule.class);

            // 验证列表是否为空
            if (CollectionUtils.isEmpty(warningRuleList)) {
                return ResponseResult.error("规则列表不能为空");
            }

            for (WarningRule warningRule : warningRuleList) {
                warningRuleRepository.insert(warningRule);
            }
            return ResponseResult.success("添加规则成功");
        } catch (JSONException e) {
            return ResponseResult.error("规则数据格式错误：" + e.getMessage());
        } catch (Exception e) {
            // 回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseResult.error("添加规则失败：" + e.getMessage());
        }
    }




}
