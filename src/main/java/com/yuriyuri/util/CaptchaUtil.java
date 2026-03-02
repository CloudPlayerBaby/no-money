package com.yuriyuri.util;

import java.util.Random;

public final class CaptchaUtil {
    private CaptchaUtil(){
        throw new AssertionError("no");
    }

    public static String createCaptcha(int digit){
        Random rand = new Random();

        StringBuffer base = new StringBuffer();

        char[] letters = new char[52];
        for (int i = 0; i < 26; i++) {
            letters[i] = (char)('A'+i);
            letters[i+26] = (char) ('a'+i);
        }
        char[] numbers = {'0','1','2','3','4','5','6','7','8','9'};

        //0-digit位随机抽
        int letter = rand.nextInt(digit+1);
        int number = digit - letter;

        //添加随机位数的字母
        for (int i = 0; i < letter; i++) {
            base.append(letters[rand.nextInt(52)]);
        }

        //添加随机位数的数字
        for (int i = 0; i < number; i++) {
            base.append(numbers[rand.nextInt(10)]);
        }

        //随机打乱
        char[] charArray = base.toString().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int j = rand.nextInt(charArray.length);
            char temp = charArray[i];
            charArray[i] = charArray[j];
            charArray[j] = temp;
        }

        return new String(charArray);
    }
}
