package com.xiaomi.infrastructure.persistence.mapper;

import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Mapper
public interface BatterySignalMapper {
    @Insert("INSERT INTO battery_signal(car_id, signal_data, processed, signal_time) " +
            "VALUES(#{carId}, #{signalData}, #{processed}, #{signalTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(BatterySignalDTO entity);

    @Select("SELECT * FROM battery_signal WHERE car_id = #{carId} " +
            "ORDER BY signal_time DESC LIMIT 1")
    Optional<BatterySignalDTO> selectLatestByCarId(String carId);

    @Select("SELECT * FROM battery_signal WHERE processed = false " +
            "ORDER BY signal_time ASC LIMIT #{limit}")
    List<BatterySignalDTO> selectUnprocessedSignals(int limit);

    @Update("UPDATE battery_signal SET processed = true WHERE id = #{id}")
    void updateProcessed(Long id);

    int saveSign(BatterySignal batterySignal);


    int updateSign(BatterySignal batterySignal);

    List<BatterySignal> selectByCarId(Long carId);

    int deleteById(Long id);

    BatterySignal selectById(Long id);

    List<BatterySignal> selectBatch(@Param("limit") int limit, @Param("statue") Boolean statue);

    void updateSignStatue(@Param("id") Long id,@Param("statue") boolean statue);

}

