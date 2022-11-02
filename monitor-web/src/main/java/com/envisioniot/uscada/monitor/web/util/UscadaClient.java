package com.envisioniot.uscada.monitor.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * UscadaClient
 *
 * @author yangkang
 * @date 2021/9/10
 */
public class UscadaClient {

//    private static final String GET_SCADA_STATUS = "/get_scada_status";
    private static final String SEND_ALARM = "/send_alarm";
    private static final String CONFIG_CENTER_SERVICE_PORT = "8892";
    private static final String SCADAWEB_CHECK_SESSION = "/scadaweb/checksession";
    private static final String SCADAWEB_SERVICE_PORT = "8701";
    private static final String CONTENT_TYPE_JSON = "application/json";
    //每个路由的最大连接数
    private static final int CONN_POOL_MAXPERROUTE = 10;
    //总共的最大连接数
    private static final int CONN_POOL_MAXTOTAL = 10;
    //从连接池获得连接最大等待时间
    private static final int CONN_LEASE_TIMEOUT_MILLIS = 5 * 1000;
    //与第三方服务创建连接最大等待时间
    private static final int CONN_ESTABLISH_TIMEOUT_MILLIS = 5 * 1000;
    //数据传输最大时长
    public static final int SOCKET_TIMEOUT_MILLIS = 30 * 1000;
    //连接最大空闲时长
    private static final int CONN_MAX_IDLETIME = 3;
    //最大重试次数
    private static final int MAX_RETRY_COUNT = 3;
    //HTTP的存活时间
    private static final int KEEP_ALIVE_TIME = 10 * 60 * 1000;

    private static final CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(CONN_POOL_MAXPERROUTE);
        connectionManager.setMaxTotal(CONN_POOL_MAXTOTAL);
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= MAX_RETRY_COUNT)
                    return false;
                if (exception instanceof HttpHostConnectException)
                    return true;
                return false;
            }
        };
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONN_LEASE_TIMEOUT_MILLIS)
                .setConnectTimeout(CONN_ESTABLISH_TIMEOUT_MILLIS)
                .setSocketTimeout(SOCKET_TIMEOUT_MILLIS)
                .setExpectContinueEnabled(false)
                .build();
        ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                Args.notNull(response, "HTTP response");
                final HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    final HeaderElement he = it.nextElement();
                    final String param = he.getName();
                    final String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(final NumberFormatException ignore) {
                        }
                    }
                }
                return KEEP_ALIVE_TIME;
            }
        };
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(CONN_MAX_IDLETIME, TimeUnit.MINUTES)
                .setConnectionManager(connectionManager)
                .setRetryHandler(retryHandler)
                .setKeepAliveStrategy(keepAliveStrategy)
                .disableCookieManagement()
                .build();
    }

    public static JSONObject sendAlarm(String ip, JSONObject jsonBody) {
        String url = "http://" + ip + ":" + CONFIG_CENTER_SERVICE_PORT + SEND_ALARM;
        return post(url, jsonBody);
    }

    public static JSONObject checkSession(String ip, String sessionId) {
        String url = "http://" + ip + ":" + SCADAWEB_SERVICE_PORT + SCADAWEB_CHECK_SESSION;
        ArrayList<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("sessionId", sessionId));
        return get(url, params);
    }

    private static JSONObject get(String url, List<NameValuePair> params) {
        return get(url, params, null);
    }

    private static JSONObject get(String url, Map<String, String> headers) {
        return get(url, null, headers);
    }

    private static JSONObject get(String url, List<NameValuePair> params, Map<String, String> headers) {
        HttpGet httpGet = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        String respBodyStr = "";
        JSONObject respJson = null;
        StringBuilder detailLog = new StringBuilder("");
        if (headers != null && !headers.isEmpty())
            detailLog.append(JSON.toJSONString(headers)).append("; ");
        if (params != null && !params.isEmpty())
            detailLog.append(JSON.toJSONString(params)).append("; ");
        int statusCode = -1;
        try {
            if (params != null && !params.isEmpty()){
                URIBuilder uriBuilder = new URIBuilder(url);
                uriBuilder.setParameters(params);
                httpGet = new HttpGet(uriBuilder.build());
            } else {
                httpGet = new HttpGet(url);
            }
            if (headers != null){
                for (Map.Entry<String, String> header : headers.entrySet()){
                    httpGet.addHeader(header.getKey(), header.getValue());
                }
            }
            httpGet.setHeader("Accept", CONTENT_TYPE_JSON);
            httpResponse = httpClient.execute(httpGet);
            entity = httpResponse.getEntity();
            if (entity != null)
                respBodyStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(HttpGet.METHOD_NAME + " " + url + " " + detailLog.toString(), e);
        } finally {
            try {
                //如果entity不为null，则能正常回收连接，下面两个不会生效；
                //如果entity为null（说明产生异常），此时执行下面两个中的一个可以保证能关闭连接，防止长期占用连接
                EntityUtils.consume(entity);
                if (httpResponse != null)
                    httpResponse.close();
                if (httpGet != null)
                    httpGet.releaseConnection();
            } catch (IOException e) {
                throw new RuntimeException(HttpGet.METHOD_NAME + " " + url + " " + detailLog.toString(), e);
            }
        }
        if (statusCode == HttpStatus.SC_OK) {
            if (respBodyStr == null)
                throw new RuntimeException(HttpGet.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " response empty");
            try {
                respJson = JSON.parseObject(respBodyStr);
            } catch (JSONException e){
                throw new RuntimeException(HttpGet.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " " + respBodyStr, e);
            }
            return respJson;
        }
        throw new RuntimeException(HttpGet.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " " + respBodyStr + " can't handle response");
    }

    private static JSONObject post(String url, JSONObject jsonBody) {
        return post(url, jsonBody, null, null);
    }

    private static JSONObject post(String url, JSONObject jsonBody, Map<String, String> headers) {
        return post(url, jsonBody, null, headers);
    }

    private static JSONObject post(String url, JSONObject jsonBody, List<NameValuePair> queryParams) {
        return post(url, jsonBody, queryParams, null);
    }

    private static JSONObject post(String url, JSONObject jsonBody, List<NameValuePair> queryParams, Map<String, String> headers) {
        HttpPost httpPost = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        String respBodyStr = "";
        JSONObject respJson = null;
        int statusCode = -1;
        StringBuilder detailLog = new StringBuilder("");
        if (headers != null && !headers.isEmpty())
            detailLog.append(JSON.toJSONString(headers)).append("; ");
        if (queryParams != null && !queryParams.isEmpty())
            detailLog.append(JSON.toJSONString(queryParams)).append("; ");
        if (jsonBody != null && !jsonBody.isEmpty())
            detailLog.append(JSON.toJSONString(jsonBody, SerializerFeature.WriteMapNullValue)).append("; ");
        try {
            if (queryParams != null && !queryParams.isEmpty()){
                URIBuilder uriBuilder = new URIBuilder(url);
                uriBuilder.setParameters(queryParams);
                httpPost = new HttpPost(uriBuilder.build());
            } else {
                httpPost = new HttpPost(url);
            }
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(jsonBody, SerializerFeature.WriteMapNullValue), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON);
            httpPost.setHeader("Accept", CONTENT_TYPE_JSON);
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    httpPost.addHeader(header.getKey(), header.getValue());
                }
            }
            httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            if (entity != null)
                respBodyStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(HttpPost.METHOD_NAME + " " + url + " " + detailLog.toString(), e);
        } finally {
            try {
                //如果entity不为null，则能正常回收连接，下面两个不会生效；
                //如果entity为null（说明产生异常），此时执行下面两个中的一个可以保证能关闭连接，防止长期占用连接
                EntityUtils.consume(entity);
                if (httpResponse != null)
                    httpResponse.close();
                if (httpPost != null)
                    httpPost.releaseConnection();
            } catch (IOException e) {
                throw new RuntimeException(HttpPost.METHOD_NAME + " " + url + " " + detailLog.toString(), e);
            }
        }
        if (statusCode == HttpStatus.SC_OK) {
            if (respBodyStr == null)
                throw new RuntimeException(HttpPost.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " response empty");
            try {
                respJson = JSON.parseObject(respBodyStr);
            } catch (JSONException e){
                throw new RuntimeException(HttpPost.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " " + respBodyStr, e);
            }
            return respJson;
        }
        throw new RuntimeException(HttpPost.METHOD_NAME + " " + url + " 200 " + detailLog.toString() + " " + respBodyStr + " can't handle response");
    }
}
