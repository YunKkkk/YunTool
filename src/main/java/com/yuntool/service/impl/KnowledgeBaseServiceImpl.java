package com.yuntool.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KnowledgeBaseServiceImpl {

    @Value("${dify.api.url}")
    private String apiUrl;

    @Value("${dify.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public KnowledgeBaseServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public ResponseEntity<String> createKnowledgeBase(Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> getKnowledgeBaseList(int page, int limit) {
        String url = apiUrl + "/datasets?page=" + page + "&limit=" + limit;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> deleteKnowledgeBase(String datasetId) {
        String url = apiUrl + "/datasets/" + datasetId;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    // 更新文档（使用文本）
    public ResponseEntity<String> updateDocumentByText(String datasetId, String documentId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/update-by-text";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 更新文档（使用文件）
    public ResponseEntity<String> updateDocumentByFile(String datasetId, String documentId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/update-by-file";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 获取文档嵌入状态
    public ResponseEntity<String> getDocumentEmbeddingStatus(String datasetId, String batch) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + batch + "/indexing-status";
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // 删除文档
    public ResponseEntity<String> deleteDocument(String datasetId, String documentId) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    // 获取知识库文档列表
    public ResponseEntity<String> getDocumentList(String datasetId) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents";
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // 向文档添加块
    public ResponseEntity<String> addChunksToDocument(String datasetId, String documentId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/segments";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 从文档中获取块
    public ResponseEntity<String> getChunksFromDocument(String datasetId, String documentId) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/segments";
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // 删除文档中的某个块
    public ResponseEntity<String> deleteChunkInDocument(String datasetId, String documentId, String segmentId) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/segments/" + segmentId;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    // 更新文档中的某个块
    public ResponseEntity<String> updateChunkInDocument(String datasetId, String documentId, String segmentId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/documents/" + documentId + "/segments/" + segmentId;
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 从知识库中检索块
    public ResponseEntity<String> retrieveChunksFromKnowledgeBase(String datasetId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/retrieve";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // 添加文档到知识库
    public ResponseEntity<String> addDocumentToKnowledgeBase(String datasetId, Map<String, Object> requestBody) {
        String url = apiUrl + "/datasets/" + datasetId + "/document/create-by-text";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
} 