package org.kobus.spring.service.pay;

import org.kobus.spring.domain.pay.FreepassPaymentDTO;
import org.kobus.spring.domain.pay.PaymentCommonDTO;
import org.kobus.spring.mapper.pay.FreePassPaymentMapper;
import org.kobus.spring.mapper.pay.PaymentCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreePassPaymentServiceImpl implements FreePassPaymentService{

    @Autowired
    private PaymentCommonMapper paymentMapper;

    @Autowired
    private FreePassPaymentMapper freepassMapper;

    @Transactional
    public int processFreepassPayment(PaymentCommonDTO payDto, FreepassPaymentDTO freepassDto) {

        // 1. 공통 결제정보 저장 (PK 생성)
        int inserted1 = paymentMapper.insertPaymentCommon(payDto);

        // 2. FK인 payment_id를 프리패스 DTO에 넣기
        freepassDto.setPaymentId(payDto.getPaymentId());

        // 3. 프리패스 결제정보 저장
        int inserted2 = freepassMapper.insertFreepassPayment(freepassDto);

        return (inserted1 > 0 && inserted2 > 0) ? 1 : 0;
    }
}
