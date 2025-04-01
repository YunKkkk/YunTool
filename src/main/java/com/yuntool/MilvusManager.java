package com.yuntool;

import com.google.gson.JsonObject;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.rbac.request.CreateRoleReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DropCollectionReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.database.request.DropDatabaseReq;
import io.milvus.v2.service.database.response.ListDatabasesResp;
import io.milvus.v2.service.vector.request.*;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.GetResp;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.QueryResp;
import io.milvus.v2.service.vector.response.SearchResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static com.alibaba.dashscope.utils.JsonUtils.gson;

@Component
public class MilvusManager {
    private final MilvusClientV2 client;

    @Autowired
    public MilvusManager(MilvusConfig config) {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri("http://localhost:19530")
                .token("root:Milvus")
                .build();
        this.client = new MilvusClientV2(connectConfig);
    }

    /**
     * 创建一个新的数据库。
     * @param databaseName 要创建的数据库名称。
     */
    public void createDatabase(String databaseName) {
        CreateDatabaseReq createDatabaseReq = CreateDatabaseReq.builder()
                .databaseName(databaseName)
                .build();
        client.createDatabase(createDatabaseReq);
    }

    /**
     * 删除一个现有的数据库。
     * @param databaseName 要删除的数据库名称。
     */
    public void dropDatabase(String databaseName) {
        DropDatabaseReq dropDatabaseReq = DropDatabaseReq.builder()
                .databaseName(databaseName)
                .build();
        client.dropDatabase(dropDatabaseReq);
    }

    /**
     * 列出所有数据库的名称。
     * @return 数据库名称的列表。
     */
    public List<String> listDatabases() {
        ListDatabasesResp listDatabasesResp = client.listDatabases();
        return listDatabasesResp.getDatabaseNames();
    }

    /**
     * 创建一个新的集合。
     * @param collectionName 要创建的集合名称。
     * @param dimension 向量的维度。
     */
    public void createCollection(String collectionName, int dimension) {
        CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                .collectionName(collectionName)
                .dimension(dimension)
                .build();
        client.createCollection(createCollectionReq);
    }

    /**
     * 删除一个现有的集合。
     * @param collectionName 要删除的集合名称。
     */
    public void dropCollection(String collectionName) {
        DropCollectionReq dropCollectionReq = DropCollectionReq.builder()
                .collectionName(collectionName)
                .build();
        client.dropCollection(dropCollectionReq);
    }

    /**
     * 创建一个新的角色。
     * @param roleName 要创建的角色名称。
     */
    public void createRole(String roleName) {
        client.createRole(CreateRoleReq.builder()
                .roleName(roleName)
                .build());
    }

    /**
     * 在指定集合中插入数据。
     * @param collectionName 集合名称。
     * @param data 要插入的数据。
     */
    public void insertData(String collectionName, List<JsonObject> data) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        InsertResp insert = client.insert(insertReq);
        System.out.println(insert.getInsertCnt());
        System.out.println(insert.getPrimaryKeys());
    }

    /**
     * 查询集合中的数据。
     * @param collectionName 集合名称。
     * @param filter 查询过滤条件。
     */
    public void queryCollection(String collectionName, String filter) {
        QueryReq queryReq = QueryReq.builder()
                .collectionName(collectionName)
                .filter(filter)
                .build();
        QueryResp queryResp = client.query(queryReq);
        for (QueryResp.QueryResult result : queryResp.getQueryResults()) {
            System.out.println(result.getEntity());
        }
    }

    /**
     * 获取特定实体。
     * @param collectionName 集合名称。
     * @param ids 实体ID列表。
     */
    public void getEntity(String collectionName, List<Object> ids) {
        GetReq getReq = GetReq.builder()
                .collectionName(collectionName)
                .ids(ids)
                .build();
        GetResp getResp = client.get(getReq);

        for (QueryResp.QueryResult result : getResp.getGetResults()) {
            System.out.println(result.getEntity());
        }
    }

    /**
     * 删除集合中的实体。
     * @param collectionName 集合名称。
     * @param filter 删除过滤条件。
     */
    public void deleteEntities(String collectionName, String filter) {
        DeleteReq deleteReq = DeleteReq.builder()
                .collectionName(collectionName)
                .filter(filter)
                .build();
        client.delete(deleteReq);
    }

    /**
     * 在集合中插入或更新数据。
     * @param collectionName 集合名称。
     * @param data 要插入或更新的数据。
     */
    public void upsertData(String collectionName, List<JsonObject> data) {
        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        client.upsert(upsertReq);
    }

    /**
     * 插入文档条目到Milvus。
     * @param entries 文档条目列表。
     */
    public void insertDocumentEntries(List<DocumentEntry> entries, String collectionName) {
        List<JsonObject> data = new ArrayList<>();
        long idCounter = 1; // 用于生成唯一的ID
        for (DocumentEntry entry : entries) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", idCounter++); // 添加ID字段
            jsonObject.addProperty("title", entry.getTitle());
            List<Float> vector = convertTitleToVector(entry.getTitle());
            jsonObject.add("vector", gson.toJsonTree(vector));
            jsonObject.addProperty("content", entry.getContent());
            data.add(jsonObject);
        }
        InsertResp insert = client.insert(InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build());
        System.out.println("insert" + insert.getInsertCnt());
    }

    /**
     * 搜索标题。
     *
     * @param title 标题。
     * @return
     */
    public SearchResp searchByTitle(String title, String collectionName) {
        List<Float> queryVector = convertTitleToVector(title);
        return client.search(SearchReq.builder()
                .collectionName(collectionName)
                .data(Collections.singletonList(new FloatVec(queryVector)))
                .topK(10)
                .outputFields(Arrays.asList("title", "content")) // 指定输出字段
                .build());
    }

    /**
     * 将标题转换为向量。
     * @param title 标题。
     * @return 向量。
     */
    public List<Float> convertTitleToVector(String title) {
        // 使用预训练模型将标题转换为向量
        List<Float> vector = new ArrayList<>();
        for (String word : title.split(" ")) {
            List<Float> wordVector = getWordVector(word);
            for (int i = 0; i < wordVector.size(); i++) {
                if (vector.size() <= i) {
                    vector.add(wordVector.get(i));
                } else {
                    vector.set(i, vector.get(i) + wordVector.get(i));
                }
            }
        }
        return vector;
    }

    private List<Float> getWordVector(String word) {
        // 假设每个词的向量维度是128
        return new ArrayList<>(Collections.nCopies(128, 1.0f));
    }

    /**
     * 关闭Milvus客户端连接。
     */
    public void close() {
        client.close();
    }
} 