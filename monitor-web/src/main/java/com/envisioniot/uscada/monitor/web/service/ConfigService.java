package com.envisioniot.uscada.monitor.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * <p>Title: ConfigService</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/2/10 14:29
 */
@Service
@Slf4j
public class ConfigService {

    public JSONObject getFrontConfig() {
        File file = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            file = ResourceUtils.getFile("config/front.json");
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return JSON.parseObject(builder.toString());
        } catch (FileNotFoundException e) {
            log.error("file config/front.json not exist.", e);
            throw new RuntimeException("config file not exist.");
        } catch (IOException e) {
            log.error("read file config/front.json failed.", e);
            throw new RuntimeException("config file read error.");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
