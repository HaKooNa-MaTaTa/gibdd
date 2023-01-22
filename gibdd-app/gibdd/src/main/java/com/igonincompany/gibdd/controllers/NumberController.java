package com.igonincompany.gibdd.controllers;

import com.igonincompany.gibdd.services.NumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/number")
public class NumberController {

    private final NumberService numberService;

    @GetMapping("/random")
    public String getRandomNumber(Model model) {
        model.addAttribute("randomNumber", numberService.generateRandomNumber());
        return "randomNumber";
    }

    @GetMapping("/next")
    public String getSerialNumber(Model model) {
        model.addAttribute("nextNumber", numberService.generateNextNumber());
        return "nextNumber";
    }
}
