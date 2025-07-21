package org.kobus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class FreePassPageController {

    @GetMapping("/buspay.htm")
    public String showBusPayForm() {
    	System.out.println(">>>>>>   http://localhost/payment/buspay.htm 요청." );
        return "kobus.payment/buspay";  // Tiles 와일드카드 규칙에 맞는 이름
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
    
} // class

