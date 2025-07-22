package org.kobus.spring.mapper.pay;

import java.util.List;

import org.kobus.spring.domain.pay.FreePassDTO;
import org.kobus.spring.domain.pay.ResPassUsageDTO;

public interface FreePassMapper {
    List<FreePassDTO> selectFreePassListByKusid(String kusid);
    List<ResPassUsageDTO> selectUsedDatesByKusid(String kusid);
}
