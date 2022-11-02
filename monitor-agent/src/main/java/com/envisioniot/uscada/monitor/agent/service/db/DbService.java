package com.envisioniot.uscada.monitor.agent.service.db;

import com.envisioniot.uscada.monitor.common.entity.DbInfo;
import com.envisioniot.uscada.monitor.common.entity.DbType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
public interface DbService {

    /**
     * @return 获取数据库连接DataSource
     */
    public DataSource getDataSource(DbInfo db) throws SQLException;

    /**
     * @return 获取数据库类型
     */
    public DbType getDbType();


    /**
     * @return 数据库版本号
     */
    public String getVersion(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库最大连接数
     */
    public Integer getMaxConn(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库已经使用连接数
     */
    public Integer getUseCon(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库启动时间s
     */
    public Long getUptime(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库questions次数
     */
    public Long getQuestions(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库commit次数
     */
    public Long getCommitNum(JdbcTemplate jdbcTemplate);

    /**
     * @return 数据库rollback次数
     */
    public Long getRollbackNum(JdbcTemplate jdbcTemplate);

}
