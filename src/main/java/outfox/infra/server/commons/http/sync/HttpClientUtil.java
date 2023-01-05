package outfox.infra.server.commons.http.sync;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import outfox.infra.server.commons.http.BaseHttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * author: zhn4528
 * create: 2020/12/2 14:29
 */
@Slf4j
public class HttpClientUtil extends BaseHttpClientUtil {

    private final CloseableHttpClient httpClient;

    public HttpClientUtil(int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections) {
        super(connectionRequestTimeout, connectTimeout, socketTimeout, maxConnections);
        initPoolingHttpClientConnectionManager();
        httpClient = getHttpClient();
    }

    public HttpClientUtil(
            int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections, String cookieSpec
    ) {
        super(connectionRequestTimeout, connectTimeout, socketTimeout, maxConnections, cookieSpec);
        initPoolingHttpClientConnectionManager();
        httpClient = getHttpClient();
    }

    public JSONObject get(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams
    ) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(getURI(scheme, url, urlParams));
        try {
            // 配置信息添加到请求中
            httpGet.setHeaders(getHeaders(headerParams));
            // 通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
            HttpResponse response = httpClient.execute(httpGet);
            return getJSONResponse(response);
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONObject get(String scheme, String url, Map<String, String> urlParams) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(getURI(scheme, url, urlParams));
        try {
            HttpResponse response = httpClient.execute(httpGet);
            return getJSONResponse(response);
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONObject post(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            httpPost.setHeaders(getHeaders(headerParams)); //配置信息添加到请求中
            HttpResponse response = httpClient.execute(httpPost);           //通过httpclient的execute提交 请求 ，并用HttpResponse接受返回信息
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    // 上传文件
    public JSONObject post(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams, String name, File file
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            httpPost.setHeaders(getHeaders(headerParams)); //配置信息添加到请求中
            MultipartEntityBuilder mBuilder = getCompatibleBuilder();
            mBuilder.addBinaryBody(name, file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity httpEntity = mBuilder.build();

            httpPost.setEntity(httpEntity); //配置信息添加到请求中
            HttpResponse response = httpClient.execute(httpPost); //通过httpclient的execute提交 请求 ，并用HttpResponse接受返回信息
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public JSONObject postJSON(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams,
            JSONObject requestBody, Charset charset
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            StringEntity entity = new StringEntity(requestBody.toJSONString(), charset);
            if (Objects.isNull(headerParams)) {
                headerParams = new HashMap<String, String>(){{
                   put("Content-Type", "application/json");
                }};
            } else {
                headerParams.put("Content-Type", "application/json");
            }
            httpPost.setHeaders(getHeaders(headerParams));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public JSONObject post(
            String scheme, String url, Map<String, String> urlParams
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            HttpResponse response = httpClient.execute(httpPost);
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public JSONObject post(
            String scheme, String url, Map<String, String> urlParams, String content
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            httpPost.setEntity(new StringEntity(content, StandardCharsets.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public JSONObject postWithFormUrlEncoded(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams,
            List<NameValuePair> formParams, Charset charset
    ) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(getURI(scheme, url, urlParams));
        try {
            httpPost.setHeaders(getHeaders(headerParams));
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, charset));
            HttpResponse response = httpClient.execute(httpPost);
            return getJSONResponse(response);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public JSONObject delete(
            String scheme, String url, Map<String, String> urlParams, Map<String, String> headerParams,
            Charset charset
    ) throws IOException, URISyntaxException {
        HttpDelete httpDelete = new HttpDelete(getURI(scheme, url, urlParams));
        try {
            httpDelete.setHeaders(getHeaders(headerParams));
            HttpResponse response = httpClient.execute(httpDelete);
            return getJSONResponse(response);
        } finally {
            httpDelete.releaseConnection();
        }
    }

}
