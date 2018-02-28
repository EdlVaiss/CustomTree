package com.javarush.task.task19.task1908;

/* 
Выделяем числа
Считать с консоли 2 имени файла.
Вывести во второй файл все числа, которые есть в первом файле.
Числа выводить через пробел.
Закрыть потоки.

Пример тела файла:
12 text var2 14 8v 1

Результат:
12 14 1


Требования:
1. Программа должна считывать имена файлов с консоли (используй BufferedReader).
2. BufferedReader для считывания данных с консоли должен быть закрыт.
3. Программа должна считывать содержимое первого файла (используй BufferedReader c конструктором FileReader).
4. Поток чтения из файла (BufferedReader) должен быть закрыт.
5. Программа должна записывать во второй файл все числа, через пробел, из первого файла (используй BufferedWriter с конструктором FileWriter).
6. Поток записи в файл (BufferedWriter) должен быть закрыт.
*/

import java.io.*;
import java.nio.Buffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) {
        String file1Name = "";
        String file2Name = "";

        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            file1Name = consoleReader.readLine();
            file2Name = consoleReader.readLine();

        } catch (IOException e) {
            System.out.println(e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file1Name));
             BufferedWriter writer = new BufferedWriter(new FileWriter(file2Name))) {

            StringBuilder strBuilder = new StringBuilder();
            Pattern pattern = Pattern.compile("-?\\b\\d+\\.?\\d*\\b");

            while (reader.ready()) {
                String line = reader.readLine();
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    strBuilder.append(matcher.group()).append(" ");
                }

            }

            writer.write(strBuilder.toString());

        } catch (IOException e) {
            System.out.println(e);
        }


    }
}
