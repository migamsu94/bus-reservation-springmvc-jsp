package org.kobus.spring.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kobus.spring.domain.pay.FreepassPaymentDTO;
import org.kobus.spring.domain.pay.PaymentCommonDTO;
import org.kobus.spring.mapper.pay.TermMapper;
import org.kobus.spring.service.pay.FreePassPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
    private TermMapper termMapper;
	
	@Autowired
    private FreePassPaymentService freepassService;
	
	// 일반 예매 결제
    @PostMapping("/Reservation.do")
    public Map<String, Object> handleReservation(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // TODO: 파라미터 추출 및 처리
        // ex) String impUid = request.getParameter("imp_uid");

        // TODO: 서비스 호출 및 DB 저장

        // 응답 결과 구성
        result.put("status", "success");
        result.put("message", "Reservation payment processed.");
        return result;
    }

    // 정기권 결제
    @PostMapping("/Seasonticket.do")
    public Map<String, Object> handleSeasonticket(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // TODO: 파라미터 추출 및 처리

        // TODO: 서비스 호출 및 DB 저장

        result.put("status", "success");
        result.put("message", "Season ticket payment processed.");
        return result;
    }

    // 프리패스 결제
    @PostMapping("/Freepass.do")
    public Map<String, Object> handleFreepass(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 세션에서 userId 확인
            String userId = (String) request.getSession().getAttribute("userId");
            if (userId == null) userId = "KUS002"; // 테스트용

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
