package com.envisioniot.uscada.monitor.common.entity;

/**
 * UscadaAlarm
 *
 * @author yangkang
 * @date 2021/9/30
 */
public class UscadaAlarm {
    private String warn_type;
    private String warn_obj;
    private String warn_text;

    public UscadaAlarm(String warn_type, String warn_obj, String warn_text) {
        this.warn_type = warn_type;
        this.warn_obj = warn_obj;
        this.warn_text = warn_text;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }

    public String getWarn_obj() {
        return warn_obj;
    }

    public void setWarn_obj(String warn_obj) {
        this.warn_obj = warn_obj;
    }

    public String getWarn_text() {
        return warn_text;
    }

    public void setWarn_text(String warn_text) {
        this.warn_text = warn_text;
    }
}
