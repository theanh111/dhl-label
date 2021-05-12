package com.printway.randomCode;

import java.util.Random;

public class CodeGenerator {
    private String COMPANY_KEY_CODE = "PW";

    public String codeGenerator() {
        String code = null;
        String SixDigit = SixDigitNumberGenerator();
        String CodeWithLetter = CharacterAndNumberGenerator();
        code = COMPANY_KEY_CODE + SixDigit + CodeWithLetter;
        return code;
    }

    public String CharacterAndNumberGenerator() {
        String first = null;
        String second = null;
        String third = null;
        String finalCode = null;
        for (int i = 0; i < 3; i++) {
            Random r = new Random(System.nanoTime());
            String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            char letter = alpha.charAt(r.nextInt(alpha.length()));
            String numbers = "0123456789";
            char number = numbers.charAt(r.nextInt(numbers.length()));
            if (i == 0) {
                first = Character.toString(letter) + number;
            }
            if (i == 1) {
                second = Character.toString(letter) + number;
            }
            if (i == 2) {
                third = Character.toString(letter) + number;
            }
        }
        finalCode = first + second + third;
        return finalCode;
    }

    public String SixDigitNumberGenerator() {
        String numberFinal = null;
        Random random = new Random(System.nanoTime());
        int number = random.nextInt(999999);
        numberFinal = String.format("%06d", number);
        return numberFinal;
    }
}
