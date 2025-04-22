package com.yuntool.tool;

import com.yuntool.tool.pdfutils.MyPDFTextStripper;
import com.yuntool.tool.pdfutils.PDFTextStripperFactory;
import com.yuntool.tool.pdfutils.Paragraphs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.List;

@Slf4j
public class AnalyzePDFUtils {

    public static void main(String[] args) {
        File file = new File("D:\\JavaCode\\Project\\信息采编\\文档\\需求文档\\【2024-9-06】信息采编统计\\成稿文档示例\\重要情况专报");
        try {
            analyzeFolder(file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

//        File file = new File("D:\\JavaCode\\Project\\信息采编\\文档\\需求文档\\【2024-9-06】信息采编统计\\成稿文档示例\\河南工作动态\\产业工人.pdf");
//        getParagraphsData(file);
    }

    /**
     * 解析文件夹下全部文件
     */
    public static void analyzeFolder(File folder){
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    analyzeFolder(file);
                } else {
                    getParagraphsData(file);
                }
            }
        } else {
            System.out.println("输入的路径不是一个文件夹！");
        }
    }

    /**
     * 解析单个文件
     */
    private static void getParagraphsData(File file) {
        log.info("处理文件: " + file.getName());
        try {
            PDDocument doc = Loader.loadPDF(file);

            // 自定义PDFTextStripper，提取段落信息
            MyPDFTextStripper textStripper = PDFTextStripperFactory.getStripper("Paragraphs");
            textStripper.setSortByPosition(true);
            String text = textStripper.getText(doc);

            //模式选择
            String modeChange = null;
            if (text.startsWith("重要情况专报")|| text.substring(0, 100).contains("重要情况专报")) {
                modeChange = "Zyqkzb";
            } else if (text.startsWith("【国办约稿】")) {
                modeChange = "Gbyg";
            } else if (text.startsWith("河南工作动态")) {
                modeChange = "Hngzdt";
            } else if (!textStripper.getIsTheCurrentMode()) {
                // 大标题处理
                modeChange = "BigTitle";
            }
            if (modeChange != null) {
                textStripper = PDFTextStripperFactory.getStripper(modeChange);
                textStripper.getText(doc);
            }

            // 输出提取的段落
            output(textStripper.getParagraphs(), textStripper.getIssue());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 输出信息
     */
    private static void output(List<Paragraphs> paragraphs, String issue) {
        for (Paragraphs paragraph : paragraphs) {
//            log.info("内容:  " + paragraph.getParagraph().toString());
            log.info("标题:  " + paragraph.getTitle().toString() + (StringUtils.isEmpty(paragraph.getUnit())? "":"              ------所属单位：" + paragraph.getUnit()));
        }
        if (!issue.isEmpty()) {
            log.info("期号:  " + issue);
        }
    }

}



