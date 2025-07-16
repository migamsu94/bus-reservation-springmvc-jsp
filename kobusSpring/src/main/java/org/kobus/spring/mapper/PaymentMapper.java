package org.kobus.spring.mapper;

import org.kobus.spring.domain.FreePassPaymentDTO;

public interface PaymentMapper {
	
    // 프리패스 결제 정보 저장
    int insertPayment(FreePassPaymentDTO dto);

    // 프리패스 결제 정보 조회 (선택사항)
    FreePassPaymentDTO selectPaymentByImpUid(String impUid);
    
    // 기타 필요한 메서드 예: 취소 처리, 결제 목록 등
}
