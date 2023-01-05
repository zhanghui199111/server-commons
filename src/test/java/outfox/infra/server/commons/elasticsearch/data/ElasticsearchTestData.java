package outfox.infra.server.commons.elasticsearch.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;


/**
 * author: zhn4528
 * create: 2020/12/7 13:53
*/
@Data
@Builder
@Document(indexName = "testcase")
public class ElasticsearchTestData implements Serializable {

    @Id
    private String id;

    private String name;

    private int num;

    private boolean judge;

}
