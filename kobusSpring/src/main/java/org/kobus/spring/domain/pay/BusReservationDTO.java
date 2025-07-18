package org.kobus.spring.domain.pay;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusReservationDTO {
	
	private String resId;
	private String kusid;
	private Date depDate;
	private String depTime;
	private String depTerminal;
	private String arrTerminal;
	private String busGrade;
	private String seatNo;
	private int fare;
	private Timestamp regDt;

}
