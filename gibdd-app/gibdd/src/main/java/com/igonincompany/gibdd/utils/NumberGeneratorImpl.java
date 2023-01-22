package com.igonincompany.gibdd.utils;

import com.igonincompany.gibdd.models.CarNumber;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.igonincompany.gibdd.services.NumberServiceImpl.MAX_DIGIT_NUMBER;


@Component
public class NumberGeneratorImpl implements NumberGenerator {

    private static final String DEFAULT_REGION = "116 RUS";

    @Override
    public CarNumber next(CarNumber carNumber) {
        if (carNumber.getValue().getDigits() < MAX_DIGIT_NUMBER) {
            return internalNextByDigit(carNumber);
        } else {
            return internalNextByLetter(carNumber);
        }
    }

    private CarNumber internalNextByDigit(CarNumber carNumber) {
        return CarNumber.of(
                carNumber.getValue().getLetters(),
                carNumber.getValue().getDigits() + 1,
                carNumber.getValue().getRegion()
        );
    }

    private CarNumber internalNextByLetter(CarNumber carNumber) {
        CarNumber.Letter[] letters = carNumber.getLetters();
        int position = getMaxPriorityLetter(letters);
        String newLetters = iterateLetter(position, letters);

        return CarNumber.of(
                newLetters,
                0,
                carNumber.getValue().getRegion()
        );
    }

    private int getMaxPriorityLetter(CarNumber.Letter[] letters) {
        for (int i = letters.length - 1; i >= 0; i--) {
            if (!letters[i].isMaxPriority()) {
                return i;
            }
        }

        return -1;
    }

    private String iterateLetter(int position, CarNumber.Letter[] letterEnum) {
        return switch (position) {
            case 0 -> iterate(0, letterEnum);
            case 1 -> iterate(1, letterEnum);
            case 2 -> iterate(2, letterEnum);
            case -1 -> iterate(-1, letterEnum);
            default -> throw new IllegalArgumentException("Illegal position - " + position);
        };
    }

    private String iterate(int position, CarNumber.Letter[] letters) {
        if (position == -1) {
            Arrays.fill(letters, CarNumber.Letter.А);
            return letters[0].name() +
                    letters[1].name() +
                    letters[2].name();
        }

        letters[position] = letters[position].next();

        for (int i = position + 1; i < letters.length; i++) {
            letters[i] = CarNumber.Letter.А;
        }

        return letters[0].name() +
                letters[1].name() +
                letters[2].name();
    }

    @Override
    public CarNumber random() {
        int digits = ThreadLocalRandom.current().nextInt(1000);
        StringBuilder sbLetters = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sbLetters.append(CarNumber.Letter.getRandomLetter());
        }

        return CarNumber.of(
                sbLetters.toString(),
                digits,
                DEFAULT_REGION
        );
    }
}
