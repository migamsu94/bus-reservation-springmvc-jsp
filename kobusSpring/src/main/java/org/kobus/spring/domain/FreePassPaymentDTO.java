package org.kobus.spring.domain;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreePassPaymentDTO {
	private String freePassPayId;  // FREE_PASS_PAY_ID
	private String paymentId;      // PAYMENT_ID
	private String kusid;          // KUSID
	private String adtnPrdSno;     // ADTN_PRD_SNO
	private Date startDate;        // START_DATE
}
