package com.yuntool;

public class DocumentEntry {
    private String title;
    private String content;

    public DocumentEntry(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
} 