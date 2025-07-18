package org.kobus.spring.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.kobus.spring.domain.reservation.ModifyResvDTO;
import org.kobus.spring.domain.reservation.ResvDTO;
import org.kobus.spring.domain.schedule.ScheduleDTO;
import org.kobus.spring.service.reservation.ResvService;
import org.kobus.spring.service.reservation.SeatService;
import org.kobus.spring.service.schedule.ScheduleService;
import org.quartz.utils.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ModifyReservationController {
	
	
	@Autowired
	SeatService seatService;
	
	@Autowired
	ResvService resvService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@GetMapping("/manageReservations.do")
	public String manageReservations(HttpSession session, Model model) {
		
//		String loginId = (String) session.getAttribute("id");
		
		String loginId = "user1";
		
//		if (session == null || session.getAttribute("id") == null) {
//	        // 로그인 안 된 상태
//			return "redirect:/koBus/koBusFile/logonMain.jsp";
//		}
		

		try {
			// 예매 내역 조회
			List<ResvDTO> resvList = resvService.searchResvList(loginId);
			
			List<ResvDTO> cancelList = resvService.searchCancelResvList(loginId);
			
			model.addAttribute("resvList", resvList);
			model.addAttribute("cancelList", cancelList);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "kobus.reservation/kobusManageResv";
	}
	
	
	@PostMapping(value = "/kobusResvCancel.ajax", produces = {
			  MediaType.APPLICATION_JSON_UTF8_VALUE
	})
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resvCancel(
			@RequestParam(required = false) String ajax,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String mrsMrnpno,
			@RequestParam(required = false) String mrsMrnpSno,
			@RequestParam(required = false) String prmmDcDvsCd,
			@RequestParam(required = false) String rtrpMrsYn,
			@RequestParam(required = false) String BRKP_AMT_CMM,
			@RequestParam(required = false) String pynDvsCd,
			@RequestParam(required = false) String pynDtlCd,
			@RequestParam(required = false) String tckSeqList,
			@RequestParam(required = false) String cancCnt,
			@RequestParam(required = false) String TRD_DTM,
			@RequestParam(required = false) String alcnDeprDt,
			@RequestParam(required = false) String alcnDeprTime,
			@RequestParam(required = false) String deprnNm,
			@RequestParam(required = false) String arvlNm,
			@RequestParam(required = false) String takeDrtm,
			@RequestParam(required = false) String cacmNm,
			@RequestParam(required = false) String deprNm,
			@RequestParam(required = false) String adltNum,
			@RequestParam(required = false) String chldNum,
			@RequestParam(required = false) String teenNum,
			@RequestParam(required = false) String seatNo) {
		
		Map<String, Object> recpListMap = new HashMap<>();

		int cancelResult = 0;
		int changeRemainSeats = 0;

		try {
			recpListMap.put("type", type);
			recpListMap.put("mrsMrnpNo", mrsMrnpno);
			recpListMap.put("mrsMrnpSno", mrsMrnpSno);
			recpListMap.put("prmmDcDvsCd", prmmDcDvsCd);
			recpListMap.put("rtrpMrsYn", rtrpMrsYn);
			recpListMap.put("BRKP_AMT_CMM", BRKP_AMT_CMM);
			recpListMap.put("pynDvsCd", pynDvsCd);
			recpListMap.put("pynDtlCd", pynDtlCd);
			recpListMap.put("tckSeqList", tckSeqList);
			recpListMap.put("cancCnt", cancCnt);
			recpListMap.put("TRD_DTM", TRD_DTM);
			recpListMap.put("alcnDeprDt", alcnDeprDt);
			recpListMap.put("alcnDeprTime", alcnDeprTime);
			recpListMap.put("deprnNm", deprnNm);
			recpListMap.put("arvlNm", arvlNm);
			recpListMap.put("takeDrtm", takeDrtm);
			recpListMap.put("cacmNm", cacmNm);
			recpListMap.put("deprNm", deprNm);
			recpListMap.put("adltNum", adltNum);
			recpListMap.put("chldNum", chldNum);
			recpListMap.put("teenNum", teenNum);
			recpListMap.put("setsList", seatNo);

			String rideTime = alcnDeprDt + alcnDeprTime;
			
			cancelResult = resvService.cancelResvList(mrsMrnpno);
			changeRemainSeats = resvService.changeRemainSeats(mrsMrnpno, rideTime);

			recpListMap.put("cancelResult", cancelResult);
			recpListMap.put("changeRemainSeats", changeRemainSeats);

			System.out.println("cancelResult : " + cancelResult);
			System.out.println("changeRemainSeats : " + changeRemainSeats);

		} catch (Exception e) {
			recpListMap.put("error", "오류 발생: " + e.getMessage());
		}

		
		if (recpListMap == null || recpListMap.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.ok(recpListMap); // 200 OK with JSON body
        }
	}
	
	
	@GetMapping("/modifyReservations.do")
	public String modifyReservations(Model model, @ModelAttribute ResvDTO resvDTO ) {
		
		List<ModifyResvDTO> resvInfoList = new ArrayList<ModifyResvDTO>();


		String deprDay = resvDTO.getRideDateStr();              // fn:substringBefore(resv.rideDateStr, ' ')
		String deprTime = resvDTO.getRideTimeStr();            // fn:substringAfter(resv.rideDateStr, ' ')

		int adultCnt = resvDTO.getAduCount();
		int stuCnt = resvDTO.getStuCount();
		int childCnt = resvDTO.getChdCount();
		
		// 날짜 + 시간 조합 문자열 → 포맷된 탑승일 문자열로 사용
		String rideDateStr = deprDay + " " + deprTime;
		
		
		

		// rideDateStr → LocalDateTime 변환 (필요 시 예외 처리 포함)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime rideDate = LocalDateTime.parse(rideDateStr, formatter);
		String formatted = rideDate.format(outputFormatter);
		
		resvDTO.setRideDateStr(formatted);

		int totalCount = adultCnt + stuCnt + childCnt;

		resvDTO.setTotalCount(totalCount);
	
		
		List<ScheduleDTO> changeList = new ArrayList<ScheduleDTO>();
		
		
		String deprDay2 = deprDay.replace("-", "");
		
		
		changeList = scheduleService.searchBusSchedule(deprRegCode, arrRegCode, deprDay2, "전체");	

		
		List<String> busTimeList = new ArrayList<>();

		for (ScheduleDTO time : changeList) {
		    LocalDateTime date = time.getDepartureDate();
		    String busTime = date.format(DateTimeFormatter.ofPattern("HH:mm"));
		    busTimeList.add(busTime);  // 리스트에 추가
		    System.out.println(busTime);
		}
		

		model.addAttribute("busTimeList", busTimeList);
		model.addAttribute("resvInfoList", resvInfoList);
		
		return "kobus.reservation/kobusModifyResv";
	}
	
	@GetMapping("/modifyResvSeat.do")
	public String modifyResvSeat(
			@RequestParam("deprCd") String deprId,
		    @RequestParam("arvlCd") String arrId,
		    @RequestParam("deprDate") String deprDate,
		    @RequestParam("deprTime") String deprTime,
		    @RequestParam("busClsCd") String busClsCd,
		    @RequestParam("deprNm") String deprNm,
		    @RequestParam("arvlNm") String arvlNm,
		    @RequestParam("sourcePage") String sourcePage,
		    @RequestParam("mrsMrnpNo") String resId,
		    @RequestParam("takeDrtm") String takeDrtm,
		    @RequestParam("comName") String comName,
		    @RequestParam("adltNum") String adltNum,
		    @RequestParam("chldNum") String chldNum,
		    @RequestParam("teenNum") String teenNum,
		    Model model) {
		
		
		LocalDate locDate = LocalDate.parse(deprDate); // yyyy-MM-dd
	    LocalTime locTime = LocalTime.parse(deprTime); // HH:mm
	    LocalDateTime loc = locDate.atTime(locTime);

	    DateTimeFormatter oracleFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
	    String deprDtm = loc.format(oracleFormatter);
	  
    	int durMinInt = Integer.parseInt(takeDrtm);
    	int aduCountInt = Integer.parseInt(adltNum);
    	int stuCountInt = Integer.parseInt(chldNum);
    	int chdCountInt = Integer.parseInt(teenNum);
    	
    	System.out.println("deprDtm " + deprDtm);
		
		if (deprDtm.matches("\\d{8} \\d{2}:\\d{2}:\\d{2}")) {
		    // 예: "20250628 08:00:00"
		    String date = deprDtm.substring(0, 8); // "20250628"
		    String time = deprDtm.substring(9, 14); // "08:00"

		    // "2025-06-28 08:00"
		    deprDtm = date.substring(0, 4) + "-" + 
		              date.substring(4, 6) + "-" + 
		              date.substring(6, 8) + " " + time;
		}
		
    	
    	// ResvDTO 객체 생성
    	ResvDTO changeSeat = ResvDTO.builder()
    			.resId(resId)
    	        .deprRegCode(deprId)
    	        .deprRegName(deprNm)
    	        .arrRegCode(arrId)
    	        .arrRegName(arvlNm)
    	        .comName(comName)
    	        .busGrade(busClsCd)
    	        .rideDateStr(deprDtm)
    	        .durMin(durMinInt)
    	        .aduCount(aduCountInt)
    	        .stuCount(stuCountInt)
    	        .chdCount(chdCountInt)
    	        .build();

    	// 리스트에 담아 request에 저장
    	List<ResvDTO> changeSeatList = new ArrayList<>();
    	changeSeatList.add(changeSeat);

    	model.addAttribute("changeSeatList", changeSeatList);
		
    	return "kobus.reservation/kobusModifyResvSeat";
		
	}

}
