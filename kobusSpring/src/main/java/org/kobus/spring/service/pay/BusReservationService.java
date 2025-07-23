package org.kobus.spring.service.pay;

import org.kobus.spring.domain.pay.PaymentCommonDTO;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;
import org.kobus.spring.domain.reservation.ResvDTO;
import org.kobus.spring.mapper.pay.BusReservationMapper;
import org.kobus.spring.mapper.pay.PaymentCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusReservationService {

    @Autowired
    private BusReservationMapper reservationMapper;

    @Autowired
    private PaymentCommonMapper commonMapper;

    @Transactional
    public boolean saveReservationAndPayment(
    		ResvDTO resvDto,
        PaymentCommonDTO payDto,
       	
        ReservationPaymentDTO linkDto
    ) {
    	
    	
        // 1. 예매 저장
        int insertedReservation = reservationMapper.insertReservation(resvDto);

        // 2. 결제 저장 (selectKey에 의해 payDto.paymentId 채워짐)
        int insertedPayment = commonMapper.insertPaymentCommon(payDto);

        // 3. 연결 테이블 insert
        linkDto.setPaymentId(payDto.getPaymentId()); // paymentId 전달
        linkDto.setResId(resvDto.getResId());
        linkDto.setKusid(resvDto.getKusid());
        
        String resId = resvDto.getResId();
        String busId = resvDto.getBshId();
        String seatList = resvDto.getSeatNo();
        String kusId = resvDto.getKusid();
        
        
        System.out.printf("=================================");
        System.out.printf("resId : %s, busId : %s, kusId : %s, seatList : %s", resId, busId, kusId, seatList);
        System.out.printf("=================================");
        
        
        
        
        
        int updateReservedSeat = reservationMapper.callAfterReservation(resId, busId, kusId, seatList);
        
        int insertedLink = reservationMapper.insertReservationPayment(linkDto);

        return insertedReservation > 0 && insertedPayment > 0 && insertedLink > 0 && updateReservedSeat > 0;
    }


}
