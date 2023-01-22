package com.igonincompany.gibdd.services;

import com.igonincompany.gibdd.exceptions.MaxAmountNumberException;
import com.igonincompany.gibdd.models.CarNumber;
import com.igonincompany.gibdd.repositories.NumberRepository;
import com.igonincompany.gibdd.utils.NumberGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NumberServiceImpl implements NumberService {

    private final NumberRepository numberRepository;
    private final NumberGenerator numberGenerator;
    private final AtomicReference<CarNumber> lastNumber = new AtomicReference<>();
    private final Set<CarNumber> allNumbers = new ConcurrentSkipListSet<>();
    public static final Integer MAX_DIGIT_NUMBER = 999;
    private static final Integer MAX_COUNT_NUMBERS = 1_726_272;

    @Autowired
    public NumberServiceImpl(NumberRepository numberRepository, NumberGenerator numberGenerator) {
        this.numberRepository = numberRepository;
        this.numberGenerator = numberGenerator;
    }

    @PostConstruct
    private void init() {
        numberRepository.getLastNumber().ifPresent(lastNumber::set);
        allNumbers.addAll(new HashSet<>(
                new ArrayList<>(numberRepository.findAll())));
    }

    @Override
    public String generateRandomNumber() {
        if (allNumbers.size() == MAX_COUNT_NUMBERS) {
            throw new MaxAmountNumberException("All numbers used!");
        }

        CarNumber number = numberGenerator.random();

        while (allNumbers.contains(number)) {
            number = numberGenerator.random();
        }

        saveNumber(number);

        return number.toString();
    }

    @Override
    public String generateNextNumber() {
        if (allNumbers.size() == MAX_COUNT_NUMBERS) {
            throw new MaxAmountNumberException("All numbers used!");
        }

        CarNumber number = lastNumber.get();

        if (number == null) {
            return generateRandomNumber();
        }

        number = numberGenerator.next(number);

        while (allNumbers.contains(number)) {
            number = numberGenerator.next(number);
        }

        saveNumber(number);

        return number.toString();
    }

    private void saveNumber(CarNumber number) {
        numberRepository.save(number);
        allNumbers.add(number);
        lastNumber.set(number);
    }
}