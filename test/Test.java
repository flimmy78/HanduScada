import main.com.handu.scada.netty.server.switch101.SwitchNetworkConnection;
import main.com.handu.scada.utils.AesUtils;

/**
 * Created by 柳梦 on 2017/12/15.
 */
public class Test {

    @org.junit.Test
    public void test() throws InterruptedException {

    }

    private void switch101() {

        byte[] b1 = new byte[]{
                0x68, 0x12,
                0x12, 0x68,
                0x73, (byte) 0xae,
                0x02, 0x01,
                (byte) 0x87, 0x14,
                0x00, (byte) 0xae,
                0x02, 0x01,
                0x00, 0x01,
                0x00, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x72, 0x16
        };


        byte[] b2 = new byte[]{
                0x68, 0x0f,
                0x0f, 0x68,
                0x73, (byte) 0xae,
                0x02, 0x01,
                (byte) 0x02, 0x14,
                0x00, (byte) 0xae,
                0x02, 0x01,
                0x00, 0x01, 0x00, 0x00, 0x00,
                (byte) 0xec, 0x16
        };

        byte[] b3 = new byte[]{
                0x68, 0x1d,
                0x1d, 0x68,
                0x53, (byte) 0xae,
                0x02, 0x09,
                (byte) 0x86, 0x14,
                0x00, (byte) 0xae,
                0x02, 0x01,
                0x40,
                0x0A, 0x00,
                0x00,
                0x00, 0x00,
                0x00,
                0x00, 0x00,
                0x00,
                0x00, 0x00,
                0x00,
                0x00, 0x00,
                0x00,
                0x00, 0x00,
                0x00,
                (byte) 0xA1, 0x16
        };

        byte[] b4 = new byte[]{
                0x68, 0x13, 0x13, 0x68, 0x53, (byte) 0xAE, 0x02, 0x1F, 0x01, 0x03, 0x00, (byte) 0xAE, 0x02,
                0X08, 0x00, 0x01,
                (byte) 0XB1, 0x75, 0X32, 0x0D, (byte) 0X0E, 0x07, 0x11,
                (byte) 0X6A, 0x16};

        SwitchNetworkConnection connection = new SwitchNetworkConnection(null);
        connection.parseUp(b3);
    }

    private void dec_enc() {
        System.err.println(AesUtils.decrypt("03A2005EF63FD6B3163B8899CD754EEA33BC22D298AEDFBC4085C8905F71585CE0ABF456F67F9758A891F845AD768EC1F7EA374067A2E9BAAA04501E303C0FA72B9CF176F076A569A09883557D604437"));
        System.err.println(AesUtils.encrypt("jdbc:mysql://192.168.2.113:3306/hdkj2018322?useUnicode=true&characterEncoding=UTF-8"));
    }
}