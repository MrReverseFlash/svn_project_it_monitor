package com.envisioniot.uscada.monitor.web.component;

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

    public Response postForServer(String url, JSONObject jsonObject, Integer retryNum) {
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
                return mapper.readValue(responseEntity.getBody(), new TypeReference<Response>() {
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException(String.format("url=%s get method failed.", url));
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
                log.error(e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException(String.format("url=%s get method failed.", url));
    }
}
