package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;


/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@NoArgsConstructor
public class HostDetailResponse {
    private String occurTime;
    private CpuStat cpuSample;
    private MemStat memSample;
    private SysLoadStat sysLoadSample;
    private NetIoStat netSample;

    public HostDetailResponse(List<Object> influxVal) {
        Instant parse = Instant.parse(String.valueOf(influxVal.get(0)));
        this.occurTime = parse.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);

        this.cpuSample = new CpuStat();
        cpuSample.setSystem((Double) influxVal.get(1));
        cpuSample.setUser((Double) influxVal.get(2));
        cpuSample.setIdle((Double) influxVal.get(3));
        cpuSample.setIowait((Double) influxVal.get(4));
        cpuSample.setIrq((Double) influxVal.get(5));
        cpuSample.setSoft((Double) influxVal.get(6));

        this.memSample = new MemStat();
        memSample.setTotal((Double) influxVal.get(7));
        memSample.setUsed((Double) influxVal.get(8));
        memSample.setUsePer((Double) influxVal.get(9));
        memSample.setFree((Double) influxVal.get(10));

        this.sysLoadSample = new SysLoadStat();
        sysLoadSample.setOneLoad((Double) influxVal.get(11));
        sysLoadSample.setFiveLoad((Double) influxVal.get(12));
        sysLoadSample.setFifteenLoad((Double) influxVal.get(13));

        this.netSample = new NetIoStat();
        netSample.setRxbyt(influxVal.get(14) != null ? ((Double) influxVal.get(14)).longValue() : null);
        netSample.setTxbyt(influxVal.get(15) != null ? ((Double) influxVal.get(15)).longValue() : null);
        netSample.setRxpck(influxVal.get(16) != null ? ((Double) influxVal.get(16)).longValue() : null);
        netSample.setTxpck(influxVal.get(17) != null ? ((Double) influxVal.get(17)).longValue() : null);
        netSample.setRxErrors(influxVal.get(18) != null ? ((Double) influxVal.get(18)).longValue() : null);
        netSample.setTxErrors(influxVal.get(19) != null ? ((Double) influxVal.get(19)).longValue() : null);
        netSample.setRxDrops(influxVal.get(20) != null ? ((Double) influxVal.get(20)).longValue() : null);
        netSample.setTxDrops(influxVal.get(21) != null ? ((Double) influxVal.get(21)).longValue() : null);
    }
}
