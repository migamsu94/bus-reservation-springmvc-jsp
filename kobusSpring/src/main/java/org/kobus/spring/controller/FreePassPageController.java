package org.kobus.spring.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.kobus.spring.domain.pay.RequestPayDTO;
import org.kobus.spring.mapper.pay.BusReservationMapper;
import org.kobus.spring.mapper.reservation.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class FreePassPageController {
	
	@Autowired
    private SeatMapper seatMapper; // SeatMapper.xml
	
	@Autowired
    private BusReservationMapper reservationMapper;
	
	/*
	@Autowired
    private LogonMapper logonMapper; // LogonMapper.xml
	*/
    @GetMapping("/buspay.htm")
    public String showBusPayForm() {
    	System.out.println(">>>>>>   http://localhost/payment/buspay.htm 요청." );
        return "kobus.payment/buspay";  // Tiles 와일드카드 규칙에 맞는 이름
    }
    
    // 2. 좌석 선택 후 결제정보 POST 전송될 때
    @PostMapping("/buspay.htm")
    public String handleBusPayPost(HttpServletRequest request, Model model) {
    	System.out.println(">>>>>> POST  http://localhost/payment/buspay.htm 요청." );
    	
    	 String deprCd = request.getParameter("deprCd");
         String deprDtRaw = request.getParameter("deprDtm");
         String deprTime = request.getParameter("deprTime");
         String deprNm = request.getParameter("deprNm");
         
         String arvlCd = request.getParameter("arvlCd");
         String arvlNm = request.getParameter("arvlNm");
         String arvlDtRaw = request.getParameter("arvlDtm");
         
         String takeDrtmOrg = request.getParameter("takeDrtmOrg");
         
         String pathDvs = request.getParameter("pathDvs");
         
         String cacmCd = "";
         String cacmNm = "";
         String indVBusClsCd = "";

         String selAdltCnt = "";
         String selChldCnt = "";
         String selTeenCnt = "";

         String selectedSeatIds = request.getParameter("selectedSeatIds");
         String selSeatNum = "";
         String selSeatCnt = "";
         String allTotAmtPrice = "";
         String busCode = "";
         String changeResId = "";

         // 왕복 관련 변수
         String selectedSeatIds1 = "", selSeatNum1 = "", selSeatCnt1 = "";
         String selAdltCnt1 = "", selChldCnt1 = "", selTeenCnt1 = "";
         String allTotAmtPrice1 = "", indVBusClsCd1 = "", cacmCd1 = "", cacmNm1 = "", busCode1 = "";

         String selectedSeatIds2 = "", selSeatNum2 = "", selSeatCnt2 = "";
         String selAdltCnt2 = "", selChldCnt2 = "", selTeenCnt2 = "";
         String allTotAmtPrice2 = "", indVBusClsCd2 = "", cacmCd2 = "", cacmNm2 = "", busCode2 = "";
         
         String seatNos = "";
         String deprSeatNos = "";
         String arvlSeatNos = "";
         
         int allFullTotal = 0;
         
         String deprDtFmt = deprDtRaw.substring(0, 10).replace("-", ".");
         
         String arvlDtFmt = "";

         String deprTimeFmt = deprTime;
         String arvlTimeFmt = "";

         String fullDateTime = deprDtRaw + ":00";
         String arvlfullDateTime = "";

         if ("sngl".equals(pathDvs)) {
             // 편도
             cacmCd = request.getParameter("cacmCd");
             cacmNm = request.getParameter("cacmNm");
             indVBusClsCd = request.getParameter("indVBusClsCd");

             selAdltCnt = request.getParameter("selAdltCnt");
             selChldCnt = request.getParameter("selChldCnt");
             selTeenCnt = request.getParameter("selTeenCnt");

             selectedSeatIds = request.getParameter("selectedSeatIds");
             selSeatNum = request.getParameter("selSeatNum");
             selSeatCnt = request.getParameter("selSeatCnt");
             allTotAmtPrice = request.getParameter("allTotAmtPrice");
             busCode = request.getParameter("bshId");
             changeResId = request.getParameter("changeResId");
             
             seatNos = searchSeatNos(selectedSeatIds);
             
             selectedSeatIds = searchSeatList(selectedSeatIds);
             
            System.out.println("selectedSeatIds " + selectedSeatIds);

         } else {
             // 왕복
             String rtrpDtl1 = request.getParameter("rtrpDtl1");
             String rtrpDtl2 = request.getParameter("rtrpDtl2");

             String[] rtrpDtl1Info = rtrpDtl1.split(":");
             String[] rtrpDtl2Info = rtrpDtl2.split(":");
             
             arvlDtFmt = arvlDtRaw.substring(0, 10).replace("-", ".");
             arvlTimeFmt = arvlDtRaw.substring(11);
             arvlfullDateTime = arvlDtRaw + ":00";

             System.out.println("rtrpDtl1Info " + Arrays.toString(rtrpDtl1Info));
             System.out.println("rtrpDtl2Info " + Arrays.toString(rtrpDtl2Info));
//             rtrpDtl1Info [seatNum_SEAT022, 0, 1, 0, 0, 0, 0, 0, 0, Y, 1, , , 15123, 2025. 7. 27. 일, 06, 00, 프리미엄, 02, 동부고속, BSH00001, undefined, a, U, 0, 0, 0,]
//            rtrpDtl2Info [seatNum_SEAT089, 0, 1, 0, 0, 0, 0, 0, 0, Y, 1, , , 15738, 2025. 7. 27. 일, 18, 00, 일반, 02, 중앙고속, BSH02526, a, U, 0, 0, 0]
             
             // 가는편
             selectedSeatIds1 = rtrpDtl1Info[0];
             selSeatNum1 = rtrpDtl1Info[0];
             selSeatCnt1 = rtrpDtl1Info[1];
             selAdltCnt1 = rtrpDtl1Info[2];
             selTeenCnt1 = rtrpDtl1Info[3];
             selChldCnt1 = rtrpDtl1Info[4];
             allTotAmtPrice1 = rtrpDtl1Info[13];
             indVBusClsCd1 = rtrpDtl1Info[17];
             cacmCd1 = rtrpDtl1Info[18];
             cacmNm1 = rtrpDtl1Info[19];
             busCode1 = rtrpDtl1Info[20];
             
             deprSeatNos = searchSeatNos(selectedSeatIds1);
             
             selectedSeatIds1 = searchSeatList(selectedSeatIds1);

             // 오는편
             selectedSeatIds2 = rtrpDtl2Info[0];
             selSeatNum2 = rtrpDtl2Info[0];
             selSeatCnt2 = rtrpDtl2Info[1];
             selAdltCnt2 = rtrpDtl2Info[2];
             selTeenCnt2 = rtrpDtl2Info[3];
             selChldCnt2 = rtrpDtl2Info[4];
             allTotAmtPrice2 = rtrpDtl2Info[13];
             indVBusClsCd2 = rtrpDtl2Info[17];
             cacmCd2 = rtrpDtl2Info[18];
             cacmNm2 = rtrpDtl2Info[19];
             busCode2 = rtrpDtl2Info[20];
             
             arvlSeatNos = searchSeatNos(selectedSeatIds2);
             
             selectedSeatIds2 = searchSeatList(selectedSeatIds2);
             
             allFullTotal = Integer.parseInt(allTotAmtPrice1) + Integer.parseInt(allTotAmtPrice2);
             
             System.out.println("selectedSeatIds1 " + selectedSeatIds1);
             System.out.println("selectedSeatIds2 " + selectedSeatIds2);
         }
        
        
        System.out.println("deprDtFmt " + deprDtFmt);
        System.out.println("deprTimeFmt " + deprTimeFmt);
        System.out.println("busCode1 " + busCode1);
        System.out.println("busCode2 " + busCode2);
//        System.out.println("changeResId " + changeResId);
//        System.out.println("busCode " + busCode);
        

		String resId = reservationMapper.generateResId(); // ReservationMapper 사용
        /* generateResId -> SELECT SEQ_RESERVATION.NEXTVAL FROM DUAL */
		System.out.println("generateResId " + resId);
         
         
        model.addAttribute("resId", resId);
        model.addAttribute("changeResId", changeResId);
        model.addAttribute("deprCd", deprCd);
        model.addAttribute("arvlCd", arvlCd);
        model.addAttribute("deprDt", deprDtRaw);
        model.addAttribute("arvlDt", arvlDtRaw);
        model.addAttribute("deprTime", deprTime);
        model.addAttribute("arvlTime", arvlTimeFmt);
        model.addAttribute("deprDtFmt", deprDtFmt);
        model.addAttribute("arvlDtFmt", arvlDtFmt);
        model.addAttribute("takeDrtmOrg", takeDrtmOrg);
        model.addAttribute("pathDvs", pathDvs);
        model.addAttribute("deprNm", deprNm);
        model.addAttribute("arvlNm", arvlNm);
        
       
        if ("sngl".equals(pathDvs)) {
            model.addAttribute("selAdltCnt", selAdltCnt);
            model.addAttribute("selChldCnt", selChldCnt);
            model.addAttribute("selTeenCnt", selTeenCnt);
            model.addAttribute("selectedSeatIds", selectedSeatIds);
            model.addAttribute("selSeatNum", selSeatNum);
            model.addAttribute("selSeatCnt", selSeatCnt);
            model.addAttribute("allTotAmtPrice", allTotAmtPrice);
            model.addAttribute("busCode", busCode);
            model.addAttribute("cacmCd", cacmCd);
            model.addAttribute("cacmNm", cacmNm);
            model.addAttribute("indVBusClsCd", indVBusClsCd);
            model.addAttribute("seatNos", seatNos);
            model.addAttribute("estmAmt", allTotAmtPrice);
            model.addAttribute("tissuAmt", allTotAmtPrice);
            
            
        } else if ("rtrp".equals(pathDvs)) {
            // 가는편
            model.addAttribute("selAdltCnt", selAdltCnt1);
            model.addAttribute("selChldCnt", selChldCnt1);
            model.addAttribute("selTeenCnt", selTeenCnt1);
            model.addAttribute("selectedSeatIds", selectedSeatIds1);
            model.addAttribute("selSeatNum", selSeatNum1);
            model.addAttribute("selSeatCnt", selSeatCnt1);
            model.addAttribute("allTotAmtPrice1", allTotAmtPrice1);
            model.addAttribute("busCode", busCode1);
            model.addAttribute("cacmCd", cacmCd1);
            model.addAttribute("cacmNm", cacmNm1);
            model.addAttribute("indVBusClsCd", indVBusClsCd1);
            model.addAttribute("seatNos", deprSeatNos);
            

            // 오는편
            model.addAttribute("selAdltCnt2", selAdltCnt2);
            model.addAttribute("selChldCnt2", selChldCnt2);
            model.addAttribute("selTeenCnt2", selTeenCnt2);
            model.addAttribute("selectedSeatIds2", selectedSeatIds2);
            model.addAttribute("selSeatNum2", selSeatNum2);
            model.addAttribute("selSeatCnt2", selSeatCnt2);
            model.addAttribute("allTotAmtPrice2", allTotAmtPrice2);
            model.addAttribute("busCode2", busCode2);
            model.addAttribute("cacmCd2", cacmCd2);
            model.addAttribute("cacmNm2", cacmNm2);
            model.addAttribute("indVBusClsCd2", indVBusClsCd2);
            model.addAttribute("arvlSeatNos", arvlSeatNos);
            model.addAttribute("estmAmt", allFullTotal);
            model.addAttribute("tissuAmt", allFullTotal);

            
        }

        return "kobus.payment/buspay"; // 타일즈 뷰 이름
    }

    
    private String searchSeatNos(String selectedSeatIds) {
    	// 좌석 ID 파싱
        Pattern pattern = Pattern.compile("SEAT\\d+");
        Matcher matcher = pattern.matcher(selectedSeatIds);
        List<String> seatIdList = new ArrayList<>();
        while (matcher.find()) {
            seatIdList.add(matcher.group());
        }
        String seatNos = "";
		try {
			seatNos = seatMapper.searchSeatId(seatIdList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // SeatMapper 사용
		
		return seatNos;
	}
    
    private String searchSeatList(String selectedSeatIds) {
    	// 좌석 ID 파싱
        Pattern pattern = Pattern.compile("SEAT\\d+");
        Matcher matcher = pattern.matcher(selectedSeatIds);
        List<String> seatIdList = new ArrayList<>();
        while (matcher.find()) {
            seatIdList.add(matcher.group());
        }
        return String.join(",", seatIdList);
	}

	@GetMapping("/freepass.htm")
    public String showFreePassForm() {
    	System.out.println(">>>>>>   http://localhost/payment/freepass.htm 요청." );
        return "kobus.payment/freepass";  // Tiles 와일드카드 규칙에 맞는 이름
        
        // return "payment/freePass";
        //  http://localhost/payment/freepass.htm 요청
    }
    
    @GetMapping("/seasonticket.htm")
    public String showSeasonTicketForm() {
    	System.out.println(">>>>>>   http://localhost/payment/seasonticket.htm 요청." );
        return "kobus.payment/seasonticket";  // Tiles 와일드카드 규칙에 맞는 이름
    }
    /*
    @GetMapping("/reservCompl.htm")
    public String showReservComplForm() {
    	System.out.println(">>>>>>   http://localhost/payment/reservCompl.htm 요청." );
        return "kobus.payment/reservCompl";  // Tiles 와일드카드 규칙에 맞는 이름
    }
    */
    @GetMapping("/reservCompl.htm")
    public String showReservationComplete(HttpServletRequest request, Model model) {
    	
    	String resId = null;
    	String changeResId = null;

    	String deprDtRaw = null;
    	String deprTime = null;
    	String arvlDtRaw = null;

    	String deprNm = null;
    	String arvlNm = null;
    	String takeDrtmOrg = null;

    	String cacmNm = null;
    	String indVBusClsCd = null;
    	String selSeatCnt = null;
    	String seatNos = null;

    	String selAdltCnt = null;
    	String selTeenCnt = null;
    	String selChldCnt = null;
    	
    	// 왕복 사용 필드
    	String cacmNm2 = null;
    	String indVBusClsCd2 = null;
    	String selSeatCnt2 = null;
    	String arvlSeatNos = null;
    	
    	String selAdltCnt2 = null;
    	String selTeenCnt2 = null;
    	String selChldCnt2 = null;

    	String payMethod = null;
    	String amountStr = null;
    	
    	String pathDvs = null;

    	RequestPayDTO paymentData = (RequestPayDTO) request.getSession().getAttribute("paymentData");
    	
    	if (paymentData != null) {
    	    // 세션에 paymentData가 존재할 때 처리
    	    resId = paymentData.getResId();
    	    changeResId = paymentData.getChangeResId();
    	    
    	    deprDtRaw = paymentData.getDeprDt();
    	    deprTime = paymentData.getDeprTime();
    	    arvlDtRaw = paymentData.getArvlDt();

    	    deprNm = paymentData.getDeprNm();
    	    arvlNm = paymentData.getArvlNm();
    	    takeDrtmOrg = paymentData.getTakeDrtmOrg();

    	    cacmNm = paymentData.getCacmNm();
    	    indVBusClsCd = paymentData.getIndVBusClsCd();
    	    selSeatCnt = paymentData.getSelSeatCnt();
    	    seatNos = paymentData.getSeatNos();
    	    
    	    selAdltCnt = paymentData.getSelAdltCnt();
    	    selTeenCnt = paymentData.getSelTeenCnt();
    	    selChldCnt = paymentData.getSelChldCnt();
    	    
    	    cacmNm2 = paymentData.getCacmNm2();
    	    indVBusClsCd2 = paymentData.getIndVBusClsCd2();
    	    selSeatCnt2 = paymentData.getSelSeatCnt2();
    	    arvlSeatNos = paymentData.getArvlSeatNos();

    	    selAdltCnt2 = paymentData.getSelAdltCnt2();
    	    selTeenCnt2 = paymentData.getSelTeenCnt2();
    	    selChldCnt2 = paymentData.getSelChldCnt2();

    	    payMethod = paymentData.getPay_method();
    	    amountStr = paymentData.getAmount();
    	    pathDvs = paymentData.getPathDvs();
    	    
    	} else {
    	    // 세션에 paymentData가 없을 때 request 파라미터로 초기화
    	    resId = request.getParameter("resId");
    	    changeResId = request.getParameter("changeResId");

    	    deprDtRaw = request.getParameter("deprDt");
    	    deprTime = request.getParameter("deprTime");

    	    deprNm = request.getParameter("deprNm");
    	    arvlNm = request.getParameter("arvlNm");
    	    takeDrtmOrg = request.getParameter("takeDrtmOrg");

    	    cacmNm = request.getParameter("cacmNm");
    	    indVBusClsCd = request.getParameter("indVBusClsCd");
    	    selSeatCnt = request.getParameter("selSeatCnt");
    	    seatNos = request.getParameter("seatNos");

    	    selAdltCnt = request.getParameter("selAdltCnt");
    	    selTeenCnt= request.getParameter("selTeenCnt");
    	    selChldCnt = request.getParameter("selChldCnt");

    	    payMethod = request.getParameter("payMethod");
    	    amountStr = request.getParameter("amount");
    	}

        

        // 3. 소요시간 변환
        String durationStr;
        try {
            int durationMin = Integer.parseInt(takeDrtmOrg);
            int hours = durationMin / 60;
            int minutes = durationMin % 60;
            durationStr = (hours > 0 ? hours + "시간 " : "") + minutes + "분";
        } catch (Exception e) {
            durationStr = "소요시간 오류";
        }

        // 4. 결제일시
        String paidAtStr = new SimpleDateFormat("yyyy.MM.dd (E) HH:mm", Locale.KOREA).format(new java.util.Date());

        String buyerSummary = createBuyerSummary(selAdltCnt, selTeenCnt, selChldCnt);
        String buyerSummary2 = null;
        
        if (pathDvs != null) {
        	buyerSummary2 = createBuyerSummary(selAdltCnt2, selTeenCnt2, selChldCnt2);
		}
        

        // 6. request에 저장
         model.addAttribute("resId", resId);
         model.addAttribute("changeResId", changeResId);
         model.addAttribute("deprNm", deprNm);
         model.addAttribute("arvlNm", arvlNm);
         model.addAttribute("deprDtTimeFmt", deprDtRaw);
         model.addAttribute("arvlDtTimeFmt", arvlDtRaw);
         model.addAttribute("takeDrtmOrg", takeDrtmOrg);
         model.addAttribute("durationStr", durationStr);

         model.addAttribute("cacmNm", cacmNm);
         model.addAttribute("indVBusClsCd", indVBusClsCd);
         model.addAttribute("selSeatCnt", selSeatCnt);
         model.addAttribute("seatNos", seatNos);
         model.addAttribute("payMethod", payMethod);
        
         model.addAttribute("cacmNm2", cacmNm2);
         model.addAttribute("indVBusClsCd2", indVBusClsCd2);
         model.addAttribute("selSeatCnt2", selSeatCnt2);
         model.addAttribute("arvlSeatNos", arvlSeatNos);
        
         model.addAttribute("amount", amountStr);
        
         model.addAttribute("paidAtStr", paidAtStr);
         model.addAttribute("buyerSummary", buyerSummary.trim());
         model.addAttribute("buyerSummary2", (buyerSummary2 != null) ? buyerSummary2.trim() : "");
        
        model.addAttribute("pathDvs", pathDvs);

        // Tiles 설정에 따라 뷰 이름 반환
        return "kobus.payment/reservCompl";
    }
    
    // 탑승객 요약 생성 메소드
    private String createBuyerSummary(String selAdltCnt, String selTeenCnt, String selChldCnt) {
        int adlt = safeParseInt(selAdltCnt);
        int teen = safeParseInt(selTeenCnt);
        int chld = safeParseInt(selChldCnt);

        return (adlt > 0 ? "일반 " + adlt + "명 " : "") +
               (teen > 0 ? "청소년 " + teen + "명 " : "") +
               (chld > 0 ? "어린이 " + chld + "명" : "").trim();
    }

    private int safeParseInt(String str) {
        if (str == null || str.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            // 필요하면 로그 기록
            return 0;
        }
    }
    
} // class

