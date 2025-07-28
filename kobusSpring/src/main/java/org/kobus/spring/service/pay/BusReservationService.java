package org.kobus.spring.service.pay;

import java.sql.SQLException;

import org.kobus.spring.domain.pay.PaymentCommonDTO;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;
import org.kobus.spring.domain.reservation.ResvDTO;
import org.kobus.spring.mapper.pay.BusReservationMapper;
import org.kobus.spring.mapper.pay.PaymentCommonMapper;
import org.kobus.spring.mapper.reservation.ResvMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusReservationService {

    @Autowired
    private BusReservationMapper reservationMapper;
    
    @Autowired ResvMapper modifyResvMapper;

    @Autowired
    private PaymentCommonMapper commonMapper;

    @Transactional
    public boolean saveReservationAndPayment(
    		ResvDTO resvDto,
        ResvDTO rtnResvDto, 
        PaymentCommonDTO payDto,
        ReservationPaymentDTO linkDto, 
        String changeResId) throws SQLException {
    	
    	
    	if (changeResId != null || !"undefined".equals(changeResId) 
    			|| (changeResId != null && !changeResId.equals(""))) {

    		int cancel = modifyResvMapper.cancelResvList(changeResId);
    		int delete = modifyResvMapper.deleteResv(changeResId);

    	}
    	
        // 1. 예매 저장
        int insertedReservation = reservationMapper.insertReservation(resvDto);

        // 2. 결제 저장 (selectKey에 의해 payDto.paymentId 채워짐)
        int insertedPayment = commonMapper.insertPaymentCommon(payDto);

        // 3. 연결 테이블 insert
        linkDto.setPaymentId(payDto.getPaymentId()); // paymentId 전달
        linkDto.setResId(resvDto.getResId());
        linkDto.setKusid(resvDto.getKusid());
        
        String resId = resvDto.getResId();
        String bshId = resvDto.getBshId();
        String seatList = resvDto.getSeatNo();
        String kusId = resvDto.getKusid();
        String rideDateStr = resvDto.getRideDateStr();
        int selAdltCnt = resvDto.getAduCount();
        int selTeenCnt = resvDto.getStuCount();
        int selChldCnt = resvDto.getChdCount();
        
        
        
        System.out.printf("=================================");
        System.out.printf("resvDto resId : %s, busId : %s, kusId : %s, seatList : %s", resId, bshId, kusId, seatList);
        System.out.printf("=================================");
        
        int updateReservedSeat = reservationMapper.callAfterReservation(resId, bshId, kusId, seatList, selAdltCnt, selTeenCnt, selChldCnt);
        
        int updateRemainSeats = reservationMapper.updateRemainSeats(resId, rideDateStr);
        
        int insertedLink = reservationMapper.insertReservationPayment(linkDto);
        
        if (rtnResvDto != null) {
        	
        	String resId1 = rtnResvDto.getResId();
            String bshId1 = rtnResvDto.getBshId();
            String seatList1 = rtnResvDto.getSeatNo();
            String kusId1 = rtnResvDto.getKusid();
            String rideDateStr1 = rtnResvDto.getRideDateStr();
            int selAdltCnt1 = rtnResvDto.getAduCount();
            int selTeenCnt1 = rtnResvDto.getStuCount();
            int selChldCnt1 = rtnResvDto.getChdCount();
            
            System.out.printf("=================================");
            System.out.printf("rtnResvDto resId : %s, busId : %s, kusId : %s, seatList : %s", resId1, bshId1, kusId1, seatList1);
            System.out.printf("=================================");
        	
        	// 1. 예매 저장
            int insertedReservation1 = reservationMapper.insertReservation(rtnResvDto);

            // 2. 결제 저장 (selectKey에 의해 payDto.paymentId 채워짐)
            int insertedPayment1 = commonMapper.insertPaymentCommon(payDto);

            // 3. 연결 테이블 insert
            linkDto.setPaymentId(payDto.getPaymentId()); // paymentId 전달
            linkDto.setResId(rtnResvDto.getResId());
            linkDto.setKusid(rtnResvDto.getKusid());
            
            
            int updateReservedSeat1 = reservationMapper.callAfterReservation(resId1, bshId1, kusId1, seatList1, selAdltCnt1, selTeenCnt1, selChldCnt1);
            
            int updateRemainSeats1 = reservationMapper.updateRemainSeats(resId1, rideDateStr1);
            
            int insertedLink1 = reservationMapper.insertReservationPayment(linkDto);
		}

        return insertedReservation > 0 && insertedPayment > 0 && insertedLink > 0 && updateReservedSeat > 0 && updateRemainSeats > 0;
    }


}
