package org.kobus.spring.mapper.pay;

import java.util.List;

import org.kobus.spring.domain.pay.ResSeasonUsageDTO;
import org.kobus.spring.domain.pay.SeasonTicketDTO;

public interface SeasonTicketMapper {
    List<SeasonTicketDTO> selectSeasonTicketListByKusid(String kusid);
    List<ResSeasonUsageDTO> selectUsedDatesByKusid(String kusid);
}
