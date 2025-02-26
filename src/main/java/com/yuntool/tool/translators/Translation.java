//package com.yuntool.tool.translators;
//
//import com.alibaba.fastjson.JSON;
//import com.yuntool.tool.translators.baiduTranslate.TransApi;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.*;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
///**
// * 翻译Obsidian中的js文件
// * @date 2022/7/4 1:07
// */
//public class Translation {
//
//    private static final String APP_ID = "20230627001726030";
//    private static final String SECURITY_KEY = "R9XbbSizCPFkGEVqgLqF";
//
//    public static void main(String[] args) throws Exception {
//
////        File file = new File("D:\\笔记\\笔记\\.obsidian\\plugins");
//        File file = new File("D:\\笔记\\笔记\\.obsidian\\plugins\\copilot\\main.js");
//
//
//        //翻译单个文件
//        backups(file, new File(file.getAbsolutePath() + ".bak"));
//        translate(file);
//
//        //翻译文件夹下所有的main.js
////        traverseFolder(file);
//
//
//    }
//
//    public static void traverseFolder(File folder) throws Exception {
//        if (folder.isDirectory()) {
//            for (File file : folder.listFiles()) {
//                if (file.isDirectory()) {
//                    traverseFolder(file);
//                } else {
//                    System.out.println(file.getAbsolutePath());
//                    if (file.getAbsolutePath().contains("remotely-save")) {
//                        continue;
//                    }
//                    if (file.getAbsolutePath().toString().endsWith("main.js")) {
//                        String absolutePath = file.getAbsolutePath();
//                        backups(file, new File(absolutePath + ".bak"));
//                        System.out.println("翻译该文件");
//                        translate(file);
//                    }
//                }
//            }
//        } else {
//            System.out.println("输入的路径不是一个文件夹！");
//        }
//    }
//
//    private static void translate(File file) throws IOException, InterruptedException {
//        // 需要翻译的插件对应的main.js文件
////        File file = new File("D:\\笔记\\笔记\\.obsidian\\plugins\\obsidian-banners\\main.js");
//        System.out.println(file.getAbsolutePath());
//        FileInputStream stream = new FileInputStream(file);
//        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        LinkedList<String> list = new LinkedList<>();
//        StringBuilder stringBuilder = new StringBuilder();
//
//        // 输出汉英对照.txt
//        File file1 = new File(".\\log.txt");
//        FileOutputStream fileOutputStream = new FileOutputStream(file1);
//
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
//        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
//
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
////            if (line.matches(".*[\\u4e00-\\u9fa5]+.*")) {
////                System.out.println(file.getName() + "已翻译过");
////                return;
////            }
//            // 匹配要翻译的部分
//            String s_setName = "setName\\([^\\)]+\\)";
//            String s_addOption = "addOption\\([^\\)]+\\)";
//            String s_setDesc = "setDesc\\([^\\)]+\\)*\"\\)";
//            String s_name = "name: \".*\",";
//            String s_append = ",d.appendText[^\\)]+\\)*\"\\)";
//            String s_header = ".createHeader[^\\)]+\\)*[\"\']\\)";
//            String s_title = "title: \".*\",";
//            String s_description = "description: \".*\",";
//
//            // 使用正则查找匹配
//            List<String> linkedList;
//
//            try {
//                if ((linkedList = find(s_setName, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        list.add(s.substring(9, s.length() - 2));
//                        line = translationAndReplace(writer, s, line, 9, 2);
//                    }
//                }
//                if ((linkedList = find(s_setDesc, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        //setDesc(createFragment(d=>{d.appendText("Choose whether to display the banner in the internal embed. This is the embed that appears when you write ")
//                        if (s.contains("appendText")) {
//                            list.add(s.substring(41, s.length() - 2));
//                            line = translationAndReplace(writer, s, line, 41, 2);
//                        } else {
//                            list.add(s.substring(9, s.length() - 2));
//                            line = translationAndReplace(writer, s, line, 9, 2);
//
//                        }
//                    }
//                }
//                if ((linkedList = find(s_addOption, line)).size() != 0) {
//                    for (String ss : linkedList) {
//                        if(!ss.contains("\"")) {
//                            continue;
//                        }
//                        String substring = ss.substring(10, ss.length() - 1);
//                        String[] split = substring.split(",");
//                        for (String s : split) {
//                            s = s.strip();
//                            list.add(s.substring(1, s.length() - 1));
//                            line = translationAndReplace(writer, s, line, 1, 1);
//                        }
//                    }
//                }
//                if ((linkedList = find(s_name, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        list.add(s.substring(7, s.length() - 2));
//                        line = translationAndReplace(writer, s, line, 7, 2);
//                    }
//                }
//
//                if ((linkedList = find(s_title, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        list.add(s.substring(8, s.length() - 2));
//                        line = translationAndReplace(writer, s, line, 8, 2);
//                    }
//                }
//                if ((linkedList = find(s_description, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        list.add(s.substring(8, s.length() - 2));
//                        line = translationAndReplace(writer, s, line, 14, 2);
//                    }
//                }
//
//                if ((linkedList = find(s_append, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        list.add(s.substring(15, s.length() - 2));
//                        line = translationAndReplace(writer, s, line, 15, 2);
//                    }
//                }
//                if ((linkedList = find(s_header, line)).size() != 0) {
//                    for (String s : linkedList) {
//                        String ss = s.substring(14, s.length() - 1);
//                        Pattern pattern = Pattern.compile("\"(.*?)\"|'(.*?)'");
//                        Matcher matcher = pattern.matcher(ss);
//                        while (matcher.find()) {
//                            String fundStr = ss.substring(matcher.start(), matcher.end());
//                            System.out.println(fundStr);
//                            list.add(fundStr.substring(1, fundStr.length() - 1));
//                            line = translationAndReplace(writer, fundStr, line, 1, 1);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                stringBuilder.append(line).append("\n");
//            }
//            stringBuilder.append(line).append("\n"); // 将修改后的行添加到 StringBuilder 中
//        }
//
//        System.out.println(list + "<<<<<<<<129");
//        bufferedReader.close();
//
//        FileWriter writer1 = new FileWriter(file, Charset.forName("UTF-8"));
//        writer1.write(stringBuilder.toString());
//        writer1.close();
//
//        writer.flush();
//    }
//
//    private static String translationAndReplace(BufferedWriter writer, String s, String line, int startIndex, int endIndex) throws IOException, InterruptedException {
//        if (StringUtils.isEmpty(s)) {
//            return line;
//        }
//
//        if (s.contains(".") || s.contains("@") || s.contains("-") || s.contains("(") || s.contains("{")) {
//            return line;
//        }
//
//        if (s.matches(".*[\\u4e00-\\u9fa5]+.*") || s.contains("\\")) {
//            return line;
//        }
////        Thread.sleep(1000);
//        String findContent = s.substring(startIndex, s.length() - endIndex);
//        if (findContent.isEmpty()) {
//            return line;
//        }
//
//        //百度翻译
//        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
//        String transResult = api.getTransResult(findContent, "auto", "zh");
//        if (StringUtils.isNotEmpty(transResult)) {
//            String translation = JSON.parseObject(JSON.parseObject(transResult).getJSONArray("trans_result").get(0).toString()).get("dst").toString();
//            String finalContent = s.substring(0, startIndex) + translation + s.substring(s.length() - endIndex, s.length());
//            //记录
//            writer.append(s).append("\n");
//            writer.append(finalContent).append("\n");
//
//            return line.replace(s, finalContent); // 将原始字符串替换为翻译后的字符串
//        } else {
//            writer.append(s).append("<<<<<<<<<<<<<<<<<<翻译失败\n");
//            return line;
//        }
//
//        //有道翻译
////        try {
////            String query = YouDaoTranslation.query(findContent);
////            String translation = JSON.parseObject(query).getJSONArray("translation").get(0).toString();
////
////            if (StringUtils.isNotEmpty(translation)) {
////    //            String translation = JSON.parseObject(JSON.parseObject(transResult).getJSONArray("trans_result").get(0).toString()).get("dst").toString();
////    //        System.out.println(translate);
////
////                String finalContent = s.substring(0, startIndex) + translation + s.substring(s.length() - endIndex, s.length());
////                //记录
////                writer.append(s).append("\n");
////                writer.append(finalContent).append("\n");
////
////                return line.replace(s, finalContent); // 将原始字符串替换为翻译后的字符串
////            } else {
////                writer.append(s).append("<<<<<<<<<<<<<<<<<<翻译失败\n");
////                return line;
////            }
////        } catch (Exception e) {
////            return line;
////        }
//
//
//    }
//
//    public static List<String> find(String regex, String str) {
//        List<String> strings = new LinkedList<>();
//        Pattern p = Pattern.compile(regex);
//        Matcher matcher = p.matcher(str);
//        while (matcher.find()) {
//            String fundStr = str.substring(matcher.start(), matcher.end());
//            System.out.println("fundStr: " + fundStr);
//            strings.add(fundStr);
//        }
//        return strings;
//    }
//
//    public static void backups(File source, File dest) throws IOException {
//        InputStream is = null;
//        OutputStream os = null;
//        try {
//            is = new FileInputStream(source);
//            os = new FileOutputStream(dest);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = is.read(buffer)) > 0) {
//                os.write(buffer, 0, length);
//            }
//        } finally {
//            is.close();
//            os.close();
//        }
//    }
//
//
//}