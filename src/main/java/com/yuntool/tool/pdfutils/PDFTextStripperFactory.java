package com.yuntool.tool.pdfutils;

import com.yuntool.tool.pdfutils.pdfparsers.*;

public class PDFTextStripperFactory {
    public static MyPDFTextStripper getStripper(String type) throws Exception {
        switch (type) {
            case "Paragraphs":
                return new PDFTextStripperOfParagraphs();
            case "BigTitle":
                return new PDFTextStripperOfBigTitle();
            case "Zyqkzb":
                return new PDFTextStripperOfZyqkzb();
            case "Gbyg":
                return new PDFTextStripperOfGbyg();
            case "Hngzdt":
                return new PDFTextStripperOfHngzdt();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }
}
