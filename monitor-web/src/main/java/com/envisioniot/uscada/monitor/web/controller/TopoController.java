package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import com.envisioniot.uscada.monitor.web.service.TopoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TopoController
 *
 * @author yangkang
 * @date 2021/3/2
 */
@RestController
@RequestMapping("/topoInfo")
public class TopoController {

    @Autowired
    private TopoService topoService;

    @GetMapping("/qryTopoInfo")
    public Response qryTopoInfo() {
        List<TopoInfo> topoInfos = topoService.qryTopoInfo();
        return new Response(ResponseCode.SUCCESS.getCode(), topoInfos);
    }

    @GetMapping("/insCustRela")
    public Response insCustRelaByHost(@RequestParam("hostIpA") String hostIpA,
                                      @RequestParam("hostIpB") String hostIpB) {
        if (StringUtils.isEmpty(hostIpA) || StringUtils.isEmpty(hostIpB)) {
            throw new WebRequestException("主机IP不能为空！");
        }
        topoService.insCustRelaByHost(hostIpA, hostIpB);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/delCustRela")
    public Response delCustRelaByHost(@RequestParam("hostIpA") String hostIpA,
                                      @RequestParam("hostIpB") String hostIpB) {
        if (StringUtils.isEmpty(hostIpA) || StringUtils.isEmpty(hostIpB)) {
            throw new WebRequestException("主机IP不能为空！");
        }
        topoService.delCustRelaByHost(hostIpA, hostIpB);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/qryTopoParam")
    public Response qryTopoParamByType(@RequestParam("type") short type) {
        if (type != 1 && type != 2 && type != 3) {
            throw new WebRequestException("类型参数值不在允许范围！");
        }
        List<TopoParam> topoParams = topoService.qryTopoParamByType(type);
        return new Response(ResponseCode.SUCCESS.getCode(), topoParams);
    }

    @PostMapping("/insTopoParam")
    public Response insTopoParam(@RequestBody Map<String, Object> data) {
        short type = (short) (int) data.get("type");
        short num = (short) (int) data.get("num");
        String name = (String) data.get("name");
        if (type != 1 && type != 2 && type != 3) {
            throw new WebRequestException("类型参数值不在允许范围！");
        }
        return new Response(ResponseCode.SUCCESS.getCode(), topoService.insTopoParam(num, name, type),"");
    }

    @GetMapping("/updTopoParam")
    public Response updTopoParam(@RequestParam("id") String id,
                                 @RequestParam(value = "num", required = false) Short num,
                                 @RequestParam(value = "name", required = false) String name) {
        topoService.updTopoParam(id, num, name);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/updHostParamId")
    public Response updHostParamId(@RequestParam("id") String id,
                                   @RequestParam("type") short type,
                                   @RequestBody List<String> list) {
        if (type != 1 && type != 2 && type != 3) {
            throw new WebRequestException("类型参数值不在允许范围！");
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new WebRequestException("主机列表不能为空！");
        }
        topoService.updHostParamId(id, type, list);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/updHostCoord")
    public Response updHostCoord(@RequestBody List<TopoInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new WebRequestException("主机列表不能为空！");
        }
        topoService.updHostCoordinate(list);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/delHostParamId")
    public Response delHostParamId(@RequestParam("type") short type,
                                   @RequestBody List<String> list) {
        if (type != 1 && type != 2 && type != 3) {
            throw new WebRequestException("类型参数值不在允许范围！");
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new WebRequestException("主机列表不能为空！");
        }
        topoService.delHostParamId(type, list);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/delTopoParam")
    public Response delTopoParam(@RequestParam("id") String id,
                                 @RequestParam("type") short type) {
        if (type != 1 && type != 2 && type != 3) {
            throw new WebRequestException("类型参数值不在允许范围！");
        }
        topoService.delTopoParam(id, type);
        return new Response(ResponseCode.SUCCESS.getCode());
    }
}
