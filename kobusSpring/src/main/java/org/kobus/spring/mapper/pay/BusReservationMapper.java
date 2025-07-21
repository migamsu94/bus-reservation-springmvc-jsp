package org.kobus.spring.mapper.pay;

import org.apache.ibatis.annotations.Param;
import org.kobus.spring.domain.pay.BusReservationDTO;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;

public interface BusReservationMapper {
	
	int insertReservation(BusReservationDTO dto);
	int insertReservationPayment(ReservationPaymentDTO dto);
	int changeReservation(@Param("resId") String resId);
	String generateResId();

}
