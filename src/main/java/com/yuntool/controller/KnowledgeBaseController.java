package com.yuntool.controller;

import com.yuntool.service.impl.KnowledgeBaseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeBaseController {

    private final KnowledgeBaseServiceImpl knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseServiceImpl knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @PostMapping
    public ResponseEntity<String> createKnowledgeBase(@RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.createKnowledgeBase(requestBody);
    }

    @PostMapping("/create-empty")
    public ResponseEntity<String> createEmptyKnowledgeBase(@RequestParam String name, @RequestParam(required = false) String description) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        if (description != null) {
            requestBody.put("description", description);
        }
        requestBody.put("permission", "only_me"); // 设置权限为仅自己可见

        return knowledgeBaseService.createKnowledgeBase(requestBody);
    }

    @GetMapping
    public ResponseEntity<String> getKnowledgeBaseList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        return knowledgeBaseService.getKnowledgeBaseList(page, limit);
    }

    @DeleteMapping("/{datasetId}")
    public ResponseEntity<String> deleteKnowledgeBase(@PathVariable String datasetId) {
        return knowledgeBaseService.deleteKnowledgeBase(datasetId);
    }

    // 更新文档（使用文本）
    @PostMapping("/{datasetId}/documents/{documentId}/update-by-text")
    public ResponseEntity<String> updateDocumentByText(@PathVariable String datasetId, @PathVariable String documentId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.updateDocumentByText(datasetId, documentId, requestBody);
    }

    // 更新文档（使用文件）
    @PostMapping("/{datasetId}/documents/{documentId}/update-by-file")
    public ResponseEntity<String> updateDocumentByFile(@PathVariable String datasetId, @PathVariable String documentId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.updateDocumentByFile(datasetId, documentId, requestBody);
    }

    // 获取文档嵌入状态
    @GetMapping("/{datasetId}/documents/{batch}/indexing-status")
    public ResponseEntity<String> getDocumentEmbeddingStatus(@PathVariable String datasetId, @PathVariable String batch) {
        return knowledgeBaseService.getDocumentEmbeddingStatus(datasetId, batch);
    }

    // 删除文档
    @DeleteMapping("/{datasetId}/documents/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable String datasetId, @PathVariable String documentId) {
        return knowledgeBaseService.deleteDocument(datasetId, documentId);
    }

    // 获取知识库文档列表
    @GetMapping("/{datasetId}/documents")
    public ResponseEntity<String> getDocumentList(@PathVariable String datasetId) {
        return knowledgeBaseService.getDocumentList(datasetId);
    }

    // 向文档添加块
    @PostMapping("/{datasetId}/documents/{documentId}/segments")
    public ResponseEntity<String> addChunksToDocument(@PathVariable String datasetId, @PathVariable String documentId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.addChunksToDocument(datasetId, documentId, requestBody);
    }

    // 从文档中获取块
    @GetMapping("/{datasetId}/documents/{documentId}/segments")
    public ResponseEntity<String> getChunksFromDocument(@PathVariable String datasetId, @PathVariable String documentId) {
        return knowledgeBaseService.getChunksFromDocument(datasetId, documentId);
    }

    // 删除文档中的某个块
    @DeleteMapping("/{datasetId}/documents/{documentId}/segments/{segmentId}")
    public ResponseEntity<String> deleteChunkInDocument(@PathVariable String datasetId, @PathVariable String documentId, @PathVariable String segmentId) {
        return knowledgeBaseService.deleteChunkInDocument(datasetId, documentId, segmentId);
    }

    // 更新文档中的某个块
    @PostMapping("/{datasetId}/documents/{documentId}/segments/{segmentId}")
    public ResponseEntity<String> updateChunkInDocument(@PathVariable String datasetId, @PathVariable String documentId, @PathVariable String segmentId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.updateChunkInDocument(datasetId, documentId, segmentId, requestBody);
    }

    // 从知识库中检索块
    @PostMapping("/{datasetId}/retrieve")
    public ResponseEntity<String> retrieveChunksFromKnowledgeBase(@PathVariable String datasetId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.retrieveChunksFromKnowledgeBase(datasetId, requestBody);
    }

    // 新增文档
    @PostMapping("/{datasetId}/documents")
    public ResponseEntity<String> addDocumentToKnowledgeBase(@PathVariable String datasetId, @RequestBody Map<String, Object> requestBody) {
        return knowledgeBaseService.addDocumentToKnowledgeBase(datasetId, requestBody);
    }

    // 其他API端点可以根据需要添加
} 