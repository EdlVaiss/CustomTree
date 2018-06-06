package com.javarush.task.task37.task3714;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 
Древний Рим
Амиго, привет! Я недавно увлекся историей вашей планеты и меня заинтересовал период Древнего Рима.
Интересно тогда было жить, сплошные развлечения и вино! Или рабство, если не повезло со стартовой локацией...

В общем, мне нужен метод romanToInteger, который будет конвертировать число в римской системе счисления {I, V, X, L, C, D, M} в десятичную.
Иначе никак не разобрать что и когда у них происходило.


Требования:
1. Метод romanToInteger должен конвертировать число в римской системе счисления в десятичную.
2. Метод romanToInteger должен возвращать значение типа int и принимать один параметр типа String.
3. Метод romanToInteger должен быть публичным.
4. Метод romanToInteger должен быть статическим.
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input a roman number to be converted to decimal: ");
        String romanString = bufferedReader.readLine();
        System.out.println("Conversion result equals " + romanToInteger(romanString));
    }

    public static int romanToInteger(String s) {
/*
https://ru.wikipedia.org/wiki/%D0%A0%D0%B8%D0%BC%D1%81%D0%BA%D0%B8%D0%B5_%D1%86%D0%B8%D1%84%D1%80%D1%8B

Для правильной записи больших чисел римскими цифрами необходимо сначала записать число тысяч, затем сотен, затем десятков и, наконец, единиц.

При этом некоторые из цифр (I, X, C, M) могут повторяться, но не более трёх раз подряд; таким образом, с их помощью можно записать любое целое число не более 3999 (MMMCMXCIX).
При записи чисел в римской системе счисления меньшая цифра может стоять справа от большей; в этом случае она прибавляется к ней.
Например, число 283 по-римски записывается как CCLXXXIII, то есть 100+100+50+30+3=283.
Здесь цифра, изображающая сотню, повторена два раза, а цифры, изображающие соответственно десяток и единицу, повторены по три раза.

Пример: число 1988. Одна тысяча M, девять сотен CM, восемь десятков LXXX, восемь единиц VIII. Запишем их вместе: MCMLXXXVIII.

Меньшая цифра может быть записана и слева от большей, тогда её следует вычесть из большей.
При этом вычитаться могут только цифры, обозначающие 1 или степени 10, а в качестве уменьшаемого выступать только
ближайшие в числовом ряду к вычитаемой две цифры (то есть вычитаемое, умноженное на 5 или 10).
Повторения меньшей цифры не допускаются. Таким образом, существует только шесть вариантов использования «правила вычитания»:

IV = 4
IX = 9
XL = 40
XC = 90
CD = 400
CM = 900

Например, число 94 будет XCIV = 100 − 10 + 5 − 1 = 94 — так называемое «правило вычитания» (появилось в эпоху поздней
античности, а до этого римляне писали число 4 как IIII, а число 40 — как XXXX).

Необходимо отметить, что другие способы «вычитания» недопустимы; так, число 99 должно быть записано как XCIX, но не как IC.

 */
        int result = 0;
        String ucs = s.toUpperCase();
        Pattern restrictedSymbolsPattern = Pattern.compile("[^IVXLCDM]");

        checkFormat(ucs, restrictedSymbolsPattern, "\"%s\" is not allowed to use in Romans!");

        ucs = ucs.replace("IV", "A").replace("IX", "B").replace("XL", "E")
                .replace("XC", "F").replace("CD", "G").replace("CM", "H");

        Pattern restrictedRepeatPattern = Pattern.compile("I{4}|X{4}|C{4}|M{4}|V{2}|L{2}|D{2}|A{2}|B{2}|E{2}|F{2}|G{2}|H{2}");
        //in roman numbers only I, X, C, M can be repeated and they can be repeated not more than 3 times
        //Attention! regex like [IXCM]{4} doesn't work
        //Romans V,L,D and fakeRomans (see enum Romans) are not allowed to be repeated


        checkFormat(ucs, restrictedRepeatPattern, "\"%s\" is repeated more then allowed!");

        char[] charsArr = ucs.toCharArray();

        Romans[] enumsArr = new Romans[charsArr.length];

        for (int i = 0; i < charsArr.length; i++) {
            enumsArr[i] = Enum.valueOf(Romans.class, String.valueOf(charsArr[i]));
        }

        //in proper Romans numbers all the digits (in case of replacing some elements by FakeRomans (see enum Romans)) must be in descending order
        //to check if digits order is OK compare enumsArr and sorted enumsArr, if they are equal then the order is OK
        Romans[] sortedEnumsArr = Arrays.copyOf(enumsArr, enumsArr.length);

        Arrays.sort(sortedEnumsArr,new RomansDescendingComparator());

        if (areEqualArrays(enumsArr, sortedEnumsArr)) {//arrays are equal if all corresponding elements are equal
            for (int i = 0; i < enumsArr.length; i++) {
                result += enumsArr[i].getArabVal();
            }
        } else {
            throw new NumberFormatException("Improper order of Romans digits!");
        }

        return result;
    }

    private static boolean checkFormat(String strToCheck, Pattern pattern, String messageIfError) {
        Matcher matcher = pattern.matcher(strToCheck);
        if (matcher.find()) {
            String symbol = matcher.group().substring(0, 1);
            throw new NumberFormatException(String.format(messageIfError, Enum.valueOf(Romans.class, symbol).getAnalog()));
        }
        return true;
    }

    private static enum Romans {
        I(1), A(4, "IV"), V(5), B(9, "IX"), X(10), E(40, "XL"), L(50), F(90, "XC"), C(100), G(400, "CD"), D(500), H(900, "CM"), M(1000),;

        //inserted fake Roman numbers to change 2-digit Roman numbers to make parsing easier
        //FakeRomans:
        //A is IV, B is IX, E is XL, F is XC, G is CD, H is CM

        private int arabVal;
        private String analog;

        private Romans(int arabVal, String... analog) {
            this.arabVal = arabVal;
            if (analog.length == 0) {
                this.analog = this.toString();
            } else {
                this.analog = analog[0];
            }
        }

        public int getArabVal() {
            return arabVal;
        }

        public String getAnalog() {
            return analog;
        }
    }

    private static <T> boolean areEqualArrays(T[] arr1, T[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }

        for (int i = 0; i < arr1.length; i++) {
            T arr1Elmnt = arr1[i];
            T arr2Elmnt = arr2[i];
            if (!arr1Elmnt.equals(arr2Elmnt)) {
                return false;
            }
        }
        return true;
    }

    private static class RomansDescendingComparator implements Comparator<Romans> {

        public int compare(Romans a, Romans b){

            return a.compareTo(b) * -1;//multiply by -1 to get descending comparison
        }
    }


}
