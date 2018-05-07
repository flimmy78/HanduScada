package main.com.handu.scada.netty.client.dtu;

import main.com.handu.scada.event.Subscriber;
import main.com.handu.scada.event.events.BaseEvent;
import main.com.handu.scada.event.events.DownProtocolEvent;
import main.com.handu.scada.event.subscribe.ISubscriber;
import main.com.handu.scada.event.subscribe.SubscribePublish;
import main.com.handu.scada.netty.server.dtu.DtuChannelManager;
import main.com.handu.scada.netty.server.dtu.DtuNetworkConnection;
import main.com.handu.scada.protocol.base.ProtocolLayerData;
import main.com.handu.scada.thread.MyThreadPoolExecutor;
import main.com.handu.scada.utils.LogUtils;
import main.com.handu.scada.utils.StringsUtils;

import java.util.concurrent.ExecutorService;

/**
 * Created by 柳梦 on 2017/12/26.
 * Dtu发送命令入口
 */

@Subscriber
public class DtuTcpClient implements ISubscriber, IClient {

    //线程池
    private ExecutorService service = MyThreadPoolExecutor.getInstance();

    private DtuTcpClient() {
    }

    @Override
    public void subscribe(SubscribePublish subscribePublish) {
        subscribePublish.subscribe(this);
    }

    @Override
    public void unSubscribe(SubscribePublish subscribePublish) {
        subscribePublish.unSubscribe(this);
    }

    @Override
    public void onEvent(String publisher, BaseEvent event) {
        if (event instanceof DownProtocolEvent) {
            ProtocolLayerData protocolLayerData = (ProtocolLayerData) event.data;
            if (protocolLayerData != null) {
                service.execute(() -> send(event.priority, protocolLayerData));
            }
        }
    }

    @Override
    public void send(ProtocolLayerData protocolLayerData) {
        send(MsgPriority.LOW, protocolLayerData);
    }

    @Override
    public void send(MsgPriority priority, ProtocolLayerData protocolLayerData) {
        if (protocolLayerData != null) {
            if (StringsUtils.isNotEmptyAndValidLength(protocolLayerData.DTUString, 8)) {
                String clientId = DtuChannelManager.getClientId(protocolLayerData.DTUString);
                if (clientId != null) {
                    DtuNetworkConnection state = DtuChannelManager.getNetworkState(clientId);
                    if (state != null) {
                        if (state.getChannel().isActive()) {
                            state.notifyDownParse(protocolLayerData, priority);
                        }
                    }
                } else {
                    LogUtils.error("dtuAddress " + protocolLayerData.DTUString + " is not connection!");
                }
            } else {
                LogUtils.error("dtuAddress " + protocolLayerData.DTUString + " is not correct!");
            }
        }
    }
}
