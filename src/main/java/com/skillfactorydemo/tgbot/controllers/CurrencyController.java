package com.skillfactorydemo.tgbot.controllers;

import com.skillfactorydemo.tgbot.dto.ValuteCursOnDate;
import com.skillfactorydemo.tgbot.service.CentralRussianBankService;
import com.skillfactorydemo.tgbot.service.StatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private final StatService statService;
    @GetMapping("/getCurrencies")
    public List<ValuteCursOnDate> getValuteCursOnDate() throws Exception {
        return centralRussianBankService.getCurrenciesFromCbr();
    }
    @GetMapping("/getStats")
    @ApiOperation(value = "Получение количества пополнений, которые превышают определенную сумму")
    public int getStatsAboutIncomesThatGreater(@RequestParam(value = "amount") BigDecimal amount){
        return statService.getCountOfIncomesThatGreater(amount);
    }
}
