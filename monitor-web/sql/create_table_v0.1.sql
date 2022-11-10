-- TODO 20220114 建表语句适配FDB
-- ----------------------------
-- Table structure for monitor_host_info
-- ----------------------------
 CREATE TABLE IF NOT EXISTS `monitor_host_info` (
     id BIGINT(10) NOT NULL AUTO_INCREMENT,
     host_ip VARCHAR(32) NOT NULL COMMENT 'agent ip' ,
     port VARCHAR(32) DEFAULT NULL COMMENT 'agent port',
     host_name VARCHAR(64) DEFAULT NULL,
     label VARCHAR(255) DEFAULT NULL COMMENT '备注',
     status INT(3) DEFAULT NULL COMMENT '主机状态',
     sys_version VARCHAR(128) DEFAULT NULL COMMENT '操作系统版本',
     sys_detail VARCHAR(255) DEFAULT NULL COMMENT '操作系统描述',
     cpu_type VARCHAR(255) DEFAULT NULL COMMENT 'cpu型号',
     cpu_core_num INT(5) DEFAULT NULL COMMENT 'cpu总核数',
     cpu_per DECIMAL(22,6) DEFAULT NULL COMMENT 'cpu使用率%',
     cpu_temperature DECIMAL(22,6) DEFAULT NULL COMMENT 'cpu温度',
     mem_per DECIMAL(22,6) DEFAULT NULL COMMENT '内存使用率%',
     total_mem DECIMAL(22,6) DEFAULT NULL COMMENT '总内存M',
     process_num INT(5) DEFAULT NULL COMMENT '总运行进程数量',
     zombie_num INT(5) DEFAULT NULL COMMENT '僵尸进程数量',
     swap_per DECIMAL(22,6) DEFAULT NULL COMMENT 'swap使用率',
     swap_total DECIMAL(22,6) DEFAULT NULL COMMENT 'swap容量M',
     net_max_per DECIMAL(22,6) DEFAULT NULL COMMENT '网卡中最大网络使用率%',
     total_bandwidth DECIMAL(22,6) DEFAULT NULL COMMENT '总带宽kbps',
     total_netflow DECIMAL(22,6) DEFAULT NULL COMMENT '网络流量kbps',
     net_per DECIMAL(22,6) DEFAULT NULL COMMENT '主机网络使用率%',
     disk_per DECIMAL(22,6) DEFAULT NULL COMMENT '磁盘使用率%',
     disk_use DECIMAL(22,6) DEFAULT NULL COMMENT '磁盘使用GB',
     disk_total DECIMAL(22,6) DEFAULT NULL COMMENT '磁盘总容量GB',
     inodes_per DECIMAL(22,6) DEFAULT NULL COMMENT 'inodes使用率%',
     start_time DATETIME NULL DEFAULT NULL COMMENT '系统启动时间',
     login_user_num INT(5) DEFAULT NULL COMMENT '当前登录用户个数',
     run_time BIGINT(10)  DEFAULT NULL COMMENT '系统运行时间单位秒',
     occur_time DATETIME DEFAULT NULL,
     update_time DATETIME DEFAULT NULL,
     create_time DATETIME DEFAULT NULL,
     type INT(3) DEFAULT 1 COMMENT 'agent机器连接方式',
     uscada_status     VARCHAR(255) DEFAULT NULL COMMENT 'SCADA状态列表格式为 IP1:STATE1;IP2:STATE2;IP3:STATE3 ，如 192.168.0.1:1;192.168.0.2:0',
     health_status     smallint(4)   NULL COMMENT '设备健康度',
     match_flag VARCHAR(32) NOT NULL COMMENT '标记本条主机信息所属层级(如一区、三区，主机状态告警扫描只会扫描本层级主机)' ,
     PRIMARY KEY (id),
     UNIQUE INDEX idx_monitor_host_info_ip (host_ip)
 );

 -- ----------------------------
 -- Table structure for monitor_app_info
 -- ----------------------------
 CREATE TABLE IF NOT EXISTS `monitor_app_info` (
     id BIGINT(10) NOT NULL AUTO_INCREMENT,
     host_ip VARCHAR(32) NOT NULL COMMENT 'agent ip' ,
     app_uid VARCHAR(500) NOT NULL COMMENT 'app 唯一识别标识符',
     app_pid BIGINT(10)  DEFAULT NULL COMMENT 'app id' ,
     app_name VARCHAR(128) DEFAULT NULL,
     container_name VARCHAR(255) DEFAULT NULL,
     status INT(3) DEFAULT NULL COMMENT 'app状态',
     start_time VARCHAR(64) DEFAULT NULL COMMENT 'app启动时间',
     mem_per DECIMAL(22,6) DEFAULT NULL COMMENT '内存使用率%',
     mem_use DECIMAL(22,6) DEFAULT NULL COMMENT '内存使用MB',
     cpu_per DECIMAL(22,6) DEFAULT NULL COMMENT 'cpu使用率%',
     io_read BIGINT(10) DEFAULT NULL COMMENT '磁盘读KB',
     io_write BIGINT(10) DEFAULT NULL COMMENT '磁盘写KB',
     thread_num INT(5) DEFAULT NULL COMMENT '线程个数',
     msg VARCHAR(1024) DEFAULT NULL,
     occur_time DATETIME DEFAULT NULL,
     update_time DATETIME DEFAULT NULL,
     create_time DATETIME DEFAULT NULL,
     PRIMARY KEY (id),
     UNIQUE INDEX idx_monitor_app_info_ip(host_ip, app_uid(255))
 );

-- ----------------------------
-- Table structure for monitor_docker_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_docker_info` (
  id BIGINT(10) NOT NULL AUTO_INCREMENT,
  host_ip VARCHAR(32) NOT NULL COMMENT 'agent ip' ,
  container_id VARCHAR(64) NOT NULL,
  container_name VARCHAR(255) DEFAULT NULL,
  status INT(3) DEFAULT NULL COMMENT '容器状态',
  uptime VARCHAR(128) DEFAULT NULL COMMENT '容器启动时间',
  container_create_time VARCHAR(128) DEFAULT NULL COMMENT '容器创建时间',
  mem_per DECIMAL(22,6) DEFAULT NULL COMMENT '内存使用率%',
  cpu_per DECIMAL(22,6) DEFAULT NULL COMMENT 'cpu使用率%',
  net_io_send BIGINT(10) DEFAULT NULL COMMENT '网络接口发送数据KB',
  net_io_receive BIGINT(10) DEFAULT NULL COMMENT '网络接口接受数据量KB ',
  block_io_read BIGINT(10) DEFAULT NULL COMMENT '块设备读取数据量KB',
  block_io_write BIGINT(10) DEFAULT NULL COMMENT '块设备写入的数据量KB',
  pids INT(5) DEFAULT NULL COMMENT '容器中进程个数',
  msg VARCHAR(1024) DEFAULT NULL,
  image VARCHAR(255) DEFAULT NULL,
  occur_time DATETIME DEFAULT NULL,
  update_time DATETIME  DEFAULT NULL,
  create_time DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_docker_info_ip(host_ip, container_id)
);


-- ----------------------------
-- Table structure for monitor_port_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_port_info` (
  id BIGINT(10) NOT NULL AUTO_INCREMENT,
  host_ip VARCHAR(32) NOT NULL,
  port VARCHAR(32) NOT NULL COMMENT '监控本机端口号',
  mark VARCHAR(255) DEFAULT NULL COMMENT '描述',
  port_type VARCHAR(8) COMMENT '端口类型',
  status INT(3) DEFAULT NULL COMMENT '端口状态',
  listening_num INT(5) DEFAULT NULL,
  established_num INT(5) DEFAULT NULL,
  timewait_num INT(5) DEFAULT NULL,
  closewait_num INT(5) DEFAULT NULL,
  synsent_num INT(5) DEFAULT NULL,
  idle_num INT(5) DEFAULT NULL,
  msg VARCHAR(1024) DEFAULT NULL,
  occur_time DATETIME DEFAULT NULL,
  update_time DATETIME  DEFAULT NULL,
  create_time DATETIME DEFAULT NULL COMMENT '监控创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_port_info_ip(host_ip, port)
);

-- ----------------------------
-- Table structure for monitor_http_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_http_info` (
  id BIGINT(10) NOT NULL AUTO_INCREMENT,
  host_ip VARCHAR(32) NOT NULL,
  url VARCHAR(128) NOT NULL,
  service_name VARCHAR(128) DEFAULT NULL,
  method VARCHAR(8) DEFAULT NULL,
  body VARCHAR(512) DEFAULT NULL,
  status INT(3) DEFAULT NULL COMMENT '监控状态',
  response_code BIGINT(10) DEFAULT NULL COMMENT '接口响应码',
  response_time BIGINT(10) DEFAULT NULL COMMENT '响应时间毫秒',
  msg VARCHAR(1024) DEFAULT NULL,
  occur_time DATETIME DEFAULT NULL,
  update_time DATETIME  DEFAULT NULL,
  create_time DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_http_info_ip(host_ip, url)
);

-- ----------------------------
-- Table structure for monitor_db_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_db_info` (
  id VARCHAR(36) NOT NULL,
  host_ip VARCHAR(32) NOT NULL,
  db_ip VARCHAR(32) NOT NULL,
  db_type VARCHAR(32) DEFAULT NULL,
  db_user VARCHAR(64) DEFAULT NULL,
  db_passwd VARCHAR(64) DEFAULT NULL,
  db_port VARCHAR(32) DEFAULT NULL,
  db_name VARCHAR(32) DEFAULT NULL,
  status INT(3) DEFAULT NULL COMMENT 'db状态',
  db_version VARCHAR(128) DEFAULT NULL,
  up_time BIGINT(10) DEFAULT NULL,
  questions BIGINT(10) DEFAULT NULL,
  commit_num BIGINT(10) DEFAULT NULL,
  rollback_num BIGINT(10) DEFAULT NULL,
  conn_max BIGINT(10) DEFAULT NULL,
  use_con BIGINT(10) DEFAULT NULL,
  msg VARCHAR(1024) DEFAULT NULL,
  occur_time DATETIME DEFAULT NULL,
  update_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT NULL,
  db_tag varchar(64) default null COMMENT '数据源标签（别名）',
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_db_info_ip(host_ip, db_ip, db_type, db_name)
);

-- ----------------------------
-- Table structure for monitor_table_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_table_info` (
  id BIGINT(10) NOT NULL AUTO_INCREMENT,
  db_id VARCHAR(36) NOT NULL,
  table_name VARCHAR(64) DEFAULT NULL,
  description VARCHAR(255) DEFAULT NULL,
  exec_sql VARCHAR(1024) DEFAULT NULL,
  record_num BIGINT(10) DEFAULT NULL,
  update_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_table_info_ip(db_id, table_name)
);

-- ----------------------------
-- Table structure for monitor_fe_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `monitor_fe_info` (
  id BIGINT(10) NOT NULL AUTO_INCREMENT,
  host_ip VARCHAR(32) NOT NULL,
  ied_alias VARCHAR(64) DEFAULT NULL,
  ied_name VARCHAR(128) DEFAULT NULL,
  peer_ip VARCHAR(32) NOT NULL COMMENT '对端ip',
  port VARCHAR(32) DEFAULT NULL,
  comm_type INT(3) NOT NULL COMMENT '端口类型',
  status INT(3) DEFAULT NULL COMMENT '通讯状态',
  msg VARCHAR(1024) DEFAULT NULL,
  occur_time DATETIME DEFAULT NULL,
  update_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX idx_monitor_fe_info_ip(host_ip, peer_ip, port)
);

-- 二期：拓扑展示新增表
-- 拓扑单元信息表
CREATE TABLE IF NOT EXISTS monitor_topo_info
(
  host_ip           varchar(32)   NOT NULL COMMENT '主机IP',
  topo_level_id     varchar(36)       NULL COMMENT '拓扑层级id',
  topo_set_id       varchar(36)       NULL COMMENT '拓扑集合id',
  topo_physic_id    varchar(36)       NULL COMMENT '拓扑物理id',
  has_agent         smallint(4)   NULL COMMENT '是否部署Agent程序',
  x                 float(8,4)    NULL COMMENT 'X轴坐标',
  y                 float(8,4)    NULL COMMENT 'Y轴坐标',
  update_time       datetime      NOT NULL COMMENT '更新时间',
  create_time       datetime      NOT NULL COMMENT '创建时间',
  CONSTRAINT idx_topo_info
    UNIQUE (host_ip)
);

-- 拓扑参数表
CREATE TABLE IF NOT EXISTS monitor_topo_param
(
  id    varchar(36)       PRIMARY KEY  COMMENT '参数id',
  num   smallint(4)   NOT NULL COMMENT '序号',
  name  varchar(64)   NOT NULL COMMENT '名称',
  type  smallint(4)   NOT NULL COMMENT '参数类型（1:level; 2:set; 3:physic）'
);

-- 拓扑连线表
CREATE TABLE IF NOT EXISTS monitor_topo_rela
(
  host_ip_src       varchar(32)   NOT NULL COMMENT '源主机IP',
  host_ip_target    varchar(32)   NOT NULL COMMENT '目标主机IP',
  status            smallint(4)   NULL COMMENT '源主机ping目标主机的状态',
  operation_millis  smallint(4)   NULL COMMENT 'ping耗时',
  is_default        smallint(4)   NULL COMMENT '是否默认生成的关系',
  update_time       datetime      NOT NULL COMMENT '更新时间',
  create_time       datetime      NOT NULL COMMENT '创建时间',
  CONSTRAINT idx_topo_rela
    UNIQUE (host_ip_src, host_ip_target)
);

-- 拓扑单元网关信息表
CREATE TABLE IF NOT EXISTS monitor_gateway_info
(
  host_ip           varchar(32)   NOT NULL COMMENT '主机IP',
  gateway_ip        varchar(32)   NOT NULL COMMENT '网关IP',
  status            smallint(4)   NULL COMMENT '主机ping网关的状态',
  operation_millis  smallint(4)   NULL COMMENT 'ping耗时',
  update_time       datetime      NOT NULL COMMENT '更新时间',
  create_time       datetime      NOT NULL COMMENT '创建时间',
  CONSTRAINT idx_gateway_info
    UNIQUE (host_ip, gateway_ip)
);

-- 主机告警历史表
CREATE TABLE IF NOT EXISTS monitor_host_alarm_his
(
    host_ip           varchar(32)   NOT NULL COMMENT '主机IP',
    occur_time        datetime      NOT NULL COMMENT '告警产生时间',
    alarm_type        int(3)        NOT NULL COMMENT '告警类型（1：CPU；2：内存；3：硬盘；4：主机状态；）',
    alarm_content     varchar(1024) NOT NULL COMMENT '告警内容',
    INDEX idx_monitor_host_alarm_his_1(host_ip, occur_time)
);

-- 数据库告警历史表
CREATE TABLE IF NOT EXISTS monitor_db_alarm_his
(
    db_id             varchar(36)    NOT NULL COMMENT '数据库ID',
    occur_time        datetime      NOT NULL COMMENT '告警产生时间',
    alarm_type        int(3)        NOT NULL COMMENT '告警类型（5：数据库状态；）',
    alarm_content     varchar(1024) NOT NULL COMMENT '告警内容',
    INDEX idx_monitor_db_alarm_his_1(db_id, occur_time)
);