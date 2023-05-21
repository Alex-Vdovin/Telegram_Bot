package com.skillfactorydemo.tgbot.service;

import com.skillfactorydemo.tgbot.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatsRepository statsRepository;

    public int getCountOfIncomesThatGreater(BigDecimal amount){
        return statsRepository.getCountOfIncomesThatGreaterThan(amount);
    }
}
