package outfox.infra.server.commons.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * author: zhn4528
 * create: 2020/12/4 11:11
*/
@Slf4j
@Component
@ConditionalOnClass(ElasticsearchRestTemplate.class)
public class ElasticsearchUtil<T> {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * insert doc
     * @param data es data
     * @return success: _id / failure: null
    */
    public String insert(String indexName, T data) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withObject(data)
                    .build();
            return elasticsearchRestTemplate.index(
                    indexQuery, IndexCoordinates.of(indexName)
            );
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * delete by id
     * @param clazz T.class
     * @param id doc _id
     * @return success: _id / failure: null
    */
    public String deleteById(Class<T> clazz, String id) {
        try {
            return elasticsearchRestTemplate.delete(id, clazz);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * delete by id
     * @param data T data
     * @return success: _id / failure: null
     */
    public String deleteById(T data) {
        try {
            return elasticsearchRestTemplate.delete(data);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * delete by id
     * @param data T data
     * @param indexName indexName
     * @return success: _id / failure: null
     */
    public String deleteById(T data, String indexName) {
        try {
            return elasticsearchRestTemplate.delete(data, IndexCoordinates.of(indexName));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * query by id
     * @param clazz T.class
     * @param id doc _id
     * @return success: data / failure: null
    */
    public T queryById(Class<T> clazz, String id) {
        try {
            return elasticsearchRestTemplate.get(
                    id, clazz
            );
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * delete index
     * @param indexName index name
     * @return success: true / failure false
    */
    public boolean deleteIndex(String indexName) {
        try {
            return elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * delete index
     * @param clazz T.class
     * @return success: true / failure false
    */
    public boolean deleteIndex(Class<T> clazz) {
        try {
            return elasticsearchRestTemplate.indexOps(clazz).delete();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * get index name by @Document
     * @param clazz class
     * @return index name
    */
    public static String getIndexName(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Document.class)) {
            throw new IllegalStateException("Elasticsearch domain must have @Document annotation!");
        }

        Document annotation = clazz.getDeclaredAnnotation(Document.class);
        return annotation.indexName();
    }

}
