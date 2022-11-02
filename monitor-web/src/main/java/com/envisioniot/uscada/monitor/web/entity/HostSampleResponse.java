package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.common.entity.CpuStat;
import com.envisioniot.uscada.monitor.common.entity.MemStat;
import com.envisioniot.uscada.monitor.common.entity.SwapStat;
import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
@NoArgsConstructor
public class HostSampleResponse {
    private String occurTime;
    private CpuStat cpuSample;
    private MemStat memSample;
    private SwapStat swapSample;

    public HostSampleResponse(List<Object> influxVal) {
        Instant parse = Instant.parse(String.valueOf(influxVal.get(0)));
        this.occurTime = parse.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);

        this.cpuSample = new CpuStat();
        cpuSample.setSystem((Double) influxVal.get(1));
        cpuSample.setUser((Double) influxVal.get(2));
        cpuSample.setIdle((Double) influxVal.get(3));
        cpuSample.setIowait((Double) influxVal.get(4));
        cpuSample.setIrq((Double) influxVal.get(5));
        cpuSample.setSoft((Double) influxVal.get(6));
        cpuSample.setProcessNum(((Double) influxVal.get(16)).intValue());

        this.memSample = new MemStat();
        memSample.setTotal((Double) influxVal.get(7));
        memSample.setUsed((Double) influxVal.get(8));
        memSample.setUsePer((Double) influxVal.get(9));
        memSample.setFree((Double) influxVal.get(10));

        this.swapSample = new SwapStat();
        swapSample.setSwapTotal((Double) influxVal.get(11));
        swapSample.setSwapUsed((Double) influxVal.get(12));
        swapSample.setSwapPer((Double) influxVal.get(13));
        swapSample.setSwapPageIn(influxVal.get(14) != null ? ((Double) influxVal.get(14)).longValue() : null);
        swapSample.setSwapPageOut(influxVal.get(15) != null ? ((Double) influxVal.get(15)).longValue() : null);
    }
}
