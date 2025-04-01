# 创建数据库（）

此操作创建一个具有名称的数据库。

```java
public void createDatabase(CreateDatabaseReq request)
```

## 请求语法

```java
createDatabase(CreateDatabaseReq.builder()
    .databaseName(String databaseName)
    .build()
)
```

**建造者方法：**

- `databaseName(String databaseName)`

  要创建的数据库的名称。


**返回：**

_空白_

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
CreateDatabaseReq createDatabaseReq = CreateDatabaseReq.builder()
        .databaseName(databaseName)
        .build();
client.createDatabase(createDatabaseReq);
```

# 删除数据库（）

此操作删除一个具有名称的数据库。

```java
public void dropDatabase(DropDatabaseReq request)
```

## 请求语法

```java
dropDatabase(DropDatabaseReq.builder()
    .databaseName(String databaseName)
    .build()
)
```

**建造者方法：**

- `databaseName(String databaseName)`

  要删除的数据库的名称。


**返回：**

_空白_

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
DropDatabaseReq dropDatabaseReq = DropDatabaseReq.builder()
        .databaseName(databaseName)
        .build();
client.dropDatabase(dropDatabaseReq);
```

# 列出数据库（）

此操作列出所有数据库名称。

```java
public ListDatabasesResp listDatabases()
```

**退货类型：**

_列出数据库响应_

**返回：**

ListDatabasesResp 对象包含所有数据库名称的列表。

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
ListDatabasesResp listDatabasesResp = client.listDatabases();
List<String> dbNames = listDatabasesResp.getDatabaseNames();
```

# 创建集合（）

此操作将创建一个具有默认或自定义设置的集合。

```java
public void createCollection(CreateCollectionReq request)
```

## 请求语法

```java
createCollection(CreateCollectionReq.builder()
    .collectionName(String collectionName)
    .description(String collectionDescription)
    .dimension(int dimension)
    .primaryFieldName(String primaryFieldName)
    .idType(DataType datatype)
    .maxLength(int maxLength)
    .vectorFieldName(String vectorFieldName)
    .metricType(String metricType)
    .autoID(boolean autoID)
    .enableDynamicField(boolean enableDynamicField)
    .numShards(int numShards)
    .collectionSchema(CreateCollectionReq.CollectionSchema collectionSchema)
    .indexParams(List<IndexParam> indexParams)
    .numPartitions(int numPartitions)
    .build()
)
```

**建造者方法：**

- `collectionName(String collectionName)`

  要创建的集合的名称。

- `description(String collectionDescription)`

  集合的描述，默认为空。

- `dimension(int dimension)`

  保存向量嵌入的集合字段的维数。

  该值应大于 1，通常由用于生成向量嵌入的模型决定。

  这是使用默认设置设置集合所必需的。如果您需要使用自定义架构设置集合，请跳过此参数。

- `primaryFieldName(String primaryFieldName)`

  此集合中主要字段的名称。

  该值默认为**id**。您可以使用其他合适的名称。如果您需要设置具有自定义架构的集合，请跳过此参数。

- `idType(DataType idType)`

  此集合中主要字段的数据类型。

  该值默认为**DataType.Int64**。如果您需要设置具有自定义架构的集合，请跳过此参数。

  如果需要设置具有自定义架构的集合，请跳过此参数。

- `maxLength(int maxLength)`

  集合内字符串或数组字段允许的最大字符数或元素数。

  **如果primaryFieldType**设置为**VarChar** ，则此参数是必需的。

  该值默认为**65535**。

- `vectorFieldName(String vectorFieldName)`

  保存向量嵌入的集合字段的名称。

  该值默认为**vector**。您可以使用其他合适的名称。如果您需要设置具有自定义架构的集合，请跳过此参数。

- `metricType(String metricType)`

  该集合使用的算法来测量向量嵌入之间的相似性。

  该值默认为**IP**。可能的值是**L2**、**IP**和**COSINE**。有关这些指标类型的详细信息，请参阅[相似性指标](https://milvus.io/docs/metric.md)。

- `autoID(boolean autoID)`

  当数据插入到该集合时，主字段是否自动递增。

  该值默认为**False**。将其设置为**True**会使主字段自动递增。如果您需要设置具有自定义架构的集合，请跳过此参数。

- `enableDynamicField(boolean enableDynamicField)`

  **是否使用名为$meta**的保留 JSON 字段以键值对的形式存储未定义的字段及其值。

  该值默认为**True**，表示使用元字段。

  如果创建具有架构的集合，请使用**[createSchema](https://milvus.io/api-reference/java/v2.3.x/v2/Collections/createSchema.md)**方法配置此参数。

- `numShards(int numShards)`

  与收集一起创建的碎片数量。

  该值默认为**1**，表示将与此集合一起创建一个分片。

  **什么是分片？**

  分片是指将写入操作分散到不同的节点，以充分利用 Milvus 集群写入数据的并行计算潜力。

  默认情况下，一个集合包含一个分片。

- `collectionSchema(CreateCollectionReq.CollectionSchema collectionSchema)`

  此集合的架构。

  将其留空表示将使用默认设置创建此集合。要设置具有自定义架构的集合，您需要创建一个**CollectionSchema**对象并在此处引用它。

- `indexParams(List<IndexParam> indexParams)`

  用于在此集合中的矢量字段上构建索引的参数。要设置具有自定义架构的集合并自动将集合加载到内存，请使用 IndexParam 对象列表创建一个[IndexParams](https://milvus.io/api-reference/java/v2.3.x/v2/Management/IndexParam.md)对象**并**在此处引用它。[](https://milvus.io/api-reference/java/v2.3.x/v2/Management/IndexParam.md)

  您至少应该为该集合中的矢量字段添加一个索引。如果您希望稍后设置索引参数，也可以跳过此参数。

- `numPartitions(int numPartitions)`

  分区数。当 Field Schema 中的 isPartitionKey 设置为 true 时使用。默认值为 64。


**返回：**

_空白_

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

### 创建集合

您可以选择快速设置或自定义设置，如下所示：

- **快速设置**

  快速设置集合有两个字段：主字段和向量字段。它还允许在动态字段中插入未定义的字段及其键值对中的值。


```java
// quickly create a collection
CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
        .collectionName(collectionName)
        .dimension(dim)
        .build();
client.createCollection(createCollectionReq);

```

- **使用索引参数进行自定义设置**

  对于定制设置，请预先创建架构和索引参数。


```java
// create a collection with schema, when indexParams is specified, it will create index as well
CreateCollectionReq.CollectionSchema collectionSchema = client.createSchema();
collectionSchema.addField(AddFieldReq.builder().fieldName("id").dataType(DataType.Int64).isPrimaryKey(Boolean.TRUE).autoID(Boolean.FALSE).description("id").build());
        collectionSchema.addField(AddFieldReq.builder().fieldName("vector").dataType(DataType.FloatVector).dimension(dim).build());

IndexParam indexParam = IndexParam.builder()
        .fieldName("vector")
        .metricType(IndexParam.MetricType.COSINE)
        .build();
CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
        .collectionName(collectionName)
        .collectionSchema(collectionSchema)
        .indexParams(Collections.singletonList(indexParam))
        .build();
client.createCollection(createCollectionReq);
```

# 创建Schema（）

此操作会创建一个集合模式。

```java
public CreateCollectionReq.CollectionSchema createSchema()
```

## 请求语法

```java
MilvusClientV2.createSchema()
```

**参数：**

没有任何

**退货类型：**

_创建集合请求.集合模式_

**返回：**

**CreateCollectionReq.CollectionSchema**对象。

## 例子

```java
// quickly create a collectionSchema
CreateCollectionReq.CollectionSchema collectionSchema = client.createSchema();
collectionSchema.addField(AddFieldReq.builder().fieldName("id").dataType(DataType.Int64).isPrimaryKey(Boolean.TRUE).autoID(Boolean.FALSE).description("id").build());
        collectionSchema.addField(AddFieldReq.builder().fieldName("vector").dataType(DataType.FloatVector).dimension(dim).build());
```

# search()

This operation conducts a vector similarity search with an optional scalar filtering expression.

```java
public SearchResp search(SearchReq request)
```

## Request Syntax

```java
search(SearchReq.builder()
    .collectionName(String collectionName)
    .partitionNames(List<String> partitionNames)
    .annsField(String annsField)
    .topK(int topK)
    .filter(String filter)
    .outputFields(List<String> outputFields)
    .data(List<BaseVector> data)
    .offset(long offset)
    .limit(long limit)
    .roundDecimal(int roundDecimal)
    .searchParams(String searchParams)
    .guaranteeTimestamp(long guaranteeTimestamp)
    .gracefulTime(long gracefulTime)
    .consistencyLevel(ConsistencyLevel consistencyLevel)
    .ignoreGrowing(boolean ignoreGrowing)
    .build()
)
```

**BUILDER METHODS:**

- `collectionName(String collectionName)`

  The name of an existing collection.

- `partitionNames(List<String> partitionNames)`

  A list of partition names.

- `annsField(String annsField)`

  The name of the vector field, used when there is more than one vector field. If only one vector field exists, we will use it directly.

- `topK(int topK)`

  The number of records to return in the search result. This parameter uses the same syntax as the `limit` parameter, so you should only set one of them.

  You can use this parameter in combination with `offset` to enable pagination.

  The sum of this value and `offset` should be less than 16,384.

- `filter(String filter)`

  A scalar filtering condition to filter matching entities.

  You can set this parameter to an empty string to skip scalar filtering. To build a scalar filtering condition, refer to [Boolean Expression Rules](https://milvus.io/docs/boolean.md).

- `outputFields(List<String> outputFields)`

  A list of field names to include in each entity in return.

  The value defaults to **None**. If left unspecified, all fields are selected as the output fields.

- `data(List<BaseVector> data)`

  A list of vector embeddings.

  Milvus searches for the most similar vector embeddings to the specified ones.

  BaseVector is a base class for abstract vector classes. The following classes are derived from BaseVector. Choose the correct class as input according to DataType of the vector field.

  **notes**

  In Java SDK v2.3.7 or earlier versions, this method is named `distance`. Since Java SDK v2.3.8, this method is renamed as `score`.



-  offset(long offset)

   The number of records to skip in the query result.

   You can use this parameter in combination with `limit` to enable pagination.

   The sum of this value and `limit` should be less than 16,384.

- `limit(long limit)`

  The number of records to return in the search result. This parameter uses the same syntax as the `topK` parameter, so you should only set one of them.

  You can use this parameter in combination with `offset` to enable pagination.

  The sum of this value and `offset` should be less than 16,384.

- `roundDecimal(int roundDecimal)`

  The number of decimal places that Milvus rounds the calculated distances to.

  The value defaults to **-1**, indicating that Milvus skips rounding the calculated distances and returns the raw value.

- `searchParams(Map<String,Object> searchParams)`

  The parameter settings specific to this operation.

  - **metric_type** (String)

    The metric type applied to this operation. This should be the same as the one used when you index the vector field specified above.

    Possible values are **L2**, **IP**, and **COSINE**.

  - **radius** (float)

    Determines the threshold of least similarity. When setting `metric_type` to `L2`, ensure that this value is greater than that of **range_filter**. Otherwise, this value should be lower than that of **range_filter**.

  - **range_filter** (float)

    Refines the search to vectors within a specific similarity range. When setting `metric_type` to `IP` or `COSINE`, ensure that this value is greater than that of **radius**. Otherwise, this value should be lower than that of **radius**.


    For details on other applicable search parameters, refer to [In-memory Index](https://milvus.io/docs/index.md) and [On-disk Index](https://milvus.io/docs/disk_index.md).

- `guaranteeTimestamp(long guaranteeTimestamp)`

  A valid timestamp.

  If this parameter is set, MilvusZilliz Cloud executes the query only if all entities inserted before this timestamp are visible to query nodes.

  **notes**

  This parameter is valid when the default consistency level applies.

- `gracefulTime(long gracefulTime)`

  A period of time in ms.

  The value defaults to **5000L**. If this parameter is set, MilvusZilliz Cloud calculates the guarantee timestamp by subtracting this from the current timestamp.

  **notes**

  This parameter is valid when a consistency level other than the default one applies.

- `consistencyLevel(ConsistencyLevel consistencyLevel)`

  The consistency level of the target collection.

  The value defaults to the one specified when you create the current collection, with options of **Strong** (**0**), **Bounded** (**1**), **Session** (**2**), and **Eventually** (**3**).

  **what is the consistency level?**

  Consistency in a distributed database specifically refers to the property that ensures every node or replica has the same view of data when writing or reading data at a given time.

  Milvus supports four consistency levels: **Strong**, **Bounded Staleness**, **Session**, and **Eventually**. The default consistency level in Milvus is **Bounded Staleness**.

  You can easily tune the consistency level when conducting a vector similarity search or query to make it best suit your application.

- `ignoreGrowing(boolean ignoreGrowing)`

  Whether to ignore growing segments during similarity searches.


**RETURN TYPE:**

_SearchResp_

**RETURNS:**

A **SearchResp object representing specific search results with the specified output fields and relevance score.

**PARAMETERS:**
```

- searchResults(List<List<SearchResult>>)
    
    ```
    A list of SearchResp.SearchResult, the size of searchResults equals the number of query vectors of the search. Each List\<SearchResult\> is a topK result of a query vector. Each SearchResult represents an entity hit by the search.
    
    Member of SearchResult:
    ```
    

**EXCEPTIONS:**

- **MilvusClientExceptions**
    
    This exception will be raised when any error occurs during this operation.
    

## Example

```java
SearchResp searchR = client.search(SearchReq.builder()
        .collectionName(collectionName)
        .data(Collections.singletonList(new FloatVec(new float[]{1.0f, 2.0f})))
        .filter("id < 100")
        .topK(10)
        .outputFields(Collections.singletonList("*"))
        .build());
List<List<SearchResp.SearchResult>> searchResults = searchR.getSearchResults();

for (List<SearchResp.SearchResult> results : searchResults) {
    for (SearchResp.SearchResult result : results) {
        result.getEntity().toString());
    }
}

```



# query()

This operation conducts a scalar filtering with a specified boolean expression.

```java
public QueryResp query(QueryReq request)
```

## Request Syntax

```java
query(QueryReq.builder()
    .collectionName(String collectionName)
    .partitionNames(List<String> partitionNames)
    .outputFields(List<String> outputFields)
    .ids(List<Object> ids)
    .filter(String filter)
    .consistencyLevel(ConsistencyLevel consistencyLevel)
    .offset(long offset)
    .limit(long limit)
    .build()
)
```

**BUILDER METHODS:**

- `collectionName(String collectionName)`

  The name of an existing collection.

- `partitionNames(List<String> partitionNames)`

  A list of partition names.

- `outputFields(List<String> outputFields)`

  A list of field names to include in each entity in return.

  The value defaults to **None**. If left unspecified, all fields in the collection are selected as the output fields.

- `ids(List<Object> ids)`

  The IDs of entities to query.

- `filter(String filter)`

  A scalar filtering condition to filter matching entities.

  You can set this parameter to an empty string to skip scalar filtering. To build a scalar filtering condition, refer to [Boolean Expression Rules](https://milvus.io/docs/boolean.md).

- `consistencyLevel(ConsistencyLevel consistencyLevel)`

  The consistency level of the target collection.

  The value defaults to the one specified when you create the current collection, with options of **Strong** (**0**), **Bounded** (**1**), **Session** (**2**), and **Eventually** (**3**).

  **what is the consistency level?**

  Consistency in a distributed database specifically refers to the property that ensures every node or replica has the same view of data when writing or reading data at a given time.

  Milvus supports four consistency levels: **Strong**, **Bounded Staleness**, **Session**, and **Eventually**. The default consistency level in Milvus is **Bounded Staleness**.

  You can easily tune the consistency level when conducting a vector similarity search or query to make it best suit your application.

- `offset(long offset)`

  The number of records to skip in the query result.

  You can use this parameter in combination with `limit` to enable pagination.

  The sum of this value and `limit` should be less than 16,384.

- `limit(long limit)`

  The number of records to return in the query result.

  You can use this parameter in combination with `offset` to enable pagination.

  The sum of this value and `offset` should be less than 16,384.


**RETURN TYPE:**

_QueryResp_

**RETURNS:**

A **QueryResp object representing specific query results with the specified output fields

**PARAMETERS:**

- queryResults(List<QueryResp.QueryResult>)

A list of QueryResult objects with each QueryResult representing a queried entity.

**notes**

If the number of returned entities is less than expected, duplicate entities may exist in your collection.

**EXCEPTIONS:**

- **MilvusClientExceptions**

  This exception will be raised when any error occurs during this operation.


## Example

```java
//query by filter "id < 10"
QueryReq queryReq = QueryReq.builder()
        .collectionName("test")
        .filter("id < 10")
        .build();
QueryResp queryResp = client.query(queryReq);
for (QueryResp.QueryResult result : queryResp.getGetResults()) {
    System.out.println(result.getEntity());
}
```

# 插入（）

此操作将数据插入到特定集合中。

```java
public InsertResp insert(InsertReq request)
```

## 请求语法

```java
insert(InsertReq.builder()
    .collectionName(String collectionName)
    .data(List<JsonObject> data)
    .partitionName(String partitionName)
    .build()
)
```

**建造者方法：**

- `collectionName(String collectionName)`

  现有集合的名称。

- `data(List<JsonObject> data)`

  要插入到当前集合中的数据。

  要插入的数据应该与`gson.JsonObject`当前集合的模式或此类字典的列表相匹配。

  以下代码假设当前集合的模式有两个字段，分别为**id**和**vector**。前者是主要字段，后者是用于保存 5 维向量嵌入的字段。

  **笔记**

  在 Java SDK v2.3.7 及之前版本中，输入为`fastjson.JSONObject`。但`fastjson`由于存在不安全的反序列化漏洞，目前不建议使用 。因此，如果您使用 Java SDK v2.3.8 及之后版本，请将其替换`fastjson`为。`gson`

    ```java
    List<JsonObject> data = new ArrayList<>();
    
    JsonObject dict1 = new JsonObject();
    List<Float> vectorArray1 = new ArrayList<>();
    vectorArray1.add(0.37417449965222693);
    vectorArray1.add(-0.9401784221711342);
    vectorArray1.add(0.9197526367693833);
    vectorArray1.add(0.49519396415367245);
    vectorArray1.add(-0.558567588166478);
    
    dict1.addProperty("id", 1L);
    dict1.add("vector", gson.toJsonTree(vectorArray1));
    
    JsonObject dict2 = new JsonObject();
    List<Float> vectorArray2 = new ArrayList<>();
    vectorArray2.add(0.46949086179692356);
    vectorArray2.add(-0.533609076732849);
    vectorArray2.add(-0.8344432775467099);
    vectorArray2.add(0.9797361846081416);
    vectorArray2.add(0.6294256393761057);
    
    dict2.addProperty("id", 2L);
    dict2.add("vector", gson.toJsonTree(vectorArray2));
    
    data.add(dict1);
    data.add(dict2);
    ```

- `partitionName(String partitionName)`

  分区的名称。


**退货类型：**

_插入响应_

**返回：**

包含有关插入实体数量信息的**InsertResp**对象。

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
JsonObject vector = new JsonObject();
List<Float> vectorList = new ArrayList<>();
vectorList.add(1.0f);
vectorList.add(2.0f);
vector.add("vector", gson.toJsonTree(vectorList));
vector.addProperty("id", 0L);

InsertReq insertReq = InsertReq.builder()
        .collectionName("test")
        .data(Collections.singletonList(vector))
        .build();
client.insert(insertReq);
```

# 得到（）

此操作通过 ID 获取特定实体。

```java
public GetResp get(GetReq request)
```

## 请求语法

```java
get(GetReq.builder()
    .collectionName(String collectionName)
    .partitionName(String partitionName)
    .ids(List<Object> ids)
    .outputFields(List<String> outputFields)
    .build()
)
```

**建造者方法：**

- `collectionName(String collectionName)`

  现有集合的名称。

- `partitionName(String partitionName)`

  分区的名称。

- `ids(List<Object> ids)`

  特定实体 ID 或实体 ID 列表。

- `outputFields(List<String> outputFields)`

  查询结果中要包含的字段名称列表。


**退货类型：**

_获取响应_

**返回：**

**代表一个或多个查询实体的GetResp**对象。

**参数：**

- **获取结果**( _List <QueryResp.QueryResult>_ )

  **QueryResp.QueryResult**对象的列表。

- **字段**（_Map <String,Object>_）

  包含字段名称及其值的键值对的映射。


**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
// get entity with id 0
GetReq getReq = GetReq.builder()
        .collectionName("test")
        .ids(Collections.singletonList("0"))
        .build();
GetResp statusR = client.get(getReq);
```

# 删除（）

此操作通过实体的 ID 或布尔表达式删除实体。

```java
public DeleteResp delete(DeleteReq request)
```

## 请求语法

```java
delete(DeleteReq.builder()
    .collectionName(String collectionName)
    .partitionName(String partitionName)
    .filter(String filter)
    .ids(List<Object> ids)
    .build()
)
```

**建造者方法：**

- `collectionName(String collectionName)`

  现有集合的名称。

- `partitionName(String partitionName)`

  分区的名称。

- `filter(String filter)`

  用于过滤匹配实体的标量过滤条件。

  该值默认为空字符串，表示不适用任何条件。

  您可以将此参数设置为空字符串以跳过标量过滤。若要构建标量过滤条件，请参阅[标量表达式规则](https://milvus.io/docs/boolean.md)。

- `ids(List<Object> ids)`

  特定实体 ID 或实体 ID 列表。


**退货类型：**

_删除响应_

**返回：**

**DeleteResp**对象包含已删除实体的数量。

**参数：**

- **deleteCnt**（_长整型_）

  已删除实体的数量。


**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
// delete entities with filter "id > 10"
DeleteReq deleteReq = DeleteReq.builder()
        .collectionName("test")
        .filter("id > 10")
        .build();
client.delete(deleteReq);
```

# 插入()

此操作在特定集合中插入或更新数据。

```java
public UpsertResp upsert(UpsertReq request)
```

## 请求语法

```java
upsert(UpsertReq.builder()
    .data(List<JsonObject> data)
    .collectionName(String collectionName)
    .partitionName(String partitionName)
    .build()
)
```

**建造者方法：**

- `data(List<JsonObject> data)`

  要插入或更新到当前集合中的数据。

  要插入或更新的数据应该与`gson.JsonObject`当前集合的模式或此类字典的列表相匹配。

  以下代码假设当前集合的模式有两个字段，分别为**id**和**vector**。前者是主要字段，后者是用于保存 5 维向量嵌入的字段。

  **笔记**

  在 Java SDK v2.3.7 及之前版本中，输入为`fastjson.JSONObject`。但`fastjson`由于存在不安全的反序列化漏洞，目前不建议使用 。因此，如果您使用 Java SDK v2.3.8 及之后版本，请将其替换`fastjson`为。`gson`

    ```java
    List<JsonObject> data = new ArrayList<>();
    
    JsonObject dict1 = new JsonObject();
    List<Float> vectorArray1 = new ArrayList<>();
    vectorArray1.add(0.37417449965222693);
    vectorArray1.add(-0.9401784221711342);
    vectorArray1.add(0.9197526367693833);
    vectorArray1.add(0.49519396415367245);
    vectorArray1.add(-0.558567588166478);
    
    dict1.addProperty("id", 1L);
    dict1.add("vector", gson.toJsonTree(vectorArray1));
    
    JsonObject dict2 = new JsonObject();
    JSONArray vectorArray2 = new ArrayList<>();
    vectorArray2.add(0.46949086179692356);
    vectorArray2.add(-0.533609076732849);
    vectorArray2.add(-0.8344432775467099);
    vectorArray2.add(0.9797361846081416);
    vectorArray2.add(0.6294256393761057);
    
    dict2.addProperty("id", 2L);
    dict2.add("vector", gson.toJsonTree(vectorArray2));
    
    data.add(dict1);
    data.add(dict2);
    ```

- `collectionName(String collectionName)`

  现有集合的名称。

- `partitionName(String partitionName)`

  现有分区的名称。


**退货类型：**

_更新插入响应_

**返回：**

包含有关插入或更新实体数量信息的**UpsertResp**对象。

**例外情况：**

- **MilvusClientExceptions**

  当此操作期间发生任何错误时，将会引发此异常。


## 例子

```java
// upsert operation
JsonObject jsonObject = new JsonObject();
List<Float> vectorList = new ArrayList<>();
vectorList.add(2.0f);
vectorList.add(3.0f);
jsonObject.add("vector", gson.toJsonTree(vectorList));
jsonObject.addProperty("id", 0L);
UpsertReq upsertReq = UpsertReq.builder()
        .collectionName("test")
        .data(Collections.singletonList(jsonObject))
        .build();

client.upsert(upsertReq);
```