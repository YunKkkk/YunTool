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
public class PDFTextStripperOfHngzdt extends MyPDFTextStripper {
    //段落
    private List<Paragraphs> paragraphs = new ArrayList<>();
    //期号
    private String issue;

    private Paragraphs currentParagraph = new Paragraphs();

    private double maxLeft = 1000;

    private Boolean isTheCurrentMode = true;

    //上一字符信息
    private double previousX = -1;
    private double previousY = -1;
    private String previousStr = "";
    private boolean previousIsBold = true;

    //计算正文中前一百个字符有多少种行间距，如果是大标题模式，则会在正文中前一百个字符内产生最少三种行间距
    boolean startMainBody = false;
    private double LineSpacing = 0;
    private Set<Integer> LineSpacingSet = new HashSet<>();
    private int num = 1;

    private Pattern BOLD_FONT_PATTERN = Pattern.compile("");

    //自动识别粗体字符
    private boolean font1 = true;
    private boolean font2 = true;
    private boolean font3 = false;
    private String fontStr = "";


    public PDFTextStripperOfHngzdt() throws Exception {
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


        if (maxLeft > currentX) {
            maxLeft = currentX;
        }
        if (!isTheCurrentMode) {
            return;
        }

        // 获取当前字符的字体名称
        float fontSize = text.getFontSize();
        String fontName = text.getFont().getName();
        String unicode = text.getUnicode();


        //动态更新加粗字体
        updateBoldFont(fontName, unicode, currentY, currentX);

        // 检查字体是否加粗
        boolean isBold = isBold(fontName, unicode);

        System.out.println("text: " + text);
//        System.out.println("text.getFont(): " + fontName);
//        System.out.println("cY: " + currentY);
//        System.out.println("cX: " + currentX);
        System.out.println(fontSize);

        //提取段落
        if (Math.abs(currentY - previousY) > 10 && Math.abs(currentX - maxLeft) > 30 && isBold) { // 新的一行时，起始位置不在最左侧
            paragraphs.add(currentParagraph);
            currentParagraph = new Paragraphs();
            previousIsBold = true;
        }

        //提取段落标题
        if (isBold && previousIsBold) {
            currentParagraph.addTitle(unicode);
        } else {
            previousIsBold = false;
        }

        // 添加当前字符到当前段落
        currentParagraph.addParagraph(unicode);

        // 更新前一个字符的坐标
        if (!unicode.equals(" ")) {
            previousX = currentX;
            previousY = currentY;
            previousStr = unicode;
        }
    }



    /**
     * 动态更新加粗字体
     */
    private void updateBoldFont(String fontName, String unicode, float currentY, float currentX) {

        //font1每个文件的第一个字--内
        if (font1) {
            String[] split = fontName.split("\\+");
            fontStr = split[0];
            BOLD_FONT_PATTERN = Pattern.compile(fontStr);
            font1 = false;
        }

        //font2每个文件的第一个左括号--(
        if (font2 && unicode.equals("(")) {
            String[] split = fontName.split("\\+");
            fontStr += "|" + split[0];
            BOLD_FONT_PATTERN = Pattern.compile(fontStr);
            font2 = false;
        }

        //font3每一段的第一行，有空格时跳过
        if (unicode.equals(" ") || Math.abs(currentY - previousY) > 10) {
            font3 = false;
        }
        //小于45是为了排除掉文件最下方的送某某某
        if (Math.abs(currentY - previousY) > 10 && Math.abs(currentX - maxLeft) > 30 && Math.abs(currentX - maxLeft) < 45 && !"政务要闻府工作快报".contains(unicode)) { // 新的一行时，起始位置不在最左侧
            font3 = true;
        }
        if (font3) {
            String[] split = fontName.split("\\+");
            if (!fontStr.contains(split[0])) {
                fontStr += "|" + split[0];
            }
            BOLD_FONT_PATTERN = Pattern.compile(fontStr);
        }
    }


    /**
     * 判断是否为粗体
     */
    private boolean isBold(String fontName, String unicode) {
        if (fontName == null || unicode.equals(" ")) {
            return false;
        }
        return BOLD_FONT_PATTERN.matcher(fontName).find();
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
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)\\s*$");// 匹配最后一个括号内容
        for (Paragraphs paragraph : paragraphs) {
            Matcher matcher = pattern.matcher(paragraph.getParagraph());
            String unit = matcher.find() ? matcher.group(1) : ""; // 返回带括号的内容
            paragraph.setUnit(unit);
        }

        //提取期号
        Pattern pattern1 = Pattern.compile("\\((\\d+)\\)");
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraphs paragraph1 = paragraphs.get(i);
            Matcher matcher = pattern1.matcher(paragraph1.getTitle());
            if (matcher.find()) {
                issue = matcher.group(1);
                break;
            }
        }
        return paragraphs.stream()
                .filter(p -> {
                    String title = Objects.toString(p.getTitle(), "");
                    return !(title.contains("河南工作动态") || pattern1.matcher(title).find());
                })
                .collect(Collectors.toList());
    }
}
