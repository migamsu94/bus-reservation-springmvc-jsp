package org.kobus.spring.mapper.pay;

import org.kobus.spring.domain.pay.ResPassUsageDTO;
import org.kobus.spring.domain.pay.ResSeasonUsageDTO;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;
import org.kobus.spring.domain.reservation.ResvDTO;

public interface BusReservationMapper {
	
	int insertReservation(ResvDTO resvDto);
	int insertReservationPayment(ReservationPaymentDTO dto);
//	int changeReservation(@Param("resId") String resId);
	String generateResId();
	
	// 예매 후 RESERVEDSEATS 에 좌석정보 레코드 추가
	int callAfterReservation(String resId, String busId, String seatList);
	
	int insertSeasonUsage(ResSeasonUsageDTO resSeasonUsageDTO);
	int insertFreePassUsage(ResPassUsageDTO resPassUsageDTO);

}
