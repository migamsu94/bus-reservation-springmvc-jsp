package org.kobus.spring.service.pay;

import org.kobus.spring.domain.FreePassPaymentDTO;
import org.kobus.spring.mapper.pay.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

	@Autowired
    private PaymentMapper paymentMapper;

    @Override
    public void saveFreePassPayment(FreePassPaymentDTO dto) {
        paymentMapper.insertPayment(dto);
    }

    @Override
    public FreePassPaymentDTO getPaymentByImpUid(String impUid) {
        return paymentMapper.selectPaymentByImpUid(impUid);
    }
}
