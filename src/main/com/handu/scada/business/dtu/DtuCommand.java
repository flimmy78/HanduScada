package main.com.handu.scada.business.dtu;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.com.handu.scada.cache.MyCacheManager;
import main.com.handu.scada.config.Config;
import main.com.handu.scada.db.bean.common.DtuCacheResult;
import main.com.handu.scada.db.mapper.common.CommonMapper;
import main.com.handu.scada.db.utils.MyBatisUtil;
import main.com.handu.scada.enums.DeviceTypeEnum;
import main.com.handu.scada.event.EventManager;
import main.com.handu.scada.event.events.DownProtocolEvent;
import main.com.handu.scada.exception.ExceptionHandler;
import main.com.handu.scada.netty.client.dtu.MsgPriority;
import main.com.handu.scada.netty.server.dtu.DtuChannelManager;
import main.com.handu.scada.netty.server.dtu.DtuNetworkConnection;
import main.com.handu.scada.protocol.base.ProtocolLayerData;
import main.com.handu.scada.protocol.enums.DeviceCmdTypeEnum;
import main.com.handu.scada.quartz.job.BaseDtuCommand;
import main.com.handu.scada.utils.LogUtils;
import main.com.handu.scada.utils.StringsUtils;
import main.com.handu.scada.utils.TxtUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by 柳梦 on 2018/01/17.
 */
public class DtuCommand extends BaseDtuCommand {

    private static DtuCommand singleton;

    public static DtuCommand getInstance() {
        if (singleton == null) {
            synchronized (DtuCommand.class) {
                if (singleton == null) {
                    singleton = new DtuCommand();
                }
            }
        }
        return singleton;
    }

    private DeviceCmdTypeEnum[] baseData = new DeviceCmdTypeEnum[]{
            DeviceCmdTypeEnum.Voltage,
            DeviceCmdTypeEnum.Current,
            DeviceCmdTypeEnum.ResidualAndMaxPhase
    };

    private DeviceCmdTypeEnum[] dateTime = new DeviceCmdTypeEnum[]{
            DeviceCmdTypeEnum.RunDate,
            DeviceCmdTypeEnum.RunTime,
    };

    /**
     * 下发基础数据采集
     */
    private void baseData() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, baseData);
        send(MsgPriority.HIGH, DeviceTypeEnum.LP1997, DeviceCmdTypeEnum.VoltageCurrentAndResidualCurrent);
    }

    /**
     * 跌落装置总召
     */
    private void fallSwitch() {
        send(MsgPriority.LOW, DeviceTypeEnum.CY_FALL_TYPE_SWITCH, DeviceCmdTypeEnum.ALL_CALL);
    }

    /**
     * 测温总召
     */
    private void temperature() {
        send(MsgPriority.HIGH, DeviceTypeEnum.YK_WIRED_TEMPERATURE, DeviceCmdTypeEnum.ALL_CALL);
    }

    /**
     * 日期时间
     */
    private void dateTime() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, dateTime);
        send(MsgPriority.HIGH, DeviceTypeEnum.LP1997, DeviceCmdTypeEnum.ReadClock);
    }

    private void readControlWordParameterModule() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, DeviceCmdTypeEnum.ReadControlWordParameterModule);
        send(MsgPriority.HIGH, DeviceTypeEnum.LP1997, DeviceCmdTypeEnum.ReadControlWordParameterModule);
    }

    /**
     * 下发运行状态
     */
    private void runState() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, DeviceCmdTypeEnum.RunState);
    }

    /**
     * 读通讯地址
     */
    public void readPostalAddress() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, DeviceCmdTypeEnum.ReadPostalAddress);
    }

    /**
     * 下发告警事件
     */
    private void protectorTripEventRecord() {
        send(MsgPriority.HIGH, DeviceTypeEnum.LP2007, DeviceCmdTypeEnum.ProtectorTripEventRecord);
        send(MsgPriority.HIGH, DeviceTypeEnum.LP1997, DeviceCmdTypeEnum.ProtectorTripEventRecord);
    }

    /**
     * 根据输入值发送命令
     *
     * @param value
     */
    public boolean sendByValue(int value) {
        switch (value) {
            case 1000:
                baseData();
                return true;
            case 1001:
                dateTime();
                return true;
            case 1002:
                runState();
                return true;
            case 1003:
                protectorTripEventRecord();
                return true;
            case 1004:
                readControlWordParameterModule();
                return true;
            case 1005:
                temperature();
                return true;
            case 1006:
                fallSwitch();
                return true;
            case 1007:
                readPostalAddress();
                return true;
            case 1008:
                sendTo4g(DeviceCmdTypeEnum.HM_AFN0C25);
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取dtu信息
     *
     * @param dtuAddresses
     */
    public void readDtuInfo(String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.CmdType = DeviceCmdTypeEnum.DTU_INFO;
            data.deviceTypeEnum = DeviceTypeEnum.DTU;
            data.DTUString = dtuAddress;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.HIGH, data), MsgPriority.HIGH);
        }
    }

    /**
     * 重启dtus
     *
     * @param dtuAddresses
     */
    public void restartDtu(String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.CmdType = DeviceCmdTypeEnum.DTU_RESTART;
            data.deviceTypeEnum = DeviceTypeEnum.DTU;
            data.DTUString = dtuAddress;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.LOW, data), MsgPriority.HIGH);
        }
    }

    /**
     * 读取模块信号强度
     *
     * @param dtuAddresses
     */
    public void readSignalStrength(String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.deviceTypeEnum = DeviceTypeEnum.DTU;
            data.CmdType = DeviceCmdTypeEnum.READ_DTU_SIGNAL_STRENGTH;
            data.DTUString = dtuAddress;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.LOW, data), MsgPriority.HIGH);
        }
    }

    /**
     * 采集信号强度
     *
     * @param dtuAddresses
     */
    public void collectSignalStrength(String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.CmdType = DeviceCmdTypeEnum.COLLECT_DTU_SIGNAL_STRENGTH;
            data.DTUString = dtuAddress;
            data.deviceTypeEnum = DeviceTypeEnum.DTU;
            data.isWaitReceive = false;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.LOW, data), MsgPriority.HIGH);
        }
    }

    /**
     * 设置模块信号模式
     *
     * @param type
     * @param dtuAddresses
     */
    public void setCommunicationModel(int type, String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.CommandData = new byte[]{(byte) type};
            data.CmdType = DeviceCmdTypeEnum.COMMUNICATION_MODEL;
            data.DTUString = dtuAddress;
            data.deviceTypeEnum = DeviceTypeEnum.DTU4G;
            data.isWaitReceive = false;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.LOW, data), MsgPriority.HIGH);
        }
    }

    /**
     * 执行sql语句
     *
     * @param sql
     */
    public void executeSql(String sql) {
        SqlSession sqlSession = null;
        try {
            JsonArray array = new JsonArray();
            sqlSession = MyBatisUtil.getSqlSession();
            CommonMapper mapper = sqlSession.getMapper(CommonMapper.class);
            List<Map<String, Object>> list = mapper.selectListBySql(sql);
            for (Map<String, Object> map : list) {
                JsonObject object = new JsonObject();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    object.addProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
                array.add(object);
            }
            LogUtils.info("sql execute success---" + (array.size() > 0 ? array.toString() : ""), true);
        } catch (Exception e) {
            LogUtils.error(" sql execute error!");
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    /**
     * 获取客户端数量
     */
    public void getClientNumber() {
        try {
            LogUtils.info("client number:" + DtuChannelManager.getNetworkStateMapCount() + "--dtu number:" + DtuChannelManager.getDtuMapCount(), true);
            if (Config.isDebug) {
                ConcurrentHashMap<String, DtuNetworkConnection> map = DtuChannelManager.getNetworkStateMap();
                synchronized (map) {
                    for (Map.Entry<String, DtuNetworkConnection> entry : map.entrySet()) {
                        String dtuAddress = entry.getValue().getDtuAddress();
                        if (dtuAddress != null) {
                            LogUtils.error(entry.getKey() + "--" + entry.getValue().getDtuAddress(), true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }

    /**
     * 导出dtu上线情况到txt文件
     */
    public void exportDtuOnline2Txt() {
        try {
            ConcurrentHashMap<String, DtuNetworkConnection> onlineMap = DtuChannelManager.getNetworkStateMap();
            ConcurrentHashMap<String, DtuCacheResult> dtuCacheMap = MyCacheManager.getDtuCacheMap();
            List<String> onlineDtu = null;
            List<String> dtuRecords = null;
            if (onlineMap != null) {
                synchronized (onlineMap) {
                    onlineDtu = onlineMap
                            .entrySet()
                            .stream().map(e -> e.getValue().getDtuAddress())
                            .filter(StringsUtils::isNotEmpty)
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
                }
            }
            if (dtuCacheMap != null) {
                synchronized (dtuCacheMap) {
                    dtuRecords = dtuCacheMap
                            .entrySet()
                            .stream()
                            .map(e -> e.getValue().getDtuAddress())
                            .filter(StringsUtils::isNotEmpty)
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("records list--->\n");
            if (dtuRecords != null) {
                for (String record : dtuRecords) {
                    sb.append(record).append("\n");
                }
            }
            sb.append("online list--->\n");
            if (onlineDtu != null) {
                for (String dtuAddress : onlineDtu) {
                    sb.append(dtuAddress).append("\n");
                }
            }
            TxtUtils.getInstance().exportDtuOnline(sb.toString());
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }

    /**
     * 对时
     */

    public void broadcastTime(String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.DTUString = dtuAddress;
            data.deviceTypeEnum = DeviceTypeEnum.LP2007;
            data.HasDTUHead = true;
            data.CmdType = DeviceCmdTypeEnum.BroadcastTime;
            data.isWaitReceive = false;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.HIGH, data), MsgPriority.HIGH);

            data = new ProtocolLayerData();
            data.DTUString = dtuAddress;
            data.deviceTypeEnum = DeviceTypeEnum.LP1997;
            data.HasDTUHead = true;
            data.CmdType = DeviceCmdTypeEnum.BroadcastTime;
            data.isWaitReceive = false;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.HIGH, data), MsgPriority.HIGH);
        }
    }

    /**
     * 下发命令到4g模块
     *
     * @param deviceCmdTypeEnum
     * @param dtuAddresses
     */
    public void sendTo4g(DeviceCmdTypeEnum deviceCmdTypeEnum, String... dtuAddresses) {
        for (String dtuAddress : dtuAddresses) {
            ProtocolLayerData data = new ProtocolLayerData();
            data.CmdType = deviceCmdTypeEnum;
            data.deviceTypeEnum = DeviceTypeEnum.DTU4G;
            data.DTUString = dtuAddress;
            data.isWaitReceive = false;
            EventManager.getInstance().publish(new DownProtocolEvent(MsgPriority.LOW, data), MsgPriority.HIGH);
        }
    }
}
