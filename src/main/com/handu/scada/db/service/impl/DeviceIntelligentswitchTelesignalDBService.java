package main.com.handu.scada.db.service.impl;

import main.com.handu.scada.business.device.DeviceData;
import main.com.handu.scada.db.bean.DeviceIntelligentswitchTelesignal;
import main.com.handu.scada.db.mapper.common.CommonMapper;
import main.com.handu.scada.db.service.BaseDBService;
import main.com.handu.scada.db.service.IDBService;

import java.util.List;

/**
 * Created by 柳梦 on 2018/05/25.
 */
public class DeviceIntelligentswitchTelesignalDBService extends BaseDBService implements IDBService {

    @Override
    protected String startSql() {
        return " insert into device_intelligentswitch_telesignal (DeviceId, DataItem, Value, DataItemName, Unit, Description, RecordTime) values ";
    }

    @Override
    protected String endSql() {
        return " on duplicate key update DeviceId = values(DeviceId),DataItem=values(DataItem),Value = values(Value),DataItemName = values(DataItemName)," +
                "Unit =values(Unit),Description=values(Description),RecordTime=values(RecordTime)";
    }

    @Override
    protected void jointSql() {
        if (dataList != null) {
            for (DeviceData data : dataList) {
                DeviceIntelligentswitchTelesignal telesignal = (DeviceIntelligentswitchTelesignal) data.getData();
                if (telesignal != null) {
                    sb.append(getStartColumn(telesignal.getDeviceid()))
                            .append(getColumn(telesignal.getDataitem()))
                            .append(getColumn(telesignal.getValue()))
                            .append(getColumn(telesignal.getDataitemname()))
                            .append(getColumn(telesignal.getUnit()))
                            .append(getColumn(telesignal.getDescription()))
                            .append(getEndColumn(telesignal.getRecordtime()))
                            .append(",");
                    num++;
                }
            }
        }
    }

    @Override
    public int submit(CommonMapper commonMapper, List<DeviceData> dataList) {
        return super.submit(commonMapper, dataList);
    }
}