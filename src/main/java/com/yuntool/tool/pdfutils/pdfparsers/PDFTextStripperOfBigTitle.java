package com.yuntool.tool.pdfutils.pdfparsers;

import com.yuntool.tool.pdfutils.MyPDFTextStripper;
import com.yuntool.tool.pdfutils.Paragraphs;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.TextPosition;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 按照段落拆分，提取标题、来源单位
 *
 * @date 2025/2/20 14:42
 */
@Data
public class PDFTextStripperOfBigTitle extends MyPDFTextStripper {
    //段落
    private List<Paragraphs> paragraphs = new ArrayList<>();
    //期号
    private String issue;

    private Paragraphs currentParagraph = new Paragraphs();

    //上一字符信息
    private double previousY = -1;
    private String previousStr = "";

    //计算正文中前一百个字符有多少种行间距，如果是大标题模式，则会在正文中前一百个字符内产生最少三种行间距
    boolean startMainTitle = false;


    public PDFTextStripperOfBigTitle() throws Exception {
        super();
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        super.processTextPosition(text);
        //坐标
        float currentY = text.getY();
        float currentX = text.getX();
        if (currentY > 760) {
            return;
        }

        // 获取当前字符的字体名称
        PDFont font = text.getFont();
        String fontName = text.getFont().getName();
        String unicode = text.getUnicode();

//        System.out.println("text: " + text);
//        System.out.println("text.getFont(): " + fontName);
//        System.out.println("cY: " + currentY);
//        System.out.println("cX: " + currentX);

        if (currentY - previousY > 40 && previousStr.equals("日")) {
            //开始正文标题
            startMainTitle = true;
        }

        //提取段落,暂且认为大标题模式下只有一个大标题
        if (startMainTitle && currentY - previousY > 40 && !previousStr.equals("日")) { // 正文标题结束、开始正文
            //正文标题结束
            startMainTitle = false;
        }

        // 添加当前字符到当前段落
        currentParagraph.addParagraph(unicode);
        if (startMainTitle) {
            currentParagraph.addTitle(unicode);
        }

        // 更新前一个字符的X坐标
        if (!unicode.equals(" ")) {
            previousY = currentY;
            previousStr = unicode;
        }
    }



    public List<Paragraphs> getParagraphs() {
        if (!currentParagraph.getParagraph().toString().isEmpty()) {
            paragraphs.add(currentParagraph);
            currentParagraph = new Paragraphs();
        }
        //提取单位
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)(?!.*\\().*");// 匹配最后一个括号内容
        for (Paragraphs paragraph : paragraphs) {
            Matcher matcher = pattern.matcher(paragraph.getParagraph());
            String unit = matcher.find() ? matcher.group(1) : ""; // 返回带括号的内容
            paragraph.setUnit(unit);
        }

        //提取期号
        Pattern pattern1 = Pattern.compile("\\(([^)]+)\\).*");// 匹配第一个括号内容
        for (Paragraphs paragraph : paragraphs) {
            Matcher matcher = pattern1.matcher(paragraph.getParagraph());
            issue = matcher.find() ? matcher.group(1) : ""; // 返回带括号的内容
        }
        return paragraphs;
    }
}
