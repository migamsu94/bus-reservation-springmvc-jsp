package org.kobus.spring.domain.reservation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResvDTO {
	
	private String resId; 			/* 예매번호 */
	private String kusid; 			/* 예매 아이디 */
	private String seatNo; 			/* 좌석번호 */
	private LocalDateTime rideDate; 		/* 탑승일 */
	private LocalDateTime resvDate; 		/* 예약일 */
	private String resvStatus; 		/* 예약상태 */
	private String resvType; 		/* 예약경로 (인터넷 or 모바일) */
	private long qrCode; 			/* 탑승 qrcode */
	private String deprRegCode; 	/* 출발지코드 */
	private String deprRegName; 	/* 출발지명 */
	private String arrRegCode; 		/* 도착지코드 */
	private String arrRegName; 		/* 도착지명 */
	private String comName;         /*고속사*/
	private String busGrade; 		/* 등급 */
	private String bshId; 		/* 버스스케줄 id */
	private int durMin;
	private int amount; 			/* 총 결제금액 */
	private String payMethod;
	private int totalCount; 		/* 총인원수 */
	private int aduCount; 		/* 일반좌석수 */
	private int stuCount; 		/* 학생좌석수 */
	private int chdCount; 		/* 아이좌석수 */
	private String pathDvs;  // 편도(sngl)/환승(trtr)/왕복(rtrp)
	
	private String rideDateStr;     /* 포맷된 탑승일 문자열 */
	private String rideTimeStr;     /* 포맷된 탑승일 문자열 */
	private String resvDateStr;     /* 포맷된 예약일 문자열 */
	private int mileage;
	private String seatable;
	
	// 오는 편 전용 필드
	private String selAdltCnt2;         // 복편 성인 인원 수
	private String selTeenCnt2;         // 복편 청소년 인원 수
	private String selChldCnt2;         // 복편 어린이 인원 수
	private String selectedSeatIds2;    // 복편 선택한 좌석 ID 목록 (예: SEAT001,SEAT002)
	private String selSeatNum2;         // 복편 선택 좌석 번호 문자열
	private String selSeatCnt2;         // 복편 총 좌석 수
	private String allTotAmtPrice1;     // 복편 가는편 금액
	private String allTotAmtPrice2;     // 복편 오는편 금액
	private String busId2;            // 복편 버스 코드
	private String cacmCd2;             // 복편 노선 코드
	private String cacmNm2;             // 복편 노선명
	private String indVBusClsCd2;       // 복편 버스 등급 코드
	private String arvlSeatNos;         // 복편 도착 좌석 번호 (예: 1A, 1B)
	

}
