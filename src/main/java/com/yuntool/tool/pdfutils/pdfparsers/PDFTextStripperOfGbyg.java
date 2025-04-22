package com.yuntool.tool.pdfutils.pdfparsers;

import com.yuntool.tool.pdfutils.MyPDFTextStripper;
import com.yuntool.tool.pdfutils.Paragraphs;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.TextPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * 按照段落拆分，提取标题、来源单位
 *
 * @date 2025/2/20 14:42
 */
@Data
public class PDFTextStripperOfGbyg extends MyPDFTextStripper {
    //段落
    private List<Paragraphs> paragraphs = new ArrayList<>();
    //期号
    private String issue;

    private Paragraphs currentParagraph = new Paragraphs();

    //上一字符信息
    private double previousY = -1;
    private String previousStr = "";



    public PDFTextStripperOfGbyg() throws Exception {
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
        String unicode = text.getUnicode();

        // 添加当前字符到当前段落
        currentParagraph.addParagraph(unicode);
        if (text.getFontSize() > 400) {
            currentParagraph.addTitle(unicode);
        }


    }



    public List<Paragraphs> getParagraphs() {
        if (!currentParagraph.getParagraph().toString().isEmpty()) {
            paragraphs.add(currentParagraph);
            currentParagraph = new Paragraphs();
        }
        return paragraphs;
    }
}
