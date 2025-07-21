package org.kobus.spring.controller;

import java.sql.SQLException;

import org.kobus.spring.domain.join.JoinDTO;
import org.kobus.spring.service.join.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class JoinController {
	
	private JoinService joinService;
	
	@PostMapping("/joinOkEnterInfo.do")
	public String joinOkEnterInfo(@ModelAttribute JoinDTO dto) throws SQLException {
		
		log.info("> joinOkEnterInfo.POST()...");
		
		int result = joinService.insert(dto);
		
		if(result == 1) {
			log.info("회원가입 성공!");
			
		}else {
			log.info("회원가입 실패!");
			
		}
		
		return "redirect:main.do";
		
	}

	// 회원가입 main페이지 get방식
	@GetMapping("/page/joinMain.do")
	public String joinMainPage() {
		return "kobus.join/joinMain";
	}

	// 회원가입 인증번호받는 페이지 get방식
	@GetMapping("/page/joinVerification.do")
	public String joinVerificationPage() {
		return "kobus.join/joinVerification";
	}

	// 회원가입 정보입력페이지 get방식
	@GetMapping("/page/joinEnterInfo.do")
	public String joinEnterInfoPage() {
		return "kobus.join/joinEnterInfo";
	}

}
