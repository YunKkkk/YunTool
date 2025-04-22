package com.yuntool.tool.pdfutils;

import lombok.Data;

@Data
public class Paragraphs {
    //段落
    private StringBuilder paragraph = new StringBuilder();
    //标题
    private StringBuilder title = new StringBuilder();
    //单位
    private String unit;

    public void addParagraph(String text) {
        paragraph.append(text);
    }

    public void addTitle(String text) {
        title.append(text);
    }
}
