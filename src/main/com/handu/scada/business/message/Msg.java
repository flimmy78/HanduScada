package main.com.handu.scada.business.message;

/**
 * Created by 柳梦 on 2017/12/29.
 */
public class Msg {

    private String DeviceId;
    private int DeviceAlarms;
    private String msgContent;
    private String deviceTableName;
    private int priority;

    public Msg(String deviceId, String deviceTableName, int deviceAlarms, String msgContent) {
        DeviceId = deviceId;
        this.deviceTableName = deviceTableName;
        DeviceAlarms = deviceAlarms;
        this.msgContent = msgContent;
        this.priority = 0;
    }

    public String getDeviceTableName() {
        return deviceTableName;
    }

    public void setDeviceTableName(String deviceTableName) {
        this.deviceTableName = deviceTableName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getDeviceAlarms() {
        return DeviceAlarms;
    }

    public void setDeviceAlarms(int deviceAlarms) {
        DeviceAlarms = deviceAlarms;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "DeviceId='" + DeviceId + '\'' +
                ", DeviceAlarms=" + DeviceAlarms +
                ", msgContent='" + msgContent + '\'' +
                ", deviceTableName='" + deviceTableName + '\'' +
                ", priority=" + priority +
                '}';
    }
}
