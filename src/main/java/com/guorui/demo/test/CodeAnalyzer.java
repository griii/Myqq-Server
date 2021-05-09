package com.guorui.demo.test;

import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class CodeAnalyzer {
    ArrayList<File> fileList;
    File root;

    public CodeAnalyzer(String pathName) {
        root = new File(pathName);
        fileList = new ArrayList<>();
    }

    public void searchFiles() {
        File[] files = root.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                root = files[i];
                searchFiles();
            } else {
                if (files[i].getName().endsWith(".java"))
                    fileList.add(files[i]);
            }
        }
    }

    public void codeAnalyze() {
        double rowsCount = 0;
        double commentsCount = 0;
        double blanksCount = 0;
        double codesCount = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (File file : fileList) {
            try {
                rowsCount += countRows(file);
                blanksCount += countBlanks(file);
                commentsCount += countComments(file);
                codesCount = rowsCount - blanksCount - commentsCount;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输出结果
        System.out.println("源程序文件总行数:" + (int) rowsCount);
        System.out.println("代码行数:" + (int) codesCount + ",占" + df.format(codesCount / rowsCount * 100) + "%");
        System.out.println("注释行数:" + (int) commentsCount + ",占" + df.format(commentsCount / rowsCount * 100) + "%");
        System.out.println("空白行数:" + (int) blanksCount + ",占" + df.format(blanksCount / rowsCount * 100) + "%");
    }

    public int countRows(File file) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        int rows = 0;
        while (input.readLine() != null) {
            rows++;
        }
        return rows;
    }

    public int countBlanks(File file) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        int blanks = 0;
        String line = null;
        while ((line = input.readLine()) != null) {
            if (line.trim().equals("")) blanks++;
        }
        return blanks;
    }

    public int countComments(File file) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        int comments = 0;
        String line = null;
        while ((line = input.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("//")) {//单行注释
                comments++;
            } else if (line.startsWith("/*")) { //多行及文档注释
                comments++;
                while (!line.endsWith("*/")) {
                    line = input.readLine().trim();
                    comments++;
                }
            } else if (line.contains("/*")) { //下行尾多行注释
                line = input.readLine().trim();
                if (line.endsWith("*/")) comments++;
            }

        }
        return comments;
    }

    public static void main(String[] args) {
        String pathName = "C:\\Users\\Administrator\\IdeaProjects\\myqq\\src\\main\\java\\com\\guorui";
        CodeAnalyzer analyzer = new CodeAnalyzer(pathName);
        analyzer.searchFiles();
        analyzer.codeAnalyze();
    }
}