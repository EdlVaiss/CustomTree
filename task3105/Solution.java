package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
В метод main приходит список аргументов.
Первый аргумент - полный путь к файлу fileName.
Второй аргумент - путь к zip-архиву.
Добавить файл (fileName) внутрь архива в директорию 'new'.
Если в архиве есть файл с таким именем, то заменить его.
/
Пример входных данных:
C:/result.mp3
C:/pathToTest/test.zip

Файлы внутри test.zip:
a.txt
b.txt

После запуска Solution.main архив test.zip должен иметь такое содержимое:
new/result.mp3
a.txt
b.txt

Подсказка: нужно сначала куда-то сохранить содержимое всех энтри, а потом записать в архив все энтри вместе с добавленным файлом.
Пользоваться файловой системой нельзя.


Требования:
1. В методе main создай ZipInputStream для архивного файла (второй аргумент main). Нужно вычитать из него все содержимое.
2. В методе main создай ZipOutputStream для архивного файла (второй аргумент main).
3. В ZipOutputStream нужно записать содержимое файла, который приходит первым аргументом в main.
4. В ZipOutputStream нужно записать все остальное содержимое, которое было вычитано из ZipInputStream.
5. Потоки для работы с архивом должны быть закрыты.
*/
public class Solution {
    private static final byte[] BUFFER = new byte[1024];
    public static Map<String, byte[]> entryMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String fileNameStr = args[0];
        String zipFileStr = args[1];
        Path fileName = Paths.get(fileNameStr);
        Path zipFilePath = Paths.get(zipFileStr);


        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            int bytesRead;
            ZipEntry entry;

            while ((entry = zipIn.getNextEntry()) != null) {

                if (!entry.getName().equals("new/" + fileName.getFileName().toString())) {

                    ByteArrayOutputStream bout = new ByteArrayOutputStream();

                    while ((bytesRead = zipIn.read(BUFFER)) != -1) {
                        // System.out.println(bytesRead);
                        bout.write(BUFFER, 0, bytesRead);
                    }

                    entryMap.put(entry.getName(), bout.toByteArray());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {

            for (Map.Entry<String, byte[]> pair : entryMap.entrySet()) {
                String name = pair.getKey();
                byte[] content = pair.getValue();
                zipOut.putNextEntry(new ZipEntry(name));
                zipOut.write(content);
                zipOut.closeEntry();
            }

            zipOut.putNextEntry(new ZipEntry("new/" + fileName.getFileName().toString()));
            Files.copy(fileName, zipOut);
            zipOut.closeEntry();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

