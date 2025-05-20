package com.xiaomi.domain.dto.warning;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarningRequest {
    @NotBlank(message = "车架编号不能为空")
    private Long carId;

    private Integer warnId; // 可选规则编号

    @NotBlank(message = "信号数据不能为空")
    private String signal; // JSON格式信号数据
}