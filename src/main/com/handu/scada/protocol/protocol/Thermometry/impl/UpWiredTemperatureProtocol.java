package main.com.handu.scada.protocol.protocol.Thermometry.impl;

import main.com.handu.scada.enums.DeviceTableEnum;
import main.com.handu.scada.enums.DeviceTypeEnum;
import main.com.handu.scada.exception.ExceptionHandler;
import main.com.handu.scada.protocol.DtuUpParse;
import main.com.handu.scada.protocol.base.MediaData;
import main.com.handu.scada.protocol.base.ProtocolLayerData;
import main.com.handu.scada.protocol.enums.RemoteType;
import main.com.handu.scada.protocol.protocol.Data.DataAttr;
import main.com.handu.scada.protocol.protocol.Thermometry.BaseUpTemperatureProtocol;
import main.com.handu.scada.utils.Crc16Utils;
import main.com.handu.scada.utils.DateUtils;
import main.com.handu.scada.utils.HexUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 柳梦 on 2018/01/25.
 */

@DtuUpParse
public class UpWiredTemperatureProtocol extends BaseUpTemperatureProtocol {

    @Override
    public ProtocolLayerData parse(MediaData mediaData) {
        try {
            if (mediaData == null) return null;
            byte upstream[] = mediaData.CommandData;
            if (!valid(upstream)) return null;
            dtuAddress = mediaData.DTUString;
            probedic = new HashMap<>();
            int temp = Integer.parseInt(HexUtils.byteArrayToHexStr(upstream, 5, 2));
            if (temp > 0x05 && temp < 0xF5) probedic.put((byte) 1, (double) (temp - 0x23));
            temp = Integer.parseInt(HexUtils.byteArrayToHexStr(upstream, 7, 2));
            if (temp > 0x05 && temp < 0xF5) probedic.put((byte) 2, (double) (temp - 0x23));
            temp = Integer.parseInt(HexUtils.byteArrayToHexStr(upstream, 9, 2));
            if (temp > 0x05 && temp < 0xF5) probedic.put((byte) 3, (double) (temp - 0x23));
            IsNormal = true;
            address = upstream[0];
            values = new ArrayList<>();
            String addressStr = Integer.toHexString(address);
            int index = 0;
            for (Map.Entry<Byte, Double> entry : probedic.entrySet()) {
                index++;
                DataAttr dataAttr = new DataAttr();
                dataAttr.setDateType(RemoteType.YC);
                dataAttr.setValue(entry.getValue());
                dataAttr.setName(index + "");
                dataAttr.setDtime(DateUtils.getNowSqlDateTime());
                dataAttr.setCnname(addressStr + index);
                dataAttr.setGroup("areaTemperature");
                dataAttr.setUnit("°C");
                values.add(dataAttr);
            }
            IsSuccess = true;
            return new ProtocolLayerData() {{
                CommandData = mediaData.CommandData;
                CommandName = "优科测温遥测";
                PostalAddress = addressStr;
                DTUString = mediaData.DTUString;
                attrList = values;
                deviceTypeEnum = DeviceTypeEnum.YK_WIRED_TEMPERATURE;
                TabName = DeviceTableEnum.Device_Temperature.getTableName();
            }};
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
        return null;
    }

    @Override
    public boolean valid(byte[] bytes) {
        try {
            if (bytes == null) return false;
            if (bytes.length < 14) return false;
            if (bytes[0] != 0x01) return false;
            //测温功能码是0x03
            if (bytes[1] != 0x03) return false;
            int Crc = Crc16Utils.calcCrc16(bytes, 0, bytes.length - 2);
            return (byte) (Crc & 0xFF) == bytes[13] && (byte) (Crc >> 8) == bytes[14];
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
        return false;
    }
}
