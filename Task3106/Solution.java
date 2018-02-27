package com.javarush.task.task31.task3106;

import java.io.*;
import java.nio.file.*;
import java.util.TreeSet;
import java.util.zip.*;
import java.util.zip.ZipOutputStream;


/*
Разархивируем файл
В метод main приходит список аргументов.
Первый аргумент - имя результирующего файла resultFileName, остальные аргументы - имена файлов fileNamePart.
Каждый файл (fileNamePart) - это кусочек zip архива. Нужно разархивировать целый файл, собрав его из кусочков.
Записать разархивированный файл в resultFileName.
Архив внутри может содержать файл большой длины, например, 50Mb.
Внутри архива может содержаться файл с любым именем.

Пример входных данных. Внутри архива находится один файл с именем abc.mp3:
C:/result.mp3
C:/pathToTest/test.zip.003
C:/pathToTest/test.zip.001
C:/pathToTest/test.zip.004
C:/pathToTest/test.zip.002


Требования:
1. В методе main нужно создать ZipInputStream для архива, собранного из кусочков файлов. Файлы приходят аргументами в main,
начиная со второго.
2. Создай поток для записи в файл, который приходит первым аргументом в main. Запиши туда содержимое файла из архива.
3. Поток для чтения из архива должен быть закрыт.
4. Поток для записи в файл должен быть закрыт.
*/
public class Solution {
    public static void main(String[] args) {
        TreeSet<String> partPathStrings = new TreeSet<>();

        Path resultFileName = Paths.get(args[0]);

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            partPathStrings.add(arg);
        }

        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {

            for (String s : partPathStrings) {
                bout.write(Files.readAllBytes(Paths.get(s)));
            }

            try (ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(bout.toByteArray()));
                 FileOutputStream fileOut = new FileOutputStream(new File(args[0]))) {

                byte[] buff = new byte[4 * 1024 * 1024];
                zipIn.getNextEntry();
                int bytesRead;
                while ((bytesRead = zipIn.read(buff)) != -1) {
                    fileOut.write(buff,0,bytesRead);
                }

                //Files.copy(zipIn, resultFileName);//it is much more convenient to use this method
                // without FileOutputStream and buffer read\write but validator refused to accept it

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
