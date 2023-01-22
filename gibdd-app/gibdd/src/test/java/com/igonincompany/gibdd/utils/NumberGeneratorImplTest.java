package com.igonincompany.gibdd.utils;

import com.igonincompany.gibdd.models.CarNumber;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = NumberGeneratorImpl.class)
class NumberGeneratorImplTest {

    @Autowired
    private NumberGenerator numberGenerator;

    @ParameterizedTest
    @CsvSource(value = {
            "А000АА 116 RUS, А001АА 116 RUS",
            "А999АА 116 RUS, А000АВ 116 RUS",
            "А999АХ 116 RUS, А000ВА 116 RUS",
            "А999ХХ 116 RUS, В000АА 116 RUS",
            "Х999ХХ 116 RUS, А000АА 116 RUS"})
    void generateNumber(String prevNumber, String expectedNumber) {
        CarNumber number = CarNumber.parse(prevNumber);
        assertEquals(numberGenerator.next(number).toString(), expectedNumber);
    }
}