package org.kobus.spring.service.pay;

import org.kobus.spring.domain.pay.FreepassPaymentDTO;
import org.kobus.spring.domain.pay.PaymentCommonDTO;

public interface FreePassPaymentService {

    int processFreepassPayment(PaymentCommonDTO payDto, FreepassPaymentDTO freepassDto);
    
} // interface
