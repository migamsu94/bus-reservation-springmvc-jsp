package org.kobus.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MyPageController {

	// 아이디찾기 페이지 get요청
	@GetMapping("/page/logonMyPage.do")
	public String logonMyPage() {
		return "kobus.mypage/logonMyPage";
	}

}
