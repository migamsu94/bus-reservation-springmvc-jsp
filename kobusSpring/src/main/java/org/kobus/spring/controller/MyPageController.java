package org.kobus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class MyPageController {

	// 아이디찾기 페이지 get요청
	@GetMapping("/page/logonMyPage.do")
	public String logonMyPage() {
		log.info("> logonMyPage...GET()");
		return "kobus.mypage/logonMyPage";
	}

}
