package org.kobus.spring.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kobus.spring.domain.pay.BusReservationDTO;
import org.kobus.spring.domain.pay.FreepassPaymentDTO;
import org.kobus.spring.domain.pay.PaymentCommonDTO;
import org.kobus.spring.domain.pay.ReservationPaymentDTO;
import org.kobus.spring.domain.pay.STPaymentSet;
import org.kobus.spring.mapper.pay.TermMapper;
import org.kobus.spring.service.pay.BusReservationService;
import org.kobus.spring.service.pay.FreePassPaymentService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
    private BusReservationService reservationService;
	
	@Autowired
    private TermMapper termMapper;
	
	@Autowired
	private FreePassPaymentService freepassService;  // 인터페이스 → 구현체
	
	
	// 일반 예매 결제
	@PostMapping("/Reservation.do")
	public Map<String, Object> handleReservation(HttpServletRequest request) {
	    Map<String, Object> resultMap = new HashMap<>();

	    try {
	        request.setCharacterEncoding("UTF-8");

	        // [1] request 파라미터 추출
	        String user_id = request.getParameter("user_id");
	        String resId = request.getParameter("resId");
	        String imp_uid = request.getParameter("imp_uid");
	        String merchant_uid = request.getParameter("merchant_uid");
	        String pay_method = request.getParameter("pay_method");
	        String amountStr = request.getParameter("amount");
	        String pay_status = request.getParameter("pay_status");
	        String pg_tid = request.getParameter("pg_tid");
	        String paid_at_str = request.getParameter("paid_at");

	        int amount = Integer.parseInt(amountStr);
	        long paidAtMillis = Long.parseLong(paid_at_str) * 1000L;
	        Timestamp paid_at = new Timestamp(paidAtMillis);

	        // [2] payment_common DTO 생성 (paymentId는 mapper에서 selectKey로 생성됨)
	        PaymentCommonDTO payDto = new PaymentCommonDTO();
	        payDto.setImpUid(imp_uid);
	        payDto.setMerchantUid(merchant_uid);
	        payDto.setPayMethod(pay_method);
	        payDto.setAmount(amount);
	        payDto.setPayStatus(pay_status);
	        payDto.setPgTid(pg_tid);
	        payDto.setPaidAt(paid_at);

	        // [3] reservation DTO 생성
	        BusReservationDTO resvDto = new BusReservationDTO();
	        resvDto.setResId(resId);
	        resvDto.setKusid(user_id);
	        resvDto.setBshId(request.getParameter("bus_schedule_id"));
	        resvDto.setRideDate(Timestamp.valueOf(request.getParameter("boarding_dt") + " 00:00:00"));
	        resvDto.setResvDate(new Timestamp(System.currentTimeMillis()));
	        resvDto.setResvStatus("예약");
	        resvDto.setResvType("일반");
	        resvDto.setQrCode((long) (Math.random() * 1000000000L));
	        resvDto.setMileage(0);
	        resvDto.setSeatable("Y");

	        // [4] reservation_payment DTO 생성 (paymentId는 insert 후에 설정됨)
	        ReservationPaymentDTO linkDto = new ReservationPaymentDTO();
	        linkDto.setKusid(user_id); // 아직 paymentId는 안 넣음

	        // [5] 서비스 호출 → paymentId는 여기서 자동 채워짐
	        boolean saved = reservationService.saveReservationAndPayment(resvDto, payDto, linkDto);

	        // [6] 결과 반환
	        resultMap.put("result", saved ? 1 : 0);

	    } catch (Exception e) {
	        e.printStackTrace();
	        resultMap.put("result", 0);
	    }

	    return resultMap;
	}




    

    // 정기권 결제
    @PostMapping("/Seasonticket.do")
    public Map<String, Object> handleSeasonticket(HttpServletRequest request, @RequestBody STPaymentSet dto) {
    	System.out.println("SPfreepassService 프록시 여부: " + AopUtils.isAopProxy(freepassService));
        System.out.println("SPfreepassService 실제 클래스: " + freepassService.getClass());
        Map<String, Object> result = new HashMap<>();

        try {
            // 로그인 사용자 확인
            String userId = (String) request.getSession().getAttribute("userId");
            if (userId == null) userId = "KUS003"; // 테스트용
            dto.setKusid(userId);

            boolean saved = freepassService.saveSeasonTicketPayment(dto);

            if (saved) {
                result.put("status", "success");
                result.put("message", "정기권 결제가 완료되었습니다.");
            } else {
                result.put("status", "fail");
                result.put("message", "DB 저장 실패");
            }

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    // 프리패스 결제
    @PostMapping("/Freepass.do")
    public Map<String, Object> handleFreepass(HttpServletRequest request) {
    	System.out.println("freepassService 프록시 여부: " + AopUtils.isAopProxy(freepassService));
        System.out.println("freepassService 실제 클래스: " + freepassService.getClass());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 세션에서 userId 확인
            String userId = (String) request.getSession().getAttribute("userId");
            if (userId == null) userId = "KUS003"; // 테스트용

            // 필수 파라미터 추출
            String adtnPrdSno = request.getParameter("adtn_prd_sno");
            String impUid = request.getParameter("imp_uid");
            String merchantUid = request.getParameter("merchant_uid");
            String payMethod = request.getParameter("pay_method");
            String payStatus = request.getParameter("pay_status");
            String pgTid = request.getParameter("pg_tid");
            String paidAtStr = request.getParameter("paid_at");
            String amountStr = request.getParameter("amount");
            String startDateStr = request.getParameter("startDate");

            if (impUid == null || merchantUid == null || payMethod == null || amountStr == null || adtnPrdSno == null) {
                resultMap.put("result", 0);
                resultMap.put("msg", "필수 파라미터 누락");
                return resultMap;
            }

            int amount = Integer.parseInt(amountStr);
            Timestamp paidAt = null;
            if (paidAtStr != null && !paidAtStr.isEmpty()) {
            	long timestampMillis = Long.parseLong(paidAtStr) * 1000L;
                paidAt = new Timestamp(timestampMillis);
            }
            
         // startDate 방어 코드 추가
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                resultMap.put("result", 0);
                resultMap.put("msg", "startDate가 비어 있습니다.");
                return resultMap;
            }
            System.out.println("startDateStr = [" + startDateStr + "]");
            Date startDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr).getTime());

            // DTO 구성
            PaymentCommonDTO payDto = new PaymentCommonDTO();
            payDto.setImpUid(impUid);
            payDto.setMerchantUid(merchantUid);
            payDto.setPayMethod(payMethod);
            payDto.setAmount(amount);
            payDto.setPayStatus(payStatus);
            payDto.setPgTid(pgTid);
            payDto.setPaidAt(paidAt);

            FreepassPaymentDTO freeDto = new FreepassPaymentDTO();
            freeDto.setAdtnPrdSno(adtnPrdSno);
            freeDto.setKusid(userId);
            freeDto.setStartDate(startDate);

            // 트랜잭션 처리 서비스 호출
            int result = freepassService.processFreepassPayment(payDto, freeDto);

            resultMap.put("result", result);
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", 0);
            resultMap.put("msg", "서버 오류");
            return resultMap;
        }
    }
    
    // 결제 전 금액 검증하는 부분
    @PostMapping("/fetchAmount.ajax")
    public Map<String, Object> fetchAmount(@RequestParam("adtn_prd_sno") String adtnPrdSno) {
        System.out.println("📌 [fetchAmount] 요청 adtn_prd_sno = " + adtnPrdSno);

        int amount = termMapper.getAmountBySno(adtnPrdSno);
        System.out.println("💰 조회된 금액 = " + amount);

        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);

        return result;  // JSON 형태로 자동 반환
    }
    
} // class
