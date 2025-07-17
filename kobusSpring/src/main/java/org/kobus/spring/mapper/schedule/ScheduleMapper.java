package org.kobus.spring.mapper.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kobus.spring.domain.schedule.ScheduleDTO;

@Mapper
public interface ScheduleMapper {

    List<ScheduleDTO> selectBySidoCode(@Param("sidoCode") int sidoCode);

    List<ScheduleDTO> selectByRegion();

    List<ScheduleDTO> searchBusSchedule(
        @Param("deprId") String deprId,
        @Param("arrId") String arrId,
        @Param("deprDtm") String deprDtm,
        @Param("busClsCd") String busClsCd
    );

    int getDurationFromRoute(
        @Param("deprRegId") String deprRegId,
        @Param("arvlRegId") String arvlRegId
    );
}
