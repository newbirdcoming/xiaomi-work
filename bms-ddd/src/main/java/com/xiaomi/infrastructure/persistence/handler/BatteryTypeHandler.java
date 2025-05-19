package com.xiaomi.infrastructure.persistence.handler;

import com.xiaomi.domain.model.vehicle.BatteryType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(BatteryType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)  // 对应数据库的VARCHAR或ENUM类型
public class BatteryTypeHandler extends BaseTypeHandler<BatteryType> {

    // 插入时：将枚举转换为数据库存储的中文描述
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    BatteryType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDescription());
    }

    // 查询时：将数据库中文描述转换为枚举
    @Override
    public BatteryType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String description = rs.getString(columnName);
        return description == null ? null : BatteryType.fromDescription(description);
    }

    // 其他方法（从列索引获取结果）
    @Override
    public BatteryType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String description = rs.getString(columnIndex);
        return description == null ? null : BatteryType.fromDescription(description);
    }

    @Override
    public BatteryType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String description = cs.getString(columnIndex);
        return description == null ? null : BatteryType.fromDescription(description);
    }
}