package org.kobus.spring.service.pay;

import org.kobus.spring.domain.FreePassPaymentDTO;

public interface PaymentService {
	void saveFreePassPayment(FreePassPaymentDTO dto);

	FreePassPaymentDTO getPaymentByImpUid(String impUid); // 선택
}
