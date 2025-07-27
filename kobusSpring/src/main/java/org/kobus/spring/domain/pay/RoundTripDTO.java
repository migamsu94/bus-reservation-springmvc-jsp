package org.kobus.spring.domain.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripDTO {

	private String deprCd;
	private String deprDtRaw;
	private String deprTime;
	private String deprNm;

	private String arvlCd;
	private String arvlNm;
	private String arvlDtRaw;

	private String takeDrtmOrg;

	private String pathDvs;

	// 편도 필드
	private String cacmCd;
	private String cacmNm;
	private String indVBusClsCd;

	private String selAdltCnt;
	private String selChldCnt;
	private String selTeenCnt;

	private String selectedSeatIds;
	private String selSeatNum;
	private String selSeatCnt;
	private String allTotAmtPrice;
	private String busCode;
	private String changeResId;

	// 왕복 가는편 필드 (rtrpDtl1)
	private String selectedSeatIds1;
	private String selSeatNum1;
	private String selSeatCnt1;
	private String selAdltCnt1;
	private String selTeenCnt1;
	private String selChldCnt1;
	private String allTotAmtPrice1;
	private String indVBusClsCd1;
	private String cacmCd1;
	private String cacmNm1;
	private String busCode1;

	// 왕복 오는편 필드 (rtrpDtl2)
	private String selectedSeatIds2;
	private String selSeatNum2;
	private String selSeatCnt2;
	private String selAdltCnt2;
	private String selTeenCnt2;
	private String selChldCnt2;
	private String allTotAmtPrice2;
	private String indVBusClsCd2;
	private String cacmCd2;
	private String cacmNm2;
	private String busCode2;

}
