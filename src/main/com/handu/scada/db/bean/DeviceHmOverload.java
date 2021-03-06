package main.com.handu.scada.db.bean;

import java.io.Serializable;
import java.util.Date;

public class DeviceHmOverload implements Serializable {
    private Long id;

    private String dtuaddress;

    private Double overload;

    private Date begintime;

    private Date endtime;

    private Integer duration;

    private String workorderid;

    private Integer issendworkorder;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDtuaddress() {
        return dtuaddress;
    }

    public void setDtuaddress(String dtuaddress) {
        this.dtuaddress = dtuaddress == null ? null : dtuaddress.trim();
    }

    public Double getOverload() {
        return overload;
    }

    public void setOverload(Double overload) {
        this.overload = overload;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getWorkorderid() {
        return workorderid;
    }

    public void setWorkorderid(String workorderid) {
        this.workorderid = workorderid == null ? null : workorderid.trim();
    }

    public Integer getIssendworkorder() {
        return issendworkorder;
    }

    public void setIssendworkorder(Integer issendworkorder) {
        this.issendworkorder = issendworkorder;
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
        DeviceHmOverload other = (DeviceHmOverload) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDtuaddress() == null ? other.getDtuaddress() == null : this.getDtuaddress().equals(other.getDtuaddress()))
            && (this.getOverload() == null ? other.getOverload() == null : this.getOverload().equals(other.getOverload()))
            && (this.getBegintime() == null ? other.getBegintime() == null : this.getBegintime().equals(other.getBegintime()))
            && (this.getEndtime() == null ? other.getEndtime() == null : this.getEndtime().equals(other.getEndtime()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getWorkorderid() == null ? other.getWorkorderid() == null : this.getWorkorderid().equals(other.getWorkorderid()))
            && (this.getIssendworkorder() == null ? other.getIssendworkorder() == null : this.getIssendworkorder().equals(other.getIssendworkorder()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDtuaddress() == null) ? 0 : getDtuaddress().hashCode());
        result = prime * result + ((getOverload() == null) ? 0 : getOverload().hashCode());
        result = prime * result + ((getBegintime() == null) ? 0 : getBegintime().hashCode());
        result = prime * result + ((getEndtime() == null) ? 0 : getEndtime().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getWorkorderid() == null) ? 0 : getWorkorderid().hashCode());
        result = prime * result + ((getIssendworkorder() == null) ? 0 : getIssendworkorder().hashCode());
        return result;
    }
}