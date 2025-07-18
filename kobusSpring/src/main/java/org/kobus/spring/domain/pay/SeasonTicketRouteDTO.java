package org.kobus.spring.domain.pay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonTicketRouteDTO {
	
	private String routeId;
	private String adtnDeprNm;
	private String adtnArvlNm;
	private String adtnDeprCd;
	private String adtnArvlCd;
	private String deprNm;
	private String arvlNm;
	private String adtnPrdSellSttDt;

}
