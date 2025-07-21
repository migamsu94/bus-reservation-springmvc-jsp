package org.kobus.spring.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.kobus.spring.domain.reservation.SeatDTO;
import org.kobus.spring.domain.schedule.ScheduleDTO;
import org.kobus.spring.service.reservation.SeatService;
import org.kobus.spring.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ReservationController {
	
	@Autowired 
	ScheduleService scheduleService;
	
	@Autowired
	SeatService seatService;
	
	@GetMapping("/kobusSeat.do")
	public String kobusSeat(
		@RequestParam(value = "deprCd", required = false) String deprId,
	    @RequestParam(value = "arvlCd", required = false) String arrId,
	    @RequestParam(value = "deprDate", required = false) String deprDate,
	    @RequestParam(value = "deprTime", required = false) String deprTime,
	    @RequestParam(value = "busClsCd", required = false) String busClsCd,
	    @RequestParam(value = "deprNm", required = false) String deprNm,
	    @RequestParam(value = "arvlNm", required = false) String arvlNm,
	    Model model) {

	    System.out.println("> SeatHandler.process() ...");
	    
	    deprId = "REG018";
	    arrId = "REG003";
	    deprDate = "20250719";
	    deprTime = "08:30";
	    busClsCd = "프리미엄";
	    deprNm = "화성";
	    arvlNm = "센트럴시티(서울)";

	    String deprDtm = deprDate + " " + deprTime;
	    
	    switch (busClsCd) {
		    case "0": busClsCd = "전체"; break;
		    case "7": busClsCd = "프리미엄"; break;
		    case "1": busClsCd = "우등"; break;
		    case "2": busClsCd = "일반"; break;
		    default: break;
	    }
	    
	    List<ScheduleDTO> busList = new ArrayList<ScheduleDTO>();
	    List<SeatDTO> seatList = new ArrayList<SeatDTO>();
	    
	    try {
	    	// 탑승하는 버스 스케줄 정보 가져오기
	    	busList = scheduleService.searchBusSchedule(deprId, arrId, deprDtm, busClsCd);

	    	// 출발지 / 도착지 / 출발시간 / 버스등급을 기준으로 사용하는 busId 가져오기
	    	String busId = seatService.getBusId(deprId, arrId, deprDtm);

	    	// 탑승하는 버스 전체 좌석 가져오기
	    	int totalSeat = seatService.getTotalSeats(busId);

	    	// 탑승하는 버스 좌석 정보 가져오기
	    	seatList = seatService.searchSeat(busId);


	    	deprDtm = deprDtm.substring(0, 4) + "-" + 
	    			deprDtm.substring(4, 6) + "-" + 
	    			deprDtm.substring(6, 8) + " " + deprDtm.substring(9, 14);
	    	
	    	System.out.println("deprDtm " + deprDtm);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    model.addAttribute("deprId", deprId);
	    model.addAttribute("arrId", arrId);
	    model.addAttribute("deprDtm", deprDtm);
	    model.addAttribute("deptTime", deprTime);
	    model.addAttribute("busClsCd", busClsCd);
	    model.addAttribute("deprDate", deprDate);
	    model.addAttribute("deprTime", deprTime);
	    model.addAttribute("deprNm", deprNm);
	    model.addAttribute("arvlNm", arvlNm);
	    model.addAttribute("busList", busList);
	    model.addAttribute("seatList", seatList);
	    
	    return "kobus.reservation/kobus_seat"; 
	   
		
	}
	
	
	
	

}
