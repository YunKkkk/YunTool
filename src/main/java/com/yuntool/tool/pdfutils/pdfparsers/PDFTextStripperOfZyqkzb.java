package com.yuntool.tool.pdfutils.pdfparsers;

import com.yuntool.tool.pdfutils.MyPDFTextStripper;
import com.yuntool.tool.pdfutils.Paragraphs;
import lombok.Data;
import org.apache.pdfbox.text.TextPosition;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 按照段落拆分，提取标题、来源单位
 *
 * @date 2025/2/20 14:42
 */
@Data
public class PDFTextStripperOfZyqkzb extends MyPDFTextStripper {
    //段落
    private List<Paragraphs> paragraphs = new ArrayList<>();

    private Paragraphs currentParagraph = new Paragraphs();

    boolean startTitle = false;

    public PDFTextStripperOfZyqkzb() throws Exception {
        super();
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        super.processTextPosition(text);
        //坐标
        float currentY = text.getY();
        if (currentY > 760) {
            return;
        }

        // 获取当前字符的字体名称
        float fontSize = text.getFontSize();
        String fontName = text.getFont().getName();
        String unicode = text.getUnicode();

//        System.out.println("text: " + text);
//        System.out.println("text.getFont(): " + fontName);
//        System.out.println("cY: " + currentY);
//        System.out.println("cX: " + currentX);
//        System.out.println(fontSize);

        if (fontSize > 17.5) {
            //标题开始
            if (!startTitle) {
                paragraphs.add(currentParagraph);
                currentParagraph = new Paragraphs();
            }
            startTitle = true;
        } else {
            startTitle = false;
        }

        // 添加当前字符到当前段落
        currentParagraph.addParagraph(unicode);
        if (startTitle) {
            currentParagraph.addTitle(unicode);
        }

    }


    /**
     * 返回段落
     */
    public List<Paragraphs> getParagraphs() {
        if (!currentParagraph.getParagraph().toString().isEmpty()) {
            paragraphs.add(currentParagraph);
            currentParagraph = new Paragraphs();
        }
        //提取单位
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)(?!.*\\().*");// 匹配最后一个括号内容
        Pattern pattern1 = Pattern.compile("摘自:(.*)");// 匹配摘自:
        for (Paragraphs paragraph : paragraphs) {
            Matcher matcher = pattern.matcher(paragraph.getParagraph());
            Matcher matcher1 = pattern1.matcher(paragraph.getParagraph());
            String unit = matcher.find() ? matcher.group(1) : ""; // 返回带括号的内容
            unit = matcher1.find() ? matcher1.group(1) : unit;
            paragraph.setUnit(unit);
        }

        return paragraphs.stream()
                .filter(p -> {
                    String title = Objects.toString(p.getTitle(), "");
                    return !(title.replaceAll("\\s+", "").contains("目录")
                            || title.replaceAll("\\s+", "").contains("重要情况专报")
                            || title.isEmpty());
                })
                .collect(Collectors.toList());
    }
}
