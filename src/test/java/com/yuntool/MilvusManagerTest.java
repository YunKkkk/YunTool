package com.yuntool;

import com.google.gson.JsonObject;
import io.milvus.param.collection.FieldType;
import io.milvus.v2.service.vector.response.SearchResp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alibaba.dashscope.utils.JsonUtils.gson;

@SpringBootTest
public class MilvusManagerTest {

    @Autowired
    private MilvusManager milvusManager;

    @Test
    public void testCreateAndDropDatabase() {
        String databaseName = "test_database";
        Assertions.assertDoesNotThrow(() -> milvusManager.createDatabase(databaseName));

        List<String> databases = milvusManager.listDatabases();
        for (String database : databases) {
            System.out.println(database);
        }
//        Assertions.assertTrue(databases.contains(databaseName));
//
//        Assertions.assertDoesNotThrow(() -> milvusManager.dropDatabase(databaseName));
//
//        databases = milvusManager.listDatabases();
//        Assertions.assertFalse(databases.contains(databaseName));
    }

    @Test
    public void testCreateAndDropCollection() {
        String collectionName = "test_collection";
        int dimension = 128;
        Assertions.assertDoesNotThrow(() -> milvusManager.createCollection(collectionName, dimension));

        // Assuming a method to list collections exists
        // List<String> collections = milvusManager.listCollections();
        // Assertions.assertTrue(collections.contains(collectionName));

//        Assertions.assertDoesNotThrow(() -> milvusManager.dropCollection(collectionName));

        // collections = milvusManager.listCollections();
        // Assertions.assertFalse(collections.contains(collectionName));
    }

    @Test
    public void testCreateRole() {
        String roleName = "read_only";
        Assertions.assertDoesNotThrow(() -> milvusManager.createRole(roleName));
    }



    @Test
    public void testInsertData() {
        String collectionName = "test_collection";
        List<JsonObject> data = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 2L);

        // 创建一个128维的向量
        List<Float> vector = new ArrayList<>(Collections.nCopies(128, 1.0f));
        jsonObject.add("vector", gson.toJsonTree(vector));

        data.add(jsonObject);
        Assertions.assertDoesNotThrow(() -> milvusManager.insertData(collectionName, data));
    }

    @Test
    public void testQueryCollection() {
        String collectionName = "test_collection";
        String filter = "id < 10";
        Assertions.assertDoesNotThrow(() -> milvusManager.queryCollection(collectionName, filter));
    }

    @Test
    public void testGetEntity() {
        String collectionName = "test_collection";
        List<Object> ids = Collections.singletonList(1L);
        Assertions.assertDoesNotThrow(() -> milvusManager.getEntity(collectionName, ids));
    }

    @Test
    public void testDeleteEntities() {
        String collectionName = "test_collection";
        String filter = "id > 10";
        Assertions.assertDoesNotThrow(() -> milvusManager.deleteEntities(collectionName, filter));
    }

    @Test
    public void testUpsertData() {
        String collectionName = "test_collection";
        List<JsonObject> data = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 1L);
        jsonObject.add("vector", gson.toJsonTree(Collections.singletonList(2.0f)));
        data.add(jsonObject);
        Assertions.assertDoesNotThrow(() -> milvusManager.upsertData(collectionName, data));
    }

    @Test
    public void testInsertAndSearchDocumentEntries() {
        List<DocumentEntry> entries = new ArrayList<>();
        entries.add(new DocumentEntry("党建管理系统", "工作计划..."));
        entries.add(new DocumentEntry("目标", "年度目标分类设置..."));

        Assertions.assertDoesNotThrow(() -> milvusManager.insertDocumentEntries(entries, "test_collection"));

        SearchResp searchResp = Assertions.assertDoesNotThrow(() -> milvusManager.searchByTitle("党建管理系统", "test_collection"));
        for (List<SearchResp.SearchResult> searchResult : searchResp.getSearchResults()) {
            System.out.println(searchResult.toString());
        }

    }

    @Test
    public void searchDocumentEntries() {
        SearchResp searchResp = Assertions.assertDoesNotThrow(() -> milvusManager.searchByTitle("党建管理系统", "test_collection"));
        for (List<SearchResp.SearchResult> searchResult : searchResp.getSearchResults()) {
            System.out.println(searchResult.toString());
        }

    }
} 