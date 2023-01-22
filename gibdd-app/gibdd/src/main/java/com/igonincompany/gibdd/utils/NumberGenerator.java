package com.igonincompany.gibdd.utils;

import com.igonincompany.gibdd.models.CarNumber;

public interface NumberGenerator {

    CarNumber next(CarNumber carNumber);

    CarNumber random();
}
