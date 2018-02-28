package com.javarush.task.task19.task1918;

/* 
Знакомство с тегами
Считайте с консоли имя файла, который имеет HTML-формат.

Пример:
Info about Leela <span xml:lang="en" lang="en"><b><span>Turanga Leela
</span></b></span><span>Super</span><span>girl</span>

Первым параметром в метод main приходит тег. Например, "span".
Вывести на консоль все теги, которые соответствуют заданному тегу.
Каждый тег на новой строке, порядок должен соответствовать порядку следования в файле.
Количество пробелов, n, r не влияют на результат.
Файл не содержит тег CDATA, для всех открывающих тегов имеется отдельный закрывающий тег, одиночных тегов нет.
Тег может содержать вложенные теги.

Пример вывода:
<span xml:lang="en" lang="en"><b><span>Turanga Leela</span></b></span>
<span>Turanga Leela</span>
<span>Super</span>
<span>girl</span>

Шаблон тега:
<tag>text1</tag>
<tag text2>text1</tag>
<tag
text2>text1</tag>

text1, text2 могут быть пустыми


Требования:
1. Программа должна считывать имя файла с консоли (используй BufferedReader).
2. BufferedReader для считывания данных с консоли должен быть закрыт.
3. Программа должна считывать содержимое файла (используй FileReader).
4. Поток чтения из файла (FileReader) должен быть закрыт.
5. Программа должна выводить в консоль все теги, которые соответствуют тегу, заданному в параметре метода main.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) {
        String fileName = "";
        String tag = args[0];

        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            fileName = consoleReader.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            while (fileReader.ready()) {
                stringBuilder.append(fileReader.readLine());
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        LinkedList<tagPos> openTagsIndices = new LinkedList<>();//use it as a stack
        TreeMap<Integer, tagPos> tagPosMap = new TreeMap<>();

        int openTagIndex = 0;
        int len = 0;
        int tagCounter = 0;//tagCounter is to mark tags order in order of their appearance

        Pattern pattern = Pattern.compile("<" + tag + "|" + "</" + tag + ">");//like that "<span|</span>"
        Matcher matcher = pattern.matcher(stringBuilder);

        while (matcher.find()) {
            String match = matcher.group();

            if (match.equals("<" + tag)) {
                tagCounter++;
                openTagIndex = matcher.start();
                //if openTag found push it's info (order and position in the string) to stack (LinkedList)
                openTagsIndices.push(new tagPos(tagCounter, openTagIndex));

            } else {
                // if closeTag found move the first (the last that was pushed)
                // element of the LinkedList to the TreeMap with "len" property set
                tagPos tagPosObj = openTagsIndices.getFirst();
                len = matcher.end() - tagPosObj.getOpenTagPos();
                tagPosObj.setLen(len);

                tagPosMap.put(tagPosObj.getOrder(), openTagsIndices.removeFirst());
            }

        }

        for (int i = 1; i <= tagPosMap.size(); i++) {
            System.out.println(tagPosMap.get(i).processString(stringBuilder));
        }

    }


    private static class tagPos {
        private int order;//order of tags appearance in a given string
        private int openTagPos;//start index of the tag in a given string
        private int len;// the length of the tag

        public tagPos(int order, int openTagPos) {
            this.order = order;
            this.openTagPos = openTagPos;
            this.len = 0;
        }

        public tagPos(int order, int openTagPos,int len) {
            this.order = order;
            this.openTagPos = openTagPos;
            this.len = len;
        }

        public int getOrder() {
            return order;
        }

        public int getOpenTagPos() {
            return openTagPos;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String processString(StringBuilder stringBuilder) {
            return new String(stringBuilder.toString().toCharArray(), openTagPos, len);
        }
    }
}

