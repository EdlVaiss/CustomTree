package com.javarush.task.task32.task3204;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/* 
Генератор паролей
Реализуй логику метода getPassword, который должен возвращать ByteArrayOutputStream, в котором будут байты пароля.
Требования к паролю:
1) 8 символов.
2) только цифры и латинские буквы разного регистра.
3) обязательно должны присутствовать цифры, и буквы разного регистра.
Все сгенерированные пароли должны быть уникальные.

Пример правильного пароля:
wMh7smNu


Требования:
1. Класс Solution должен содержать метод getPassword(), который возвращает ByteArrayOutputStream со сгенерированным паролем.
2. Длина пароля должна составлять 8 символов.
3. Пароль должен содержать хотя бы одну цифру.
4. Пароль должен содержать хотя бы одну латинскую букву нижнего регистра.
5. Пароль должен содержать хотя бы одну латинскую букву верхнего регистра.
6. Пароль не должен содержать других символов, кроме цифр и латинских букв разного регистра.
7. Сгенерированные пароли должны быть уникальными.
*/
public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() {
        char[] passArr = new char[8];
        final String D_FLAG = "d";
        final String LC_FLAG = "l";
        final String UC_FLAG = "u";

        //according to the task there can be from 1 to 6 lowerCaseChars in password
        int lCaseNum = new Random().nextInt(6) + 1;
        StringBuilder passTemplateSB = new StringBuilder();
        passTemplateSB.append(String.join("", Collections.nCopies(lCaseNum, LC_FLAG)));

        //there can be digits from 1 to password_length - number_of_lowerCaseChars - 1 (-1 is used because at least 1 char
        // has to be left for UCaseChar)
        int digitsNum = new Random().nextInt(passArr.length - lCaseNum - 1) + 1;
        passTemplateSB.append(String.join("", Collections.nCopies(digitsNum, D_FLAG)));

        int UCaseNum = passArr.length - digitsNum - lCaseNum;
        passTemplateSB.append(String.join("", Collections.nCopies(UCaseNum, UC_FLAG)));

       // System.out.println(passTemplateSB);

        //password template contains flags of all necessary elements ("d" - digit, "l" - lower case char, "u" - upper case char)
        List<String> passTemplate = Arrays.asList(passTemplateSB.toString().split(""));

        Collections.shuffle(passTemplate);

        String LCaseString = "abcdefghijklmnopqrstuvwxyz";
        char[] LCaseChars = LCaseString.toCharArray();
        char[] UCaseChars = LCaseString.toUpperCase().toCharArray();
        char[] digitChars = "1234567890".toCharArray();

        for (int i = 0; i < passTemplate.size(); i++) {
            String s =  passTemplate.get(i);
            switch (s) {
                case LC_FLAG : passArr[i] = getRandomChar(LCaseChars);
                    break;
                case D_FLAG : passArr[i] = getRandomChar(digitChars);
                    break;
                case UC_FLAG : passArr[i] = getRandomChar(UCaseChars);
                    break;
            }
        }

        //System.out.println(passTemplate);
       // System.out.println(passArr);


        try ( ByteArrayInputStream bais = new ByteArrayInputStream(new String(passArr).getBytes());
              ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {

            while (bais.available() > 0) {
                baos.write(bais.read());
            }

            return baos;
        }catch(IOException e){
            e.printStackTrace();
        }


        return new ByteArrayOutputStream();
    }

    private static char getRandomChar(char[] charArr) {
        int index = new Random().nextInt(charArr.length);
        return charArr[index];
    }
}