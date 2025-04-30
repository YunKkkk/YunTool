package com.yuntool.tool.pdfutils;

import com.yuntool.tool.pdfutils.pdfparsers.*;

public class PDFTextStripperFactory {
    public static MyPDFTextStripper getStripper(String type) throws Exception {
        return switch (type) {
            case "Paragraphs" -> new PDFTextStripperOfParagraphs();
            case "BigTitle" -> new PDFTextStripperOfBigTitle();
            case "Zyqkzb" -> new PDFTextStripperOfZyqkzb();
            case "Gbyg" -> new PDFTextStripperOfGbyg();
            case "Hngzdt" -> new PDFTextStripperOfHngzdt();
            default -> throw new IllegalArgumentException("Unknown type");
        };
    }
}
