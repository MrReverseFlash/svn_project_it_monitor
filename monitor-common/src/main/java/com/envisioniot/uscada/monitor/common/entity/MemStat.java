package com.envisioniot.uscada.monitor.common.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 */
@Data
@ToString
public class MemStat implements Serializable {

	private static final long serialVersionUID = 1761149476868648551L;

	/**
	 * 总计内存，M
	 */
    private Double total;

    /**
	 *已使用多少，M
	 */
    private Double used;
    
    /**
	 * 未使用，M
	 */
    private Double free;
    
    /**
     * 已使用百分比%
     */
    private Double usePer;

}