package util;

import java.util.Random;

/**
 * Генератор псевдослучайных кодов
 *
 * @author Pavel Stankevich
 */
public class CodeGenerator {

    private static final int LENGTH = 4;

    /**
     * Генерирует числовой код
     *
     * @return сгенерированный код
     */
    public static String generateCode() {
        return generateCode(LENGTH);
    }

    /**
     * Генерирует числовой код заданной длинны
     *
     * @param length длинна кода
     * @return сгенерированный код
     */
    public static String generateCode(int length) {
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(rand.nextInt(10));
        }
        return result.toString();
    }
}
