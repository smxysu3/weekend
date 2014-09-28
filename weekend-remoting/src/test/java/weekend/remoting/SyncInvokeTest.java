/**
 * $Id: SyncInvokeTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package weekend.remoting;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import weekend.remoting.protocol.RemotingCommand;



/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 */
public class SyncInvokeTest {
    @Test
    public void test_RPC_Sync() throws Exception {
        RemotingServer server = NettyRPCTest.createRemotingServer();
        RemotingClient client = NettyRPCTest.createRemotingClient();

        for (int i = 0; i < 1000000; i++) {
            try {
                RemotingCommand request = RemotingCommand.createRequestCommand(0, null);
                RemotingCommand response = client.invokeSync("127.0.0.1:10911", request, 1000 * 3);
                System.out.println(i + "\t" + "invoke result = " + response);
                assertTrue(response != null);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        client.shutdown();
        server.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }
}
