package main.com.handu.scada.db.bean;

import java.io.Serializable;
import java.util.Date;

public class DeviceHistoryRemotesignalling implements Serializable {
    private String remotesignallingid;

    private Date recordtime;

    private String deviceid;

    private String dataitem;

    private Integer value;

    private String unit;

    private String description;

    private static final long serialVersionUID = 1L;

    public String getRemotesignallingid() {
        return remotesignallingid;
    }

    public void setRemotesignallingid(String remotesignallingid) {
        this.remotesignallingid = remotesignallingid == null ? null : remotesignallingid.trim();
    }

    public Date getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }

    public String getDataitem() {
        return dataitem;
    }

    public void setDataitem(String dataitem) {
        this.dataitem = dataitem == null ? null : dataitem.trim();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        DeviceHistoryRemotesignalling other = (DeviceHistoryRemotesignalling) that;
        return (this.getRemotesignallingid() == null ? other.getRemotesignallingid() == null : this.getRemotesignallingid().equals(other.getRemotesignallingid()))
            && (this.getRecordtime() == null ? other.getRecordtime() == null : this.getRecordtime().equals(other.getRecordtime()))
            && (this.getDeviceid() == null ? other.getDeviceid() == null : this.getDeviceid().equals(other.getDeviceid()))
            && (this.getDataitem() == null ? other.getDataitem() == null : this.getDataitem().equals(other.getDataitem()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
            && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRemotesignallingid() == null) ? 0 : getRemotesignallingid().hashCode());
        result = prime * result + ((getRecordtime() == null) ? 0 : getRecordtime().hashCode());
        result = prime * result + ((getDeviceid() == null) ? 0 : getDeviceid().hashCode());
        result = prime * result + ((getDataitem() == null) ? 0 : getDataitem().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
}