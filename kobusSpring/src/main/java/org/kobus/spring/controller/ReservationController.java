package org.kobus.spring.controller;


import java.sql.SQLException;
import java.util.List;

import org.kobus.spring.domain.schedule.ScheduleDTO;
import org.kobus.spring.service.reservation.SeatService;
import org.kobus.spring.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	    @RequestParam("deprCd") String deprId,
	    @RequestParam("arvlCd") String arrId,
	    @RequestParam("deprDate") String deprDate,
	    @RequestParam("deprTime") String deprTime,
	    @RequestParam("busClsCd") String busClsCd,
	    @RequestParam("deprNm") String deprNm,
	    @RequestParam("arvlNm") String arvlNm,
	    @RequestParam("sourcePage") String sourcePage,
	    Model model) {

	    System.out.println("> SeatHandler.process() ...");

	    String deprDtm = deprDate + " " + deprTime;
	    
	    switch (busClsCd) {
		    case "0": busClsCd = "전체"; break;
		    case "7": busClsCd = "프리미엄"; break;
		    case "1": busClsCd = "우등"; break;
		    case "2": busClsCd = "일반"; break;
		    default: break;
	    }
	    
	    List<ScheduleDTO> busList = null;
	    
	    try {
			// 탑승하는 버스 스케줄 정보 가져오기
			busList = scheduleService.searchBusSchedule(deprId, arrId, deprDtm, busClsCd);
			
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
	    
	    return "kobus.payment/kobus_pay"; 
	   
		
	}
	
	
	
	

}
