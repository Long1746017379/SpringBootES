package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.EsConfig;
import com.example.demo.entity.BusinessInfoEs;
import com.example.demo.entity.SyncBusinessInfo;
import com.example.demo.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/test")
public class UserController {

    @Value("${es.cluster.indexName}")
    private String indexName;

    RestHighLevelClient restHighLevelClient = EsConfig.getclient();

    @GetMapping(value = "/testJsonObject")
    public JSONObject testJsonObject() {
        String postJson = "{\n" +
                "    \"rc\": \"0000\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"password\": \"\",\n" +
                "            \"address\": \"东四环与汉风路交叉口北50米路东 郑州市殡仪馆\",\n" +
                "            \"addService\": \"转行\",\n" +
                "            \"phone\": \"037166884444\",\n" +
                "            \"name\": \"郑州市殡仪馆\",\n" +
                "            \"unitid\": \"97403168\",\n" +
                "            \"keyname\": \"郑州市殡仪馆\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"password\": \"\",\n" +
                "            \"address\": \"郑州市金水区郑花路59号 郑州市金水区良信电器制冷设备维修部\",\n" +
                "            \"addService\": \"转行\",\n" +
                "            \"phone\": \"15333823233\",\n" +
                "            \"name\": \"小松鼠壁挂炉维修非特约\",\n" +
                "            \"unitid\": \"97823066\",\n" +
                "            \"keyname\": \"小松鼠壁挂炉维修\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"password\": \"\",\n" +
                "            \"address\": \"郑州市范围内均可服务 郑州天顺家电服务有限公司\",\n" +
                "            \"addService\": \"转行\",\n" +
                "            \"phone\": \"18838268051\",\n" +
                "            \"name\": \"华帝灶具维修非特约\",\n" +
                "            \"unitid\": \"98005923\",\n" +
                "            \"keyname\": \"华帝灶具维修非特约\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"desc\": \"查询成功!\"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject rspBody = new JSONObject();
        try {
            List<SyncBusinessInfo> rspList = new ArrayList<>();
            Map<String, Object> jsonMap = objectMapper.readValue(postJson, Map.class);
            String respCode = (String) jsonMap.get("respCode");

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SyncBusinessInfo.class);
            List<SyncBusinessInfo> data = objectMapper.readValue(objectMapper.writeValueAsBytes(jsonMap.get("data")), listType);

            log.info("数据为：{}", data);
            rspBody.put("data", data);
        } catch (IOException e) {
            e.printStackTrace();
            rspBody.put("message", e);
        }
        return rspBody;
    }


    @PostMapping(value = "/createIndex")
    public String createESIndex(@RequestBody JSONObject jsonObject) {
        log.info("----------------------------------------createIndex----------------------------------------");
        String indexName = (String) jsonObject.get("indexName");

        try {
            if (createIndex(indexName)) {
                return "创建成功";
            }
            return "已存在";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "/pinyin")
    public void testPinyin(@RequestBody JSONObject jsonObject) {
        log.info("pinyin");
        String content = (String) jsonObject.get("content");
        log.info(content);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        WildcardQueryBuilder wildcardQueryBuilder = null;
        if (StringUtils.isNoneBlank(content)) {
            wildcardQueryBuilder = QueryBuilders.wildcardQuery("businessName", content);
        }

        boolQueryBuilder.must(wildcardQueryBuilder);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(Integer.MAX_VALUE);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = search.getHits();
            log.info("共命中 {}", hits.getTotalHits().value);

            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
//                log.info("ID {} 分数 {}", hit.getId(), hit.getScore());
                log.info("全部信息 {}", sourceAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @PostMapping(value = "/queryByCountyCodeAndBusinessTypeId")
    public JSONArray queryByCountyCodeAndBusinessTypeId(@RequestBody JSONObject jsonObject) {
        log.info("/queryByCountyCodeAndBusinessTypeId 请求{}", jsonObject);

        String searchCont = null;
        Double userLng = null;
        Double userLat = null;
        Integer pageSize = null;
        Integer pageNo = null;
        Integer businessTypeId = null;
        Integer countyCode = null;
        String orderBy = null;

        JSONArray jsonArray = new JSONArray();
        try {
            searchCont = (String) jsonObject.get("searchCont");
            /**
             * 经度
             */
            userLng = (Double) jsonObject.get("lng");
            /**
             * 纬度
             */
            userLat = (Double) jsonObject.get("lat");
            pageSize = (Integer) jsonObject.get("pageSize");
            pageNo = (Integer) jsonObject.get("pageNo");
            businessTypeId = (Integer) jsonObject.get("businessTypeId");
            orderBy = (String) jsonObject.get("orderBy");
            countyCode = (Integer) jsonObject.get("countyCode");

            // 走ES聚合查询
            jsonArray = queryEsByBoolQueryBuilder(searchCont, userLat, userLng, countyCode, businessTypeId, pageNo, pageSize);

            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("入参类型转换异常 {}", e.getMessage());
            return jsonArray;
        }

    }

    /**
     * ES 聚合查询
     *
     * @param searchCont     搜索词
     * @param userLat        纬度
     * @param userLng        经度
     * @param countCode      区县编码
     * @param businessTypeId 商家类别编码
     * @param pageNO         页数
     * @param pageSize       每页个数
     * @return
     */
    public JSONArray queryEsByBoolQueryBuilder(String searchCont, Double userLat, Double userLng, Integer countCode,
                                               Integer businessTypeId, int pageNO, int pageSize) {

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (StringUtils.isNoneBlank(searchCont)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("businessName", searchCont));
        }

        if (countCode != null && countCode != 0) {
            QueryBuilder countCodeBuilder = QueryBuilders.termQuery("countyCode", countCode);
            boolQueryBuilder.must(countCodeBuilder);
        }

        if (businessTypeId != null && businessTypeId != 0) {
            QueryBuilder businessTypeBuilder = QueryBuilders.termQuery("businessTypeId", businessTypeId);
            boolQueryBuilder.must(businessTypeBuilder);
        }

        // 设置搜索半径
        GeoDistanceQueryBuilder businessLngLot = QueryBuilders.geoDistanceQuery("businessLngLat")
                .point(userLat, userLng)
                .distance(20, DistanceUnit.KILOMETERS)
                .geoDistance(GeoDistance.PLANE);

        // 距离排序
        GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("businessLngLat", userLat, userLng);
        sort.order(SortOrder.ASC);
        sort.point(userLat, userLng);

        boolQueryBuilder.must(businessLngLot);

        // 构建检索
        SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource()
                .from(pageNO * pageSize)
                .size(pageSize)
                .query(boolQueryBuilder)
                .sort(sort);

        // 搜索请求（哪个索引库）
        SearchRequest searchRequest = new SearchRequest(indexName)
                .source(sourceBuilder);


        JSONArray responseJsonArray = new JSONArray();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 获取查询结果集
            SearchHits hits = response.getHits();

            JSONObject responseJsonObject = null;

            if (hits.getHits().length > 0) {
                log.info("共命中 {} 个", hits.getTotalHits().value);
                log.info("匹配到结果 {} 个", hits.getHits().length);

                // 遍历结果集
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    responseJsonObject = JSONObject.parseObject(sourceAsString);

                    String esBusinessHeadUrl = (String) responseJsonObject.get("businessHeadUrl");
                    if (StringUtils.isNoneBlank(esBusinessHeadUrl)) {
                        responseJsonObject.put("businessHeadUrl", esBusinessHeadUrl);
                    }

                    String esBusinessLngLot = (String) responseJsonObject.get("businessLngLat");
                    String[] split = null;
                    if (StringUtils.isNoneBlank(esBusinessLngLot)) {
                        split = esBusinessLngLot.split(",");
                        responseJsonObject.put("lat", Double.valueOf(split[0]));
                        responseJsonObject.put("lng", Double.valueOf(split[1]));
                    }

                    List<String> bgUrlList = new ArrayList<>();
                    String esBusinessBgUrl = (String) responseJsonObject.get("businessBgUrl");
                    if (StringUtils.isNoneBlank(esBusinessBgUrl)) {
                        String[] bgUrls = esBusinessBgUrl.split(",");
                        for (String bgUrl : bgUrls) {
                            bgUrl = bgUrl;
                            bgUrlList.add(bgUrl);
                        }
                    }
                    responseJsonObject.put("businessBgUrl", bgUrlList);

                    if (split != null && split.length == 2) {
                        double calculate = GeoDistance.ARC.calculate(userLat, userLng, Double.valueOf(split[0]), Double.valueOf(split[1]), DistanceUnit.METERS);
                        responseJsonObject.put("businessToMeDist", calculate);
                    }
                    responseJsonArray.add(responseJsonObject);
                }
                log.info("共返回 {}个", responseJsonArray.size());
                return responseJsonArray;
            }

            return responseJsonArray;
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("ES查询失败 {}", e.getMessage());
            return responseJsonArray;
        }


    }

    /**
     * 通过ID进行查询ES
     *
     * @param j
     * @return
     */
    @PostMapping(value = "/queryEsById")
    public JSONArray queryEsById(@RequestBody JSONObject j) {
        log.info("请求 {}", j);
        String businessId = (String) j.get("businessId");
        JSONArray jsonArray = new JSONArray();
        List<BusinessInfoEs> businessInfoEsList = new ArrayList<>();

        if (StringUtils.isNoneBlank(businessId)) {

            IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().
                    addIds(businessId);

            SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                    .query(idsQueryBuilder);

            SearchRequest searchRequest = new SearchRequest(indexName)
                    .source(searchSourceBuilder);

            JSONObject jsonObject = null;
            try {
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                if (hits.getHits().length > 0) {
                    log.info("共命中 {} 个", hits.getTotalHits().value);
                    log.info("匹配到结果 {} 个", hits.getHits().length);

                    for (SearchHit hit : hits) {
                        String sourceAsString = hit.getSourceAsString();
                        BusinessInfoEs businessInfoEs = JSONObject.parseObject(sourceAsString, BusinessInfoEs.class);
                        log.info("ID {}， 分数 {}", hit.getId(), hit.getScore());
                        jsonObject = JSONObject.parseObject(sourceAsString);

                        String esBusinessLngLot = (String) jsonObject.get("businessLngLat");
                        String[] split = null;
                        if (StringUtils.isNoneBlank(esBusinessLngLot)) {
                            split = esBusinessLngLot.split(",");
                            jsonObject.put("lat", Double.valueOf(split[0]));
                            jsonObject.put("lng", Double.valueOf(split[1]));
                        }

                        log.info("jsonObject信息 {}", jsonObject);
                        JSONObject jsonObject1 = JSONObject.parseObject(JsonUtils.toString(businessInfoEs));
                        jsonArray.add(jsonObject);
                        businessInfoEsList.add(businessInfoEs);
                    }
                    return jsonArray;
                }
                return jsonArray;
            } catch (IOException e) {
                e.printStackTrace();
                log.error("ES查询失败 {}", e.getMessage());
                return jsonArray;
            }
        }
        return jsonArray;
    }


    /**
     * 测试pinyin分词
     *
     * @param j
     * @return
     */
    @PostMapping(value = "/queryAll")
    public JSONArray queryAll(@RequestBody JSONObject j) {

        log.info("{}", j);
        // 复合查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 查询全部
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("businessName", "宝feng");

        // 加入复合查询的条件
        boolQueryBuilder.must(matchQueryBuilder);

        // 构建检索
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .query(boolQueryBuilder)
                .from(0)
                .size(30);
//                .sort(new FieldSortBuilder("id").order(SortOrder.ASC));


        // 搜索请求
        SearchRequest searchRequest = new SearchRequest(indexName)
                .source(searchSourceBuilder);

        JSONArray jsonArray = new JSONArray();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            JSONObject jsonObject = null;
            log.info("共命中 {} 个", hits.getTotalHits().value);
            log.info("匹配到结果 {} 个", hits.getHits().length);

            if (hits.getHits().length > 0) {
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    jsonObject = JSONObject.parseObject(sourceAsString);
                    log.info("ID {} 分数 {} 全部信息{}", hit.getId(), hit.getScore(), sourceAsString);
                    jsonObject.put("ID", hit.getId());
                    jsonArray.add(jsonObject);
                }
            }

            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    /**
     * 更新ES
     *
     * @param jsonObject
     */
    @PostMapping(value = "updateES")
    public void updateES(@RequestBody JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        Map<String, Object> map = new HashMap<>();
        map.put("age", 999);
        map.put("phone", 987654321);
        map.put("address", "河北省邯郸市成安县");

        UpdateRequest updateRequest = new UpdateRequest("1125", id)
                .doc(map)
                .upsert(map);

        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            log.info("{}", updateResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @PostMapping(value = "writeES")
    public void writeES(@RequestBody JSONObject jsonObject) {
        String s = JSONObject.toJSONString(jsonObject);
        log.info(s);

        IndexRequest indexRequest = new IndexRequest("1125")
                .id((String) jsonObject.get("id"))
                .source(s, XContentType.JSON);

        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("{}", indexResponse);
            if (indexResponse != null) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                    log.info("shardInfo={}", JSONObject.toJSON(shardInfo));
                }

                // 如果有分片副本失败，可以获得失败原因信息
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        log.info("副本失败原因,reason={}", reason);
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 删除索引库
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/deleteIndex")
    public String deleteIndex(@RequestBody JSONObject jsonObject) {
        String indexName = (String) jsonObject.get("indexName");
        try {
            if (deleteIndex(indexName)) {
                return "删除成功";
            }
            return "删除失败";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引库和Mapping
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean createIndex(String indexName) throws IOException {
        log.info("需要创建的索引库为{}", indexName);
        if (!existsIndex(indexName)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("properties")
                    .startObject()
                    .field("businessId").startObject().field("index", "true").field("type", "integer").endObject()
                    .field("businessName").startObject().field("index", "true").field("type", "text").field("analyzer", "ik_max_word").endObject()
                    .field("businessAddress").startObject().field("index", "true").field("type", "text").field("analyzer", "ik_max_word").endObject()
                    .field("businessPhone").startObject().field("index", "true").field("type", "integer").endObject()
                    .field("businessLngLot").startObject().field("index", "true").field("type", "text").endObject()
                    .field("businessStatus").startObject().field("index", "true").field("type", "text").endObject()
                    .field("businessCreateData").startObject().field("index", "true").field("type", "date").field("format", "strict_date_optional_time||epoch_millis").endObject()
                    .endObject()
                    .endObject();

            createIndexRequest.mapping(builder);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            // isAcknowledged() 判断操作结果 Boolean 类型
            if (createIndexResponse.isAcknowledged()) {
                log.info("{}索引创建成功", indexName);
            }
            return true;
        }
        log.error("{}该索引库已存在，无法创建！！！", indexName);
        return false;
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        log.info("需要删除的索引库为{}", indexName);
        if (existsIndex(indexName)) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            if (delete.isAcknowledged()) {
                log.info("{}索引删除成功", indexName);
            }
            return true;
        }
        log.error("{}该索引库不存在，无法删除！！！", indexName);
        return false;
    }


}
