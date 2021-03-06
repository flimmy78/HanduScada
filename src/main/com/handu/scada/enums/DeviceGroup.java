package main.com.handu.scada.enums;

import java.util.Objects;

/**
 * Created by 柳梦 on 2018/04/23.
 * 设备分组
 */
public enum DeviceGroup {
    NONE,
    //DTU采集器
    DTU,
    //DTU相关联的设备
    DTU_DEVICE,
    //101协议设备
    PROTOCOL101_DEVICE,
    //台区总表
    HM,
    //国标
    LP2007,
    //乾隆
    LP1997,
    //有线测温
    WIRED_TEMPERATURE,
    //无线测温
    WIRELESS_TEMPERATURE,
    //跌落装置
    FALL_TYPE_SWITCH,
    //4G营配模块
    DTU4G,
    //无功补偿
    REACTIVE_POWER,
    //开关
    SWITCH,
    //故障指示器
    FAULT_INDICATOR;

    public static DeviceGroup getDeviceGroup(String name) {
        name = name.toLowerCase();
        for (DeviceGroup deviceGroup : DeviceGroup.values()) {
            if (Objects.equals(deviceGroup.name().toLowerCase().replace("_", ""), name)) {
                return deviceGroup;
            }
        }
        return null;
    }
}
