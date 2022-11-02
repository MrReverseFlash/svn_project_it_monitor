package com.envisioniot.uscada.monitor.agent.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


/**
 * @author hao.luo
 */
@Component
@Slf4j
public class HttpComponent {

    @Autowired
    private RestTemplate restTemplate;

    public Object postForServer(String url, JSONObject jsonObject, Integer retryNum) {
        ObjectMapper mapper = new ObjectMapper();
        // 后续可以添加接口验证的功能， MD5方式去验证
//        if (null != jsonObject) {
//            jsonObject.putOpt(TOKEN_KEY, MD5Utils.GetMD5Code(TOKEN_VAL));
//        }
        for (int i = 0; i < retryNum; i++) {
            try {
                HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
                HttpEntity<String> httpEntity = new HttpEntity<>(JSONUtil.parse(jsonObject).toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
                Response response = mapper.readValue(responseEntity.getBody(), new TypeReference<Response>() {
                });
                if (ResponseCode.SUCCESS.getCode() == response.getCode()) {
                    return response.getData();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException(String.format("url=%s get method failed.", url));
    }

    public JSONObject post(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        return JSONUtil.parseObj(responseEntity.getBody());
    }

    public int post(String url, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
            HttpEntity<String> httpEntity = new HttpEntity<>(body != null ? body : "", headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            return responseEntity.getStatusCodeValue();
        } catch (HttpClientErrorException e) {
            log.error("服务接口检测任务错误", e);
            return e.getRawStatusCode();
        } catch (Exception e) {
            log.error("服务接口检测任务错误", e);
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public int get(String url) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return responseEntity.getStatusCodeValue();
        } catch (HttpClientErrorException e) {
            log.error("服务接口检测任务错误", e);
            return e.getRawStatusCode();
        } catch (Exception e) {
            log.error("服务接口检测任务错误", e);
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public Object getApi(String url, Integer retryNum) {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < retryNum; i++) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            String body = responseEntity.getBody();
            try {
                Response response = mapper.readValue(body, new TypeReference<Response>() {
                });
                if (ResponseCode.SUCCESS.getCode() == response.getCode()) {
                    return response.getData();
                }
            } catch (IOException e) {
                log.error("url={}", url);
                log.error(e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException(String.format("url=%s get method failed.", url));
    }
}
