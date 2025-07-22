package org.kobus.spring.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kobus.spring.domain.schedule.ScheduleDTO;
import org.kobus.spring.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	@PostMapping("/readRotLinInf.ajax")
	@ResponseBody
	public ResponseEntity<?> readRotLinInf(
			@RequestParam(name = "regionCode", required = false) String regionCode,
			@RequestParam(name = "ajaxType") String ajaxType) throws SQLException {

		log.info(">> readRotLinInf.ajax 호출됨 - ajaxType : " + ajaxType);

		if ("getStationName".equals(ajaxType)) {
			if (regionCode == null || regionCode.trim().isEmpty()
					|| "null".equalsIgnoreCase(regionCode)
					|| "undefined".equalsIgnoreCase(regionCode)) {
				regionCode = "11";
			}

			if ("all".equals(regionCode)) {
				List<ScheduleDTO> result = scheduleService.selectByRegion();
				return ResponseEntity.ok(result);
			} else {
				int sidoCode = Integer.parseInt(regionCode);
				List<ScheduleDTO> result = scheduleService.selectBySidoCode(sidoCode);
				return ResponseEntity.ok(result);
			}

		}
		return null;
	}


	@PostMapping("/searchSchedule/readAlcnSrch.ajax")
	@ResponseBody
	public ResponseEntity<?> readAlcnSrch(
			@RequestParam(name = "ajaxType") String ajaxType,
			@RequestParam(name = "deprCd", required = false) String deprCd,
			@RequestParam(name = "arvlCd", required = false) String arvlCd,
			@RequestParam(name = "deprDtm", required = false) String deprDtm,
			@RequestParam(name = "busClsCd", required = false) String busClsCd) throws SQLException {

		log.info(">> readRotLinInf.ajax 호출됨 - ajaxType : " + ajaxType);

		if ("searchSch".equals(ajaxType)) {
			log.info(">> searchSch 실행");

			if (busClsCd != null) {
				switch (busClsCd) {
				case "0": busClsCd = "전체"; break;
				case "7": busClsCd = "프리미엄"; break;
				case "1": busClsCd = "우등"; break;
				case "2": busClsCd = "일반"; break;
				}
			}

			List<ScheduleDTO> schList = scheduleService.searchBusSchedule(deprCd, arvlCd, deprDtm, busClsCd);

			int durmin = schList.isEmpty() ? 0 : schList.get(0).getDurMin();

			Map<String, Object> responseMap = new HashMap<>();
			// 편도 / 왕복 여부
			responseMap.put("rotVldChc", schList.isEmpty() ? "N" : "Y");

			Map<String, Object> alcnCmnMap = new HashMap<>();
			alcnCmnMap.put("takeDrtm", durmin);
			responseMap.put("alcnCmnMap", alcnCmnMap);

			List<Map<String, Object>> alcnAllList = new ArrayList<>();
			for (ScheduleDTO dto : schList) {
				Map<String, Object> scheduleMap = new HashMap<>();
				LocalDateTime departureDate = dto.getDepartureDate();
				String time = departureDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

				scheduleMap.put("DEPR_TIME_DVS", time);
				scheduleMap.put("CACM_MN", dto.getComName());
				scheduleMap.put("BUS_CLS_NM", dto.getBusGrade());
				scheduleMap.put("ADLT_FEE", dto.getAdultFare());
				scheduleMap.put("CHLD_FEE", dto.getStuFare());
				scheduleMap.put("TEEN_FEE", dto.getChildFare());
				scheduleMap.put("RMN_SATS_NUM", dto.getRemainSeats());
				scheduleMap.put("TOT_SATS_NUM", dto.getBusSeats());

				alcnAllList.add(scheduleMap);
			}
			responseMap.put("alcnAllList", alcnAllList);

			return ResponseEntity.ok(responseMap);
		}
		return null;
	}

	@GetMapping("/getDuration.ajax")
	@ResponseBody
	public ResponseEntity<?> getDurationAjax(
	    @RequestParam(name = "ajaxType") String ajaxType,
	    @RequestParam(name = "deprCd", required = false) String deprCd,
	    @RequestParam(name = "arvlCd", required = false) String arvlCd,
	    @RequestParam(name = "deprDtm", required = false) String deprDtm,
	    @RequestParam(name = "busClsCd", required = false) String busClsCd
	) throws SQLException {

	    log.info(">> getDuration.ajax 호출됨 - ajaxType : " + ajaxType);

	    if ("getDuration".equals(ajaxType)) {
	        Integer duration = scheduleService.getRouteDuration(deprCd, arvlCd);
	        Map<String, Object> response = new HashMap<>();
	        response.put("duration", duration);
	        return ResponseEntity.ok(response);
	    }

	    if ("searchSch".equals(ajaxType)) {
	        if (busClsCd != null) {
	            switch (busClsCd) {
	                case "0": busClsCd = "전체"; break;
	                case "7": busClsCd = "프리미엄"; break;
	                case "1": busClsCd = "우등"; break;
	                case "2": busClsCd = "일반"; break;
	            }
	        }

	        List<ScheduleDTO> schList = scheduleService.searchBusSchedule(deprCd, arvlCd, deprDtm, busClsCd);
	        int durmin = schList.isEmpty() ? 0 : schList.get(0).getDurMin();

	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("rotVldChc", schList.isEmpty() ? "N" : "Y");

	        Map<String, Object> alcnCmnMap = new HashMap<>();
	        alcnCmnMap.put("takeDrtm", durmin);
	        responseMap.put("alcnCmnMap", alcnCmnMap);

	        List<Map<String, Object>> alcnAllList = new ArrayList<>();
	        for (ScheduleDTO dto : schList) {
	            Map<String, Object> scheduleMap = new HashMap<>();
	            LocalDateTime departureDate = dto.getDepartureDate();
	            String time = departureDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

	            scheduleMap.put("DEPR_TIME_DVS", time);
	            scheduleMap.put("CACM_MN", dto.getComName());
	            scheduleMap.put("BUS_CLS_NM", dto.getBusGrade());
	            scheduleMap.put("ADLT_FEE", dto.getAdultFare());
	            scheduleMap.put("CHLD_FEE", dto.getStuFare());
	            scheduleMap.put("TEEN_FEE", dto.getChildFare());
	            scheduleMap.put("RMN_SATS_NUM", dto.getRemainSeats());
	            scheduleMap.put("TOT_SATS_NUM", dto.getBusSeats());

	            alcnAllList.add(scheduleMap);
	        }

	        responseMap.put("alcnAllList", alcnAllList);
	        return ResponseEntity.ok(responseMap);
	    }

	    return ResponseEntity.badRequest().body("지원되지 않는 ajaxType");
	}

	
	@GetMapping("/kobusSchedule.do")
	public String getSchedule() {
		return "kobus.reservation/kobusSchedule";

	}
	
	
	@PostMapping("/reservation2.do")
	public String showReservationPagePost() {
	    return "kobus.reservation/KOBUSreservationTime";
	}
	
	
	@GetMapping("/reservation2.do")
	public String showReservationPage() {
	    return "kobus.reservation/KOBUSreservationTime"; 
	}
	
	
}
