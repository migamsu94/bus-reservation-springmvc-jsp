package org.kobus.spring.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kobus.spring.service.member.MemberMyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class MyPageController {
	
	@Autowired
	private MemberMyPageService memberMyPageService;
	
	// 아이디찾기 페이지 post요청
	@GetMapping("/page/logonMyPage.do")
	public String logonMyPage(
			HttpServletRequest request,
			HttpServletResponse response
			) throws SQLException {
		log.info("> logonMyPage...POST()");
		String auth = (String) request.getSession().getAttribute("auth");
		log.info("가져온 아이디값 : " + auth);
		String telNum = memberMyPageService.getTelNum(auth);
		int resvCount = memberMyPageService.reservationCount(auth);
		int couponCount = memberMyPageService.popCouponCount(auth);
		couponCount += memberMyPageService.freeCouponCount(auth);
		
		request.setAttribute("tel", telNum);
		request.setAttribute("reservationCount", resvCount);
		request.setAttribute("couponCount", couponCount);
		
		return "kobus.mypage/logonMyPage";
	}

}
