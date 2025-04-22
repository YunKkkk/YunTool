package com.yuntool.tool.pdfutils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPDFTextStripper extends PDFTextStripper {
    public List<Paragraphs> getParagraphs(){
        return new ArrayList<>();
    }
    public String getIssue() {
        return "";
    }

    public String getText(PDDocument doc) throws IOException {
        return super.getText(doc);
    }

    public Boolean getIsTheCurrentMode() {
        return true;
    }
}
