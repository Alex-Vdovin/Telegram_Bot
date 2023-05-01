package com.skillfactorydemo.tgbot.controllers;

import com.skillfactorydemo.tgbot.dto.ValuteCursOnDate;
import com.skillfactorydemo.tgbot.service.CentralRussianBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    @RequestMapping("/test")
    @ResponseBody
    String test(){
        return "TEST COMPLETED";
    }
    private final CentralRussianBankService centralRussianBankService;
    @GetMapping("/getCurrencies")
    public List<ValuteCursOnDate> getValuteCursOnDate() throws Exception {
        return centralRussianBankService.getCurrenciesFromCbr();
    }
}
