package com.envisioniot.uscada.monitor.agent.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import com.envisioniot.uscada.monitor.common.entity.TaskList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Configuration
@Slf4j
public class TaskConfig implements InitializingBean {

    @Value("${task.file}")
    private String taskFile;

    @Getter
    private volatile TaskList taskList = null;

    @Getter
    private String filePath;

    @Override
    public void afterPropertiesSet() {
        filePath = System.getProperty("user.dir") + File.separator + taskFile;
        if (!FileUtil.exist(filePath)) {
            filePath = ClassUtil.getClassPath() + File.separator + taskFile;
            if (!FileUtil.exist(filePath)) {
                throw new IllegalArgumentException(String.format("Both path={%s} and {%s} not exist!", filePath, System.getProperty("user.dir") + File.separator + taskFile));
            }
        }
        log.info("start to load yml file={}", filePath);
        loadYml();
        Assert.notNull(taskFile, "no valid schedule task in path=" + filePath);
        log.info("success to load yml file.");
    }

    private void loadYml() {
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = new FileInputStream(filePath);
            taskList = yaml.loadAs(inputStream, TaskList.class);
        } catch (FileNotFoundException e) {
            log.error("load yml file={} fail.", filePath);
            log.error(e.getMessage(), e);
        }
    }

    public void saveYml() {
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setPrettyFlow(false);
        Yaml yaml = new Yaml(options);
        if (taskList != null) {
            try (FileWriter writer = new FileWriter(filePath)) {
                yaml.dump(taskList, writer);
            } catch (IOException e) {
                log.error("save file={} fail.", filePath);
            }
        }
    }
}
