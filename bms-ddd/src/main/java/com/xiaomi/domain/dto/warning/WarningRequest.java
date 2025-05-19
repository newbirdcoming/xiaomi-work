package com.xiaomi.domain.dto.warning;


import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WarningRequest {
    @NotBlank(message = "车架编号不能为空")
    private Long carId;

    private Integer warnId; // 可选规则编号

    @NotBlank(message = "信号数据不能为空")
    private String signal; // JSON格式信号数据
}