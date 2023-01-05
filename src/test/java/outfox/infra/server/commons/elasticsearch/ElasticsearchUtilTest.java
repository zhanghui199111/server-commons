package outfox.infra.server.commons.elasticsearch;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import outfox.infra.server.commons.elasticsearch.data.ElasticsearchTestData;
import outfox.infra.server.commons.BaseTestCase;

/**
 * author: zhn4528
 * create: 2020/12/4 11:23
*/
@Ignore
public class ElasticsearchUtilTest extends BaseTestCase {

    @Autowired
    private ElasticsearchUtil<ElasticsearchTestData> elasticsearchUtil;

    @Before
    public void init() {
    }

    @Test
    public void testInsert() {
        String name = "testInsert";
        int num = 23432523;
        boolean judge = true;
        ElasticsearchTestData elasticsearchTestData = ElasticsearchTestData
                .builder()
                .name(name)
                .num(num)
                .judge(judge)
                .build();
        String id = elasticsearchUtil.insert(
                ElasticsearchUtil.getIndexName(ElasticsearchTestData.class),
                elasticsearchTestData
        );
        ElasticsearchTestData getData = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNotNull(getData);
        Assert.assertEquals(getData.getName(), name);
        Assert.assertEquals(getData.getNum(), num);
        Assert.assertEquals(getData.isJudge(), judge);
    }

    // deleteById(T data) testcase
    @Test
    public void testDeleteById1() {
        String name = "testDeleteById1";
        int num = 134512435;
        boolean judge = false;
        ElasticsearchTestData elasticsearchTestData = ElasticsearchTestData
                .builder()
                .name(name)
                .num(num)
                .judge(judge)
                .build();
        String id = elasticsearchUtil.insert(
                ElasticsearchUtil.getIndexName(ElasticsearchTestData.class),
                elasticsearchTestData
        );
        ElasticsearchTestData getData = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNotNull(getData);

        String deletedId = elasticsearchUtil.deleteById(getData);
        Assert.assertEquals(id, deletedId);
        ElasticsearchTestData getData1 = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNull(getData1);
    }

    // deleteById(Class<T> clazz, String id) testcase
    @Test
    public void testDeleteById2() {
        String name = "testDeleteById2";
        int num = 765512435;
        boolean judge = false;
        ElasticsearchTestData elasticsearchTestData = ElasticsearchTestData
                .builder()
                .name(name)
                .num(num)
                .judge(judge)
                .build();
        String id = elasticsearchUtil.insert(
                ElasticsearchUtil.getIndexName(ElasticsearchTestData.class),
                elasticsearchTestData
        );
        ElasticsearchTestData getData = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNotNull(getData);

        String deletedId = elasticsearchUtil.deleteById(ElasticsearchTestData.class, id);
        Assert.assertEquals(id, deletedId);
        ElasticsearchTestData getData1 = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNull(getData1);
    }

    // deleteById(T data, String indexName) testcase
    @Test
    public void testDeleteById3() {
        String name = "testDeleteById3";
        int num = 765512234;
        boolean judge = true;
        ElasticsearchTestData elasticsearchTestData = ElasticsearchTestData
                .builder()
                .name(name)
                .num(num)
                .judge(judge)
                .build();
        String id = elasticsearchUtil.insert(
                ElasticsearchUtil.getIndexName(ElasticsearchTestData.class),
                elasticsearchTestData
        );
        ElasticsearchTestData getData = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNotNull(getData);

        String deletedId = elasticsearchUtil.deleteById(
                getData, ElasticsearchUtil.getIndexName(ElasticsearchTestData.class)
        );
        Assert.assertEquals(id, deletedId);
        ElasticsearchTestData getData1 = elasticsearchUtil.queryById(ElasticsearchTestData.class, id);
        Assert.assertNull(getData1);
    }

    @After
    public void teardown() {
        elasticsearchUtil.deleteIndex(ElasticsearchTestData.class);
    }

}
