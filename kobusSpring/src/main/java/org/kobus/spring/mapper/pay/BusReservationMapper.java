package org.kobus.spring.mapper.pay;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;
import org.kobus.spring.domain.reservation.ResvDTO;

public interface BusReservationMapper {
	
	String findId(@Param("username") String username);
	
	int insertReservation(ResvDTO resvDto);
	int insertReservationPayment(ReservationPaymentDTO dto);
//	int changeReservation(@Param("resId") String resId);
	String generateResId();
	
	// 예매 후 RESERVEDSEATS 에 좌석정보 레코드 추가
	int callAfterReservation(@Param("resId") String resId, @Param("bshId") String bshId, 
			@Param("kusId") String kusId, @Param("seatList") String seatList);
	
	int updateRemainSeats(@Param("resId") String resId, @Param("rideDateStr") String rideDateStr);

}
