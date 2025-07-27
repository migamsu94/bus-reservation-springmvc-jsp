package org.kobus.spring.domain.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestPayDTO {

	private String imp_uid;
	private String merchant_uid;
	private String pay_method;
	private String amount;
	private String pay_status;
	private String pg_tid;
	private String paid_at;
	private String user_id;
	private String bshid;
	private String selectedSeatIds;
	private String seat_number;
	private String boarding_dt;
	private String resId;
	private String changeResId;
	private String deprDt;
	private String deprTime;
	private String deprNm;
	private String arvlNm;
	private String arvlDt;
	private String takeDrtmOrg;
	private String cacmNm;
	private String indVBusClsCd;
	private String selSeatCnt;
	private String seatNos;
	private String selAdltCnt;
	private String selTeenCnt;
	private String selChldCnt;
	private String pathDvs;

	// 왕복 관련 필드
	private String selAdltCnt2;
	private String selTeenCnt2;
	private String selChldCnt2;
	private String selectedSeatIds1;
	private String selectedSeatIds2;
	private String selSeatNum2;
	private String selSeatCnt2;
	private String allTotAmtPrice1;
	private String allTotAmtPrice2;
	private String bshid2;
	private String cacmCd2;
	private String cacmNm2;
	private String indVBusClsCd2;
	private String arvlSeatNos;

}
