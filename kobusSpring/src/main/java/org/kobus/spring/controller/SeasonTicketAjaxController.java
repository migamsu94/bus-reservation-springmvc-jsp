package org.kobus.spring.controller;

import java.security.Principal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kobus.spring.domain.pay.FreePassDTO;
import org.kobus.spring.mapper.pay.BusReservationMapper;
import org.kobus.spring.mapper.pay.FreePassMapper;
import org.kobus.spring.mapper.pay.SeasonTicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mrs/pay")
public class SeasonTicketAjaxController {

    @Autowired
    private SeasonTicketMapper seasonTicketMapper; // Mapper 주입
    
    @Autowired
    private FreePassMapper freePassMapper;
    
    @Autowired
    private BusReservationMapper reservationMapper;

    @PostMapping("/useSeasonTicket.do")
    @ResponseBody
    public Map<String, Object> checkUsage(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        String adtnPrdSno = request.getParameter("adtnPrdSno");
        String usedDate = request.getParameter("usedDate");

        if (adtnPrdSno == null || usedDate == null) {
            result.put("status", "error");
            result.put("message", "필수 파라미터 누락");
            return result;
        }

        int count = seasonTicketMapper.countUsagePerDay(adtnPrdSno, usedDate);

        result.put("status", "success");
        result.put("usageCount", count);
        return result;
    }
    
    @PostMapping("/useFreePass.do")
    @ResponseBody
    public Map<String, Object> useFreePass(HttpServletRequest request, Principal principal) {
        Map<String, Object> result = new HashMap<>();

        try {
            String adtnCpnNo = request.getParameter("adtnCpnNo"); // 프리패스 번호
            //String kusid = (String) request.getSession().getAttribute("kusid");
            String userId = principal.getName();
            String kusId = reservationMapper.findId(userId);
            String rideDate = request.getParameter("rideDate");
            System.out.println("rideDate: " + rideDate);
            LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
	        String formatted = now.format(formatter);
	        
	        

            if (kusId == null || adtnCpnNo == null || formatted == null) {
                result.put("result", "FAIL");
                result.put("message", "필수 정보 누락");
                return result;
            }

            // 1. 프리패스 유효 여부 확인
            FreePassDTO pass = freePassMapper.selectFreePassByCpnNo(adtnCpnNo);
            if (pass == null || !kusId.equals(pass.getKusid())) {
                result.put("result", "FAIL");
                result.put("message", "프리패스 정보가 유효하지 않습니다.");
                return result;
            }

            
         // 2. 날짜 범위 확인
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            // (1) rideDate에서 날짜만 추출 후 java.util.Date로 변환
            java.util.Date rideUtil = sdf.parse(rideDate.substring(0, 8));

            // (2) 프리패스 시작일
            java.util.Date start = pass.getStartDate();

            // (3) 프리패스 사용 가능 일수 조회
            Integer useDays = freePassMapper.getFreePassDays(adtnCpnNo); // 👈 여기!!

            // 유효성 체크
            if (useDays == null || useDays <= 0) {
                result.put("result", "FAIL");
                result.put("message", "프리패스 유효기간 정보가 잘못되었습니다.");
                return result;
            }

            // (4) 종료일 계산
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.DATE, useDays - 1); // 3일권이면 start + 2일 = end
            java.util.Date end = cal.getTime();

            // 로그 확인
            System.out.println("✅ rideUtil: " + sdf.format(rideUtil));
            System.out.println("✅ start: " + sdf.format(start));
            System.out.println("✅ end: " + sdf.format(end));
            System.out.println("✅ useDays: " + useDays);

            // (5) 날짜 범위 비교
            if (rideUtil.before(start) || rideUtil.after(end)) {
                result.put("result", "FAIL");
                result.put("message", "사용기간에 포함되지 않은 날짜입니다.");
                return result;
            }
            
            // 3. 예매 저장
            // (1) reservation insert
            freePassMapper.insertReservationForFreePass(pass, formatted, kusId);


            result.put("result", "SUCCESS");
        } catch (Exception e) {
        	e.printStackTrace();  // 콘솔에 전체 스택트레이스 출력
            result.put("result", "FAIL");
            result.put("message", "서버 오류: " + e.getMessage());
        }

        return result;
    }
}
