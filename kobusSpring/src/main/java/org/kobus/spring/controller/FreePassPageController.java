package org.kobus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class FreePassPageController {

    @GetMapping("/freepass.htm")
    public String showFreePassForm() {
    	System.out.println(">>>>>>   http://localhost/payment/freepass.htm 요청." );
        return "kobus.payment/freePass";  // Tiles 와일드카드 규칙에 맞는 이름
        
        // return "payment/freePass";
        //  http://localhost/payment/freepass.htm 요청
    }
}

