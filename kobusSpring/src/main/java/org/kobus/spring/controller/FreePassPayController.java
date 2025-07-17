package org.kobus.spring.controller;

import org.kobus.spring.domain.FreePassPaymentDTO;
import org.kobus.spring.service.pay.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/freepass")
public class FreePassPayController {
	@Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public String handlePayment(@ModelAttribute FreePassPaymentDTO dto, Model model) {
    	paymentService.saveFreePassPayment(dto);
        return "freepass";
    }
}