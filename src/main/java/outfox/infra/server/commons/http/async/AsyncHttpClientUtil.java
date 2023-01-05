package outfox.infra.server.commons.http.async;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import outfox.infra.server.commons.http.BaseHttpClientUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * author: zhn4528
 * create: 2020/12/2 14:32
*/
@Slf4j
public class AsyncHttpClientUtil extends BaseHttpClientUtil {

    private CloseableHttpAsyncClient closeableHttpAsyncClient;

    public AsyncHttpClientUtil(int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections) {
        super(connectionRequestTimeout, connectTimeout, socketTimeout, maxConnections);
        initPoolNIOHttpClientConnectionManager();
        closeableHttpAsyncClient = getHttpAsyncClient();
    }

    public AsyncHttpClientUtil(
            int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections, String cookieSpec
    ) {
        super(connectionRequestTimeout, connectTimeout, socketTimeout, maxConnections, cookieSpec);
        initPoolNIOHttpClientConnectionManager();
        closeableHttpAsyncClient = getHttpAsyncClient();
    }

    public Future<HttpResponse> get(String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams, final IHandler handler) throws IOException, URISyntaxException {

        HttpGet httpGet = new HttpGet(getURI(scheme, url, urlParams));    //使用Get方法提交
        httpGet.setHeaders(getHeaders(headerParams));
        return execute(closeableHttpAsyncClient, httpGet, handler);
    }

    public Future<HttpResponse> postJSON(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams,
            JSONObject requestBody, Charset charset, final IHandler handler
    ) throws URISyntaxException {
        if (Objects.isNull(headerParams)) {
            headerParams = new HashMap<String, String>(){{
                put("Content-Type", "application/json");
            }};
        } else {
            headerParams.put("Content-Type", "application/json");
        }
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));   //使用Post方法提交
        StringEntity entity = new StringEntity(requestBody.toJSONString(), charset);
        httpPost.setConfig(getRequestConfig());  //配置信息添加到Post请求中
        httpPost.setEntity(entity);              //配置信息添加到Post请求中
        httpPost.setHeaders(getHeaders(headerParams));
        return execute(closeableHttpAsyncClient, httpPost, handler);
    }

    private Future<HttpResponse> execute(CloseableHttpAsyncClient closeableHttpAsyncClient, HttpRequestBase httpRequest, final IHandler handler) {
        if( !closeableHttpAsyncClient.isRunning() ) {
            closeableHttpAsyncClient.start();
        }
        return closeableHttpAsyncClient.execute(httpRequest, new FutureCallback<HttpResponse>(){
            @Override
            public void completed(final HttpResponse response) {
                handler.completed(response);
                releaseConnection(httpRequest);
            }

            @Override
            public void failed(final Exception ex) {
                handler.failed(ex);
                releaseConnection(httpRequest);
            }

            @Override
            public void cancelled() {
                handler.cancelled();
                releaseConnection(httpRequest);
            }
        });
    }

    private static void close(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseConnection(HttpRequestBase httpRequest) {
        httpRequest.releaseConnection();
    }

}
