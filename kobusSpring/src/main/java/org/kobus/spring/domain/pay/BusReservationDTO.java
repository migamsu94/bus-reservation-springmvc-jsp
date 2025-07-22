package org.kobus.spring.domain.pay;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusReservationDTO {
	
	private String resId;           // 예매 ID
    private String bshId;           // 가는 편 버스 스케줄 ID
    private String returnBshId;     // 오는 편 버스 스케줄 ID
    private String returnSeatId;    // 오는 편 좌석 ID

    private String kusid;           // 회원 ID
    private Timestamp rideDate;     // 탑승일자 (가는 편)
    private Timestamp returnRideDate; // 오는 편 탑승일자

    private Timestamp resvDate;     // 예매일자
    private String resvStatus;      // 예매 상태
    private String resvType;        // 예매 타입

    private long qrCode;            // 탑승 QR 코드
    private int mileage;            // 마일리지
    private String seatable;        // 예매 유무 ('Y'/'N')

}
