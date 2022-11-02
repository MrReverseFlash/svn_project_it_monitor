package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dbInfo")
public class DbController {

	private static final Logger logger = LoggerFactory.getLogger(DbController.class);
	
    @Resource
    private DbService dbService;

	/**
     * 查询监控的数据库列表
     * @return
     */
	@GetMapping("/list")
	public Response dbInfoList(@RequestParam(name = "hostIp", required = false) String hostIp) {
		List<DbInfo> dbInfos = dbService.qryDbList(hostIp);
		return new Response(ResponseCode.SUCCESS.getCode(), dbInfos);
	}

	/**
	 * 测试数据库连接
	 * @return
	 */
	@PostMapping("/validate")
	public Response testDbConn(@RequestBody DbConnTestReq req) {
		return dbService.testDbConn(req);
	}

	/**
     * 保存数据源信息
     * @return
     */
    @PostMapping("/save")
    public Response saveDbInfo(@RequestBody DbInfo dbInfo) {
		 if(dbInfo.getId() == null) {
			 dbService.save(dbInfo);
		 }else{
			 dbService.updateById(dbInfo);
		 }
		 return new Response(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 删除数据源
     * @return
     */
    @PostMapping(value="del")
    public Response delete(@RequestBody List<String> ids) {
		if (!CollectionUtils.isEmpty(ids)) {
			dbService.deleteByIds(ids);
		}
		return new Response(ResponseCode.SUCCESS.getCode());
    }

	/**
	 * 数据库采样信息（历史）查询
	 * @param dbSampleReq
	 * @return
	 */
	@PostMapping("/getDbSample")
	public Response getDbSample(@RequestBody DbSampleReq dbSampleReq) {
		List<DbSampleResp> sample = dbService.getDbSample(dbSampleReq);
		return new Response(ResponseCode.SUCCESS.getCode(), sample);
	}



	/**
	 * 查询所有表监控
	 * @return
	 */
	@GetMapping("/tableList")
	public Response tableList() {
		List<TableInfo> tableInfoList = dbService.qryAllTable();
		return new Response(ResponseCode.SUCCESS.getCode(), tableInfoList);
	}

	/**
	 * 保存更新表监控信息
	 * @return
	 */
	@PostMapping("/saveTable")
	public Response saveTableInfo(@RequestBody SaveTableReq saveTableReq) {
		if(saveTableReq.getId() == null) {
			dbService.saveTableInfo(saveTableReq);
		}else{
			dbService.updTableById(saveTableReq);
		}
		return new Response(ResponseCode.SUCCESS.getCode());
	}

	/**
	 * 删除表监控
	 * @param ids
	 * @return
	 */
	@PostMapping("/delTable")
	public Response delTable(@RequestBody List<Long> ids) {
		if (!CollectionUtils.isEmpty(ids)) {
			dbService.delTableByIds(ids);
		}
		return new Response(ResponseCode.SUCCESS.getCode());
	}

	/**
	 * 表采样信息（历史）查询
	 * @param tableSampleReq
	 * @return
	 */
	@PostMapping("/getTableSample")
	public Response getTableSample(@RequestBody TableSampleReq tableSampleReq) {
		List<TableSampleResp> sample = dbService.getTableSample(tableSampleReq);
		return new Response(ResponseCode.SUCCESS.getCode(), sample);
	}


	@GetMapping("/getDbAlarm")
	public Response getDbAlarm(@RequestParam("st") String st,
							   @RequestParam("et") String et,
							   @RequestParam("dbId") String dbId) {
		return new Response(ResponseCode.SUCCESS.getCode(), dbService.getDbAlarm(st, et, dbId));
	}
}
