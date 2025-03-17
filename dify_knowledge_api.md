# Knowledge API

## API Server

http://dify.cogmo.com.br/v1

## Authentication

Service API of Dify authenticates using an API-Key.

It is suggested that developers store the API-Key in the backend instead of sharing or storing it in the client side to avoid the leakage of the API-Key, which may lead to property loss.

All API requests should include your API-Key in the Authorization HTTP Header, as shown below:

```
Authorization: Bearer {API_KEY}
```

## Create a Document from Text

### POST /datasets/{dataset_id}/document/create-by-text

This API is based on an existing knowledge and creates a new document through text based on this knowledge.

#### Params

- dataset_id (string): Knowledge ID

#### Request Body

- name (string): Document name
- text (string): Document content
- indexing_technique (string): Index mode
  - high_quality: High quality - embedding using embedding model, built as vector database index
  - economy: Economy - Build using inverted index of keyword table index
- process_rule (object): Processing rules
  - mode (string): Cleaning, segmentation mode, automatic / custom
  - rules (object): Custom rules (in automatic mode, this field is empty)
  - pre_processing_rules (array[object]): Preprocessing rules
    - id (string): Unique identifier for the preprocessing rule
    - enumerate
      - remove_extra_spaces: Replace consecutive spaces, newlines, tabs
      - remove_urls_emails: Delete URL, email address
    - enabled (bool): Whether to select this rule or not. If no document ID is passed in, it represents the default value.
  - segmentation (object): Segmentation rules
    - separator: Custom segment identifier, currently only allows one delimiter to be set. Default is \n
    - max_tokens: Maximum length (token) defaults to 1000

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/document/create-by-text' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{"name": "text","text": "text","indexing_technique": "high_quality","process_rule": {"mode": "automatic"}}'
```

#### Response

```
{
  "document": {
    "id": "",
    "position": 1,
    "data_source_type": "upload_file",
    "data_source_info": {
        "upload_file_id": ""
    },
    "dataset_process_rule_id": "",
    "name": "text.txt",
    "created_from": "api",
    "created_by": "",
    "created_at": 1695690280,
    "tokens": 0,
    "indexing_status": "waiting",
    "error": null,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "archived": false,
    "display_status": "queuing",
    "word_count": 0,
    "hit_count": 0,
    "doc_form": "text_model"
  },
  "batch": ""
}
```

## Create a Document from a File

### POST /datasets/{dataset_id}/document/create-by-file

This API is based on an existing knowledge and creates a new document through a file based on this knowledge.

#### Params

- dataset_id (string): Knowledge ID

#### Request Body

- data (multipart/form-data json string): Source document ID (optional)
  - Used to re-upload the document or modify the document cleaning and segmentation configuration
  - The source document cannot be an archived document
  - When original_document_id is passed in, the update operation is performed on behalf of the document
  - process_rule is a fillable item. If not filled in, the segmentation method of the source document will be used by default
  - When original_document_id is not passed in, the new operation is performed on behalf of the document, and process_rule is required
- indexing_technique (string): Index mode
  - high_quality: High quality - embedding using embedding model, built as vector database index
  - economy: Economy - Build using inverted index of keyword table index
- process_rule (object): Processing rules
  - mode (string): Cleaning, segmentation mode, automatic / custom
  - rules (object): Custom rules (in automatic mode, this field is empty)
  - pre_processing_rules (array[object]): Preprocessing rules
    - id (string): Unique identifier for the preprocessing rule
    - enumerate
      - remove_extra_spaces: Replace consecutive spaces, newlines, tabs
      - remove_urls_emails: Delete URL, email address
    - enabled (bool): Whether to select this rule or not. If no document ID is passed in, it represents the default value.
  - segmentation (object): Segmentation rules
    - separator: Custom segment identifier, currently only allows one delimiter to be set. Default is \n
    - max_tokens: Maximum length (token) defaults to 1000
- file (multipart/form-data): Files that need to be uploaded

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/document/create-by-file' \
--header 'Authorization: Bearer {api_key}' \
--form 'data="{"indexing_technique":"high_quality","process_rule":{"rules":{"pre_processing_rules":[{"id":"remove_extra_spaces","enabled":true},{"id":"remove_urls_emails","enabled":true}],"segmentation":{"separator":"###","max_tokens":500}},"mode":"custom"}}";type=text/plain' \
--form 'file=@"/path/to/file"'
```

#### Response

```
{
  "document": {
    "id": "",
    "position": 1,
    "data_source_type": "upload_file",
    "data_source_info": {
      "upload_file_id": ""
    },
    "dataset_process_rule_id": "",
    "name": "Dify.txt",
    "created_from": "api",
    "created_by": "",
    "created_at": 1695308667,
    "tokens": 0,
    "indexing_status": "waiting",
    "error": null,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "archived": false,
    "display_status": "queuing",
    "word_count": 0,
    "hit_count": 0,
    "doc_form": "text_model"
  },
  "batch": ""
}
```

## Create an Empty Knowledge Base

### POST /datasets

#### Request Body

- name (string): Knowledge name
- description (string): Knowledge description (optional)
- indexing_technique (string): Index technique (optional)
  - high_quality: High quality
  - economy: Economy
- permission (string): Permission
  - only_me: Only me
  - all_team_members: All team members
  - partial_members: Partial members
- provider (string): Provider (optional, default: vendor)
  - vendor: Vendor
  - external: External knowledge
- external_knowledge_api_id (string): External knowledge API ID (optional)
- external_knowledge_id (string): External knowledge ID (optional)

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{"name": "name", "permission": "only_me"}'
```

#### Response

```
{
  "id": "",
  "name": "name",
  "description": null,
  "provider": "vendor",
  "permission": "only_me",
  "data_source_type": null,
  "indexing_technique": null,
  "app_count": 0,
  "document_count": 0,
  "word_count": 0,
  "created_by": "",
  "created_at": 1695636173,
  "updated_by": "",
  "updated_at": 1695636173,
  "embedding_model": null,
  "embedding_model_provider": null,
  "embedding_available": null
}
```

## Get Knowledge Base List

### GET /datasets

#### Query Parameters

- page (string): Page number
- limit (string): Number of items returned, default 20, range 1-100

#### Request

```
curl --location --request GET 'http://dify.cogmo.com.br/v1/datasets?page=1&limit=20' \
--header 'Authorization: Bearer {api_key}'
```

#### Response

```
{
  "data": [
    {
      "id": "",
      "name": "name",
      "description": "desc",
      "permission": "only_me",
      "data_source_type": "upload_file",
      "indexing_technique": "",
      "app_count": 2,
      "document_count": 10,
      "word_count": 1200,
      "created_by": "",
      "created_at": "",
      "updated_by": "",
      "updated_at": ""
    }
  ],
  "has_more": true,
  "limit": 20,
  "total": 50,
  "page": 1
}
```

## Delete a Knowledge Base

### DELETE /datasets/{dataset_id}

#### Params

- dataset_id (string): Knowledge ID

#### Request

```
curl --location --request DELETE 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}' \
--header 'Authorization: Bearer {api_key}'
```

#### Response

204 No Content

## Update a Document with Text

### POST /datasets/{dataset_id}/documents/{document_id}/update-by-text

This API is based on an existing knowledge and updates the document through text based on this knowledge.

#### Params

- dataset_id (string): Knowledge ID
- document_id (string): Document ID

#### Request Body

- name (string): Document name (optional)
- text (string): Document content (optional)
- process_rule (object): Processing rules
  - mode (string): Cleaning, segmentation mode, automatic / custom
  - rules (object): Custom rules (in automatic mode, this field is empty)
  - pre_processing_rules (array[object]): Preprocessing rules
    - id (string): Unique identifier for the preprocessing rule
    - enumerate
      - remove_extra_spaces: Replace consecutive spaces, newlines, tabs
      - remove_urls_emails: Delete URL, email address
    - enabled (bool): Whether to select this rule or not. If no document ID is passed in, it represents the default value.
  - segmentation (object): Segmentation rules
    - separator: Custom segment identifier, currently only allows one delimiter to be set. Default is \n
    - max_tokens: Maximum length (token) defaults to 1000

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}/update-by-text' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{"name": "name","text": "text"}'
```

#### Response

```
{
  "document": {
    "id": "",
    "position": 1,
    "data_source_type": "upload_file",
    "data_source_info": {
      "upload_file_id": ""
    },
    "dataset_process_rule_id": "",
    "name": "name.txt",
    "created_from": "api",
    "created_by": "",
    "created_at": 1695308667,
    "tokens": 0,
    "indexing_status": "waiting",
    "error": null,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "archived": false,
    "display_status": "queuing",
    "word_count": 0,
    "hit_count": 0,
    "doc_form": "text_model"
  },
  "batch": ""
}
```

## Update a Document with a File

### POST /datasets/{dataset_id}/documents/{document_id}/update-by-file

This API is based on an existing knowledge, and updates documents through files based on this knowledge.

#### Params

- dataset_id (string): Knowledge ID
- document_id (string): Document ID

#### Request Body

- name (string): Document name (optional)
- file (multipart/form-data): Files to be uploaded
- process_rule (object): Processing rules
  - mode (string): Cleaning, segmentation mode, automatic / custom
  - rules (object): Custom rules (in automatic mode, this field is empty)
  - pre_processing_rules (array[object]): Preprocessing rules
    - id (string): Unique identifier for the preprocessing rule
    - enumerate
      - remove_extra_spaces: Replace consecutive spaces, newlines, tabs
      - remove_urls_emails: Delete URL, email address
    - enabled (bool): Whether to select this rule or not. If no document ID is passed in, it represents the default value.
  - segmentation (object): Segmentation rules
    - separator: Custom segment identifier, currently only allows one delimiter to be set. Default is \n
    - max_tokens: Maximum length (token) defaults to 1000

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}/update-by-file' \
--header 'Authorization: Bearer {api_key}' \
--form 'data="{"name":"Dify","indexing_technique":"high_quality","process_rule":{"rules":{"pre_processing_rules":[{"id":"remove_extra_spaces","enabled":true},{"id":"remove_urls_emails","enabled":true}],"segmentation":{"separator":"###","max_tokens":500}},"mode":"custom"}}";type=text/plain' \
--form 'file=@"/path/to/file"'
```

#### Response

```
{
  "document": {
    "id": "",
    "position": 1,
    "data_source_type": "upload_file",
    "data_source_info": {
      "upload_file_id": ""
    },
    "dataset_process_rule_id": "",
    "name": "Dify.txt",
    "created_from": "api",
    "created_by": "",
    "created_at": 1695308667,
    "tokens": 0,
    "indexing_status": "waiting",
    "error": null,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "archived": false,
    "display_status": "queuing",
    "word_count": 0,
    "hit_count": 0,
    "doc_form": "text_model"
  },
  "batch": "20230921150427533684"
}
```

## Get Document Embedding Status (Progress)

### GET /datasets/{dataset_id}/documents/{batch}/indexing-status

#### Params

- dataset_id (string): Knowledge ID
- batch (string): Batch number of uploaded documents

#### Request

```
curl --location --request GET 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{batch}/indexing-status' \
--header 'Authorization: Bearer {api_key}'
```

#### Response

```
{
  "data": [{
    "id": "",
    "indexing_status": "indexing",
    "processing_started_at": 1681623462.0,
    "parsing_completed_at": 1681623462.0,
    "cleaning_completed_at": 1681623462.0,
    "splitting_completed_at": 1681623462.0,
    "completed_at": null,
    "paused_at": null,
    "error": null,
    "stopped_at": null,
    "completed_segments": 24,
    "total_segments": 100
  }]
}
```

## Delete a Document

### DELETE /datasets/{dataset_id}/documents/{document_id}

#### Params

- dataset_id (string): Knowledge ID
- document_id (string): Document ID

#### Request

```
curl --location --request DELETE 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}' \
--header 'Authorization: Bearer {api_key}'
```

#### Response

```
{
  "result": "success"
}
```

## Get the Document List of a Knowledge Base

### GET /datasets/{dataset_id}/documents

#### Params

- dataset_id (string): Knowledge ID

#### Query Parameters

- keyword (string): Search keywords, currently only search document names (optional)
- page (string): Page number (optional)
- limit (string): Number of items returned, default 20, range 1-100 (optional)

#### Request

```
curl --location --request GET 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents' \
--header 'Authorization: Bearer {api_key}'
```

#### Response

```
{
  "data": [
    {
      "id": "",
      "position": 1,
      "data_source_type": "file_upload",
      "data_source_info": null,
      "dataset_process_rule_id": null,
      "name": "dify",
      "created_from": "",
      "created_by": "",
      "created_at": 1681623639,
      "tokens": 0,
      "indexing_status": "waiting",
      "error": null,
      "enabled": true,
      "disabled_at": null,
      "disabled_by": null,
      "archived": false
    }
  ],
  "has_more": false,
  "limit": 20,
  "total": 9,
  "page": 1
}
```

## Add Chunks to a Document

### POST /datasets/{dataset_id}/documents/{document_id}/segments

#### Params

- dataset_id (string): Knowledge ID
- document_id (string): Document ID

#### Request Body

- segments (array[object]): List of segments
  - content (string): Text content / question content, required
  - answer (string): Answer content, if the mode of the knowledge is Q&A mode, pass the value (optional)
  - keywords (array[string]): Keywords (optional)

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}/segments' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{"segments": [{"content": "1","answer": "1","keywords": ["a"]}]}'
```

#### Response

```
{
  "data": [{
    "id": "",
    "position": 1,
    "document_id": "",
    "content": "1",
    "answer": "1",
    "word_count": 25,
    "tokens": 0,
    "keywords": [
      "a"
    ],
    "index_node_id": "",
    "index_node_hash": "",
    "hit_count": 0,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "status": "completed",
    "created_by": "",
    "created_at": 1695312007,
    "indexing_at": 1695312007,
    "completed_at": 1695312007,
    "error": null,
    "stopped_at": null
  }],
  "doc_form": "text_model"
}
```

## Get Chunks from a Document

### GET /datasets/{dataset_id}/documents/{document_id}/segments

#### Path Parameters

- dataset_id (string): Knowledge ID
- document_id (string): Document ID

#### Query Parameters

- keyword (string): Keyword (optional)
- status (string): Search status, completed

#### Request

```
curl --location --request GET 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}/segments' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json'
```

#### Response

```
{
  "data": [{
    "id": "",
    "position": 1,
    "document_id": "",
    "content": "1",
    "answer": "1",
    "word_count": 25,
    "tokens": 0,
    "keywords": [
      "a"
    ],
    "index_node_id": "",
    "index_node_hash": "",
    "hit_count": 0,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "status": "completed",
    "created_by": "",
    "created_at": 1695312007,
    "indexing_at": 1695312007,
    "completed_at": 1695312007,
    "error": null,
    "stopped_at": null
  }],
  "doc_form": "text_model"
}
```

## Delete a Chunk in a Document

### DELETE /datasets/{dataset_id}/documents/{document_id}/segments/{segment_id}

#### Path Parameters

- dataset_id (string): Knowledge ID
- document_id (string): Document ID
- segment_id (string): Document Segment ID

#### Request

```
curl --location --request DELETE 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/segments/{segment_id}' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json'
```

#### Response

```
{
  "result": "success"
}
```

## Update a Chunk in a Document

### POST /datasets/{dataset_id}/documents/{document_id}/segments/{segment_id}

#### Path Parameters

- dataset_id (string): Knowledge ID
- document_id (string): Document ID
- segment_id (string): Document Segment ID

#### Request Body

- segment (object): Segment data
  - content (string): Text content / question content, required
  - answer (string): Answer content, passed if the knowledge is in Q&A mode (optional)
  - keywords (array[string]): Keywords (optional)
  - enabled (boolean): False / true (optional)

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/documents/{document_id}/segments/{segment_id}' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{"segment": {"content": "1","answer": "1", "keywords": ["a"], "enabled": false}}'
```

#### Response

```
{
  "data": [{
    "id": "",
    "position": 1,
    "document_id": "",
    "content": "1",
    "answer": "1",
    "word_count": 25,
    "tokens": 0,
    "keywords": [
      "a"
    ],
    "index_node_id": "",
    "index_node_hash": "",
    "hit_count": 0,
    "enabled": true,
    "disabled_at": null,
    "disabled_by": null,
    "status": "completed",
    "created_by": "",
    "created_at": 1695312007,
    "indexing_at": 1695312007,
    "completed_at": 1695312007,
    "error": null,
    "stopped_at": null
  }],
  "doc_form": "text_model"
}
```

## Retrieve Chunks from a Knowledge Base

### POST /datasets/{dataset_id}/retrieve

#### Path Parameters

- dataset_id (string): Knowledge ID

#### Request Body

- query (string): Query keyword
- retrieval_model (object): Retrieval model (optional, if not filled, it will be recalled according to the default method)
  - search_method (string): Search method, one of the following required:
    - keyword_search: Keyword search
    - semantic_search: Semantic search
    - full_text_search: Full-text search
    - hybrid_search: Hybrid search
  - reranking_enable (boolean): Whether to enable reranking, required if search mode is semantic_search or hybrid_search
  - reranking_mode (object): Rerank model configuration, required if reranking is enabled
    - reranking_provider_name (string): Rerank model provider
    - reranking_model_name (string): Rerank model name
  - weights (number): Semantic search weight setting in hybrid search mode
  - top_k (integer): Number of results to return (optional)
  - score_threshold_enabled (boolean): Whether to enable score threshold
  - score_threshold (number): Score threshold
- external_retrieval_model (object): Unused field

#### Request

```
curl --location --request POST 'http://dify.cogmo.com.br/v1/datasets/{dataset_id}/retrieve' \
--header 'Authorization: Bearer {api_key}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "query": "test",
  "retrieval_model": {
    "search_method": "keyword_search",
    "reranking_enable": false,
    "reranking_mode": null,
    "reranking_model": {
      "reranking_provider_name": "",
      "reranking_model_name": ""
    },
    "weights": null,
    "top_k": 1,
    "score_threshold_enabled": false,
    "score_threshold": null
  }
}'
```

#### Response

```
{
  "query": {
    "content": "test"
  },
  "records": [
    {
      "segment": {
        "id": "7fa6f24f-8679-48b3-bc9d-bdf28d73f218",
        "position": 1,
        "document_id": "a8c6c36f-9f5d-4d7a-8472-f5d7b75d71d2",
        "content": "Operation guide",
        "answer": null,
        "word_count": 847,
        "tokens": 280,
        "keywords": [
          "install",
          "java",
          "base",
          "scripts",
          "jdk",
          "manual",
          "internal",
          "opens",
          "add",
          "vmoptions"
        ],
        "index_node_id": "39dd8443-d960-45a8-bb46-7275ad7fbc8e",
        "index_node_hash": "0189157697b3c6a418ccf8264a09699f25858975578f3467c76d6bfc94df1d73",
        "hit_count": 0,
        "enabled": true,
        "disabled_at": null,
        "disabled_by": null,
        "status": "completed",
        "created_by": "dbcb1ab5-90c8-41a7-8b78-73b235eb6f6f",
        "created_at": 1728734540,
        "indexing_at": 1728734552,
        "completed_at": 1728734584,
        "error": null,
        "stopped_at": null,
        "document": {
          "id": "a8c6c36f-9f5d-4d7a-8472-f5d7b75d71d2",
          "data_source_type": "upload_file",
          "name": "readme.txt",
          "doc_type": null
        }
      },
      "score": 3.730463140527718e-05,
      "tsne_position": null
    }
  ]
}
```

## Error Messages

#### Fields

- code (string): Error code
- status (number): Error status
- message (string): Error message

```

```
