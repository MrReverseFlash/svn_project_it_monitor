package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.envisioniot.uscada.monitor.common.util.InfluxConstants.*;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
@Repository
@Slf4j
public class HostInfluxDao {
    @Autowired
    private InfluxDB influxdb;

    public void insertHis(HostStat sample) {
        try {
            String occurTime = sample.getOccurTime();
            long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
            if (sample.getStatus() == CommConstants.ONLINE_STATUS) {
                insertHostHis(sample, timestamp);
                insertDiskHis(sample.getDiskList(), timestamp, sample.getHostIp());
                insetNetHis(sample.getNetSample(), timestamp, sample.getHostIp());
            }
        } catch (Exception e) {
            log.error("sample={} host sample save in influxdb error.", sample);
            log.error(e.getMessage(), e);
        }
    }

    public void insertHostHis(HostStat sample, long timestamp) {
        Point.Builder builder = Point.measurement(INFLUX_HOST_HIS)
                .time(timestamp, TimeUnit.MILLISECONDS)
                .tag(TAG_IP, sample.getHostIp())
                .addField(FIELD_LOGIN_USER_NUM, sample.getLogUserNum());
        SystemInfo systemInfo = sample.getSystemInfo();
        if (systemInfo != null) {
            builder.addField(FIELD_PROCESS_NUM, systemInfo.getProcessNum())
                    .addField(FIELD_PROCESS_ZOMBIE, systemInfo.getZombieNum());
        }
        CpuStat cpuSample = sample.getCpuSample();
        if (cpuSample != null) {
            builder.addField(FIELD_CPU_SYS, cpuSample.getSystem())
                    .addField(FIELD_CPU_USER, cpuSample.getUser())
                    .addField(FIELD_CPU_IDLE, cpuSample.getIdle())
                    .addField(FIELD_CPU_IOWAIT, cpuSample.getIowait())
                    .addField(FIELD_CPU_IRQ, cpuSample.getIrq())
                    .addField(FIELD_CPU_SOFT, cpuSample.getSoft());
        }
        MemStat memSample = sample.getMemSample();
        if (memSample != null) {
            builder.addField(FIELD_MEM_TOTAL, memSample.getTotal())
                    .addField(FIELD_MEM_USE, memSample.getUsed())
                    .addField(FIELD_MEM_FREE, memSample.getFree())
                    .addField(FIELD_MEM_PER, memSample.getUsePer());
        }
        SysLoadStat sysLoadSample = sample.getSysLoadSample();
        if (sysLoadSample != null) {
            builder.addField(FIELD_ONE_LOAD, sysLoadSample.getOneLoad())
                    .addField(FIELD_FIVE_LOAD, sysLoadSample.getFiveLoad())
                    .addField(FIELD_FIFTEEN_LOAD, sysLoadSample.getFifteenLoad());
        }
        SwapStat swapSample = sample.getSwapSample();
        if (swapSample != null) {
            builder.addField(FIELD_SWAP_TOTAL, swapSample.getSwapTotal())
                    .addField(FIELD_SWAP_USED, swapSample.getSwapUsed())
                    .addField(FIELD_SWAP_PER, swapSample.getSwapPer())
                    .addField(FIELD_SWAP_PAGE_IN, swapSample.getSwapPageIn())
                    .addField(FIELD_SWAP_PAGE_OUT, swapSample.getSwapPageOut());
        }
        NetTotalInfo netTotal = sample.getNetTotal();
        if (netTotal != null) {
            builder.addField(FIELD_NET_MAX_PER, netTotal.getNetMaxPer())
                    .addField(FIELD_TOTAL_BANDWIDTH, netTotal.getTotalBandWidth())
                    .addField(FIELD_TOTAL_NETFLOW, netTotal.getTotalNetFlow())
                    .addField(FIELD_NET_PER, netTotal.getNetPer());
        }
        DiskTotalInfo diskTotal = sample.getDiskTotal();
        if (diskTotal != null) {
            builder.addField(FIELD_DISK_PER, diskTotal.getDiskPer())
                    .addField(FIELD_DISK_USE, diskTotal.getDiskUse())
                    .addField(FIELD_DISK_TOTAL, diskTotal.getDiskTotal())
                    .addField(FIELD_INODES_PER, diskTotal.getInodesPer());
        }
        Point point = builder.build();
        influxdb.write(point);
    }

    private void insetNetHis(List<NetIoStat> netList, long timestamp, String ip) {
        if (CollectionUtil.isEmpty(netList)) {
            log.error("no net card sample information.");
            return;
        }
        netList.forEach(net -> {
            Point.Builder builder = Point.measurement(INFLUX_NET_HIS)
                    .time(timestamp, TimeUnit.MILLISECONDS)
                    .tag(TAG_IP, ip)
                    .tag(TAG_NET_CARD_NAME, net.getIfName())
                    .addField(FIELD_RXBYT, net.getRxbyt())
                    .addField(FIELD_TXBYT, net.getTxbyt())
                    .addField(FIELD_RXPCK, net.getRxpck())
                    .addField(FIELD_TXPCK, net.getTxpck())
                    .addField(FIELD_RXDROPS, net.getRxDrops())
                    .addField(FIELD_TXDROPS, net.getTxDrops())
                    .addField(FIELD_RXERRORS, net.getRxErrors())
                    .addField(FIELD_TXERRORS, net.getTxErrors())
                    .addField(FIELD_TOTAL_BANDWIDTH, net.getTotalBandWidth())
                    .addField(FIELD_TOTAL_NETFLOW, net.getTotalNetFlow())
                    .addField(FIELD_TOTAL_NET_IN, net.getTotalNetIn())
                    .addField(FIELD_TOTAL_NET_OUT, net.getTotalNetOut());
            Point point = builder.build();
            influxdb.write(point);
        });
        long rxbyt, txbyt, rxpck, txpck, rxdrops, txdrops, rxerrors, txerrors;
        rxbyt = netList.stream().mapToLong(net -> net.getRxbyt() != -1 ? net.getRxbyt() : 0).sum();
        txbyt = netList.stream().mapToLong(net -> net.getTxbyt() != -1 ? net.getTxbyt() : 0).sum();
        rxpck = netList.stream().mapToLong(net -> net.getRxpck() != -1 ? net.getRxpck() : 0).sum();
        txpck = netList.stream().mapToLong(net -> net.getTxpck() != -1 ? net.getTxpck() : 0).sum();
        rxdrops = netList.stream().mapToLong(net -> net.getRxDrops() != -1 ? net.getRxDrops() : 0).sum();
        txdrops = netList.stream().mapToLong(net -> net.getTxDrops() != -1 ? net.getTxDrops() : 0).sum();
        rxerrors = netList.stream().mapToLong(net -> net.getRxErrors() != -1 ? net.getRxErrors() : 0).sum();
        txerrors = netList.stream().mapToLong(net -> net.getTxErrors() != -1 ? net.getTxErrors() : 0).sum();
        Point point = Point.measurement(INFLUX_HOST_HIS)
                .time(timestamp, TimeUnit.MILLISECONDS)
                .tag(TAG_IP, ip)
                .addField(FIELD_RXBYT, rxbyt)
                .addField(FIELD_TXBYT, txbyt)
                .addField(FIELD_RXPCK, rxpck)
                .addField(FIELD_TXPCK, txpck)
                .addField(FIELD_RXDROPS, rxdrops)
                .addField(FIELD_TXDROPS, txdrops)
                .addField(FIELD_RXERRORS, rxerrors)
                .addField(FIELD_TXERRORS, txerrors)
                .build();
        influxdb.write(point);
    }

    private void insertDiskHis(List<DiskStat> diskList, long timestamp, String ip) {
        if (CollectionUtil.isEmpty(diskList)) {
            log.error("no system file sample information.");
            return;
        }
        diskList.forEach(diskStat -> {
            Point.Builder builder = Point.measurement(INFLUX_DISK_HIS)
                    .time(timestamp, TimeUnit.MILLISECONDS)
                    .tag(TAG_IP, ip)
                    .tag(TAG_DISK_NAME, convertTag(diskStat.getDirName()))
                    .addField(FIELD_SIZE, diskStat.getSize())
                    .addField(FIELD_USED, diskStat.getUsed())
                    .addField(FIELD_AVAIL, diskStat.getAvail())
                    .addField(FIELD_USE_PER, diskStat.getUsePer())
                    .addField(FIELD_READ_COUNT, diskStat.getReadCount())
                    .addField(FIELD_WRITE_COUNT, diskStat.getWriteCount())
                    .addField(FIELD_READ_BYTES, diskStat.getReadBytes())
                    .addField(FIELD_WRITE_BYTES, diskStat.getWriteCount())
                    .addField(FIELD_INODES_PER, diskStat.getInodesUsePer())
                    .addField(FIELD_READ_RATE, diskStat.getReadRate())
                    .addField(FIELD_WRITE_RATE, diskStat.getWriteRate());
            Point point = builder.build();
            influxdb.write(point);
        });
        influxdb.flush();
    }
}
