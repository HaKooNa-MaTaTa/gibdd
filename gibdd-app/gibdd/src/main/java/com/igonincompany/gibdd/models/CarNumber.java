package com.igonincompany.gibdd.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "car_number")
public class CarNumber implements Comparable<CarNumber> {

    @EmbeddedId
    private ID value;

    @Column(name = "create_date")
    @EqualsAndHashCode.Exclude
    private LocalDateTime createDate;

    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Метод, возвращающий массив буквенных перечислений номера
     *
     * @return массив буквенных перечислений номера
     */
    public Letter[] getLetters() {
        return Arrays.stream(value.getLetters().split(""))
                .filter(Letter::isNumberLetter)
                .map(Letter::valueOf)
                .toArray(Letter[]::new);
    }

    /**
     * Метод, для инициализации времени создания номера.
     */
    @PrePersist
    void onCreate() {
        createDate = LocalDateTime.now();
    }

    @Override
    public int compareTo(CarNumber carNumber) {
        int thisDigitsNumber = this.value.getDigits();
        int anotherDigitsNumber = carNumber.getValue().getDigits();

        if (thisDigitsNumber == anotherDigitsNumber) {

            Letter[] thisNumberLetters = getLetters();
            Letter[] anotherNumberLetters = carNumber.getLetters();

            for (int i = 2; i >= 0; i--) {
                if (thisNumberLetters[i].getPriority() != anotherNumberLetters[i].getPriority()) {
                    return thisNumberLetters[i].getPriority() - anotherNumberLetters[i].getPriority();
                }
            }

            return 0;
        }
        return thisDigitsNumber - anotherDigitsNumber;
    }

    /**
     * Метод для парсинга строки. Вытаскивает из строки буквы, цифры и регион.
     * @param value - стрковое представление автомобильного номера.
     * @return - объект типа Number (новый номер)
     */
    public static CarNumber parse(String value) {
        String letters = value.charAt(0) + value.substring(4, 6);
        int digits = Integer.parseInt(value.substring(1, 4));
        String region = value.substring(6).trim();

        return CarNumber.of(letters, digits, region);
    }

    /**
     * Метод для создания номера. На вход принимает буквы, цифры и регион номера.
     * @param letters - буквы номера
     * @param digits - цифры номера
     * @param region - регион номер
     * @return объект типа Number (новый номер)
     */
    public static CarNumber of(String letters, int digits, String region) {
        return new CarNumber()
                .setValue(new ID()
                        .setLetters(letters)
                        .setDigits(digits)
                        .setRegion(region));
    }

    /**
     * Буквенные перечисления для автомобильного номера
     */
    public enum Letter {
        А(1),
        Е(3),
        Т(10),
        О(7),
        Р(8),
        Н(6),
        У(11),
        К(4),
        Х(12),
        С(9),
        В(2),
        М(5);

        @Getter
        private final int priority;

        Letter(int priority) {
            this.priority = priority;
        }

        /** Метод, возвращающий следуещее буквенное перечисление
         * @return следующее буквенное перечисление, которое идет после текущего буквенного перечисления
         */
        public Letter next() {
            return Arrays.stream(Letter.values())
                    .filter(p -> p.getPriority() == this.priority + 1)
                    .findFirst()
                    .orElse(А);
        }

        /**
         * Метод, проверяющий, является ли приоритет текущей буквы максимальным.
         *
         * @return возвращает true, если приоритет имеет максимальное значение (буква является последней в
         * диапазоне буквенных значений, указанных в классе). Во всех остальных случаях возвращает false.
         */
        public boolean isMaxPriority() {
            int max = Arrays.stream(Letter.values())
                    .mapToInt(Letter::getPriority)
                    .max()
                    .orElse(1);

            return this.getPriority() == max;
        }

        /**
         * Вспомогательный метод для сравнения эквивалентности букв между собой
         *
         * @param letter - строковое представление буквенного перечисления
         * @return true - если буквы эквивалентны, во всех остальных случаях - false
         */
        public static boolean isNumberLetter(String letter) {
            Letter[] allLetters = Letter.values();
            for (Letter thisLetter : allLetters) {
                if (letter.equals(thisLetter.name())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Вспомогательный метод, возвращающий случайную букву для номера из перечислений, указанных в классе.
         *
         * @return случайную букву из перечислений, указанных в классе.
         */
        public static Letter getRandomLetter() {
            int range = Letter.values().length;
            int index = ThreadLocalRandom.current().nextInt(range);
            return Letter.values()[index];
        }
    }

    /**
     * Вложенный статический класс, хранящий в себе буквы, цифры и регион номера.
     * Объект класса ID используется в качестве уникального идентификатора объекта Number.
     */
    @Embeddable
    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class ID implements Serializable {
        @NotBlank
        @Column(name = "letters")
        private String letters;

        @Max(999)
        @Positive
        @Column(name = "digits")
        private int digits;

        @NotBlank
        @Column(name = "region")
        private String region;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(letters.charAt(0));
            sb.append(String.format("%03d", digits));
            sb.append(letters.charAt(1));
            sb.append(letters.charAt(2));
            sb.append(" ");
            sb.append(region);

            return sb.toString();
        }
    }
}
