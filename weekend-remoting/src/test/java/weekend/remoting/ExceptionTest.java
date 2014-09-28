/**
 * $Id: ExceptionTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package weekend.remoting;

import static org.junit.Assert.assertTrue;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

import org.junit.Test;

import weekend.remoting.exception.RemotingConnectException;
import weekend.remoting.exception.RemotingSendRequestException;
import weekend.remoting.exception.RemotingTimeoutException;
import weekend.remoting.netty.NettyClientConfig;
import weekend.remoting.netty.NettyRemotingClient;
import weekend.remoting.netty.NettyRemotingServer;
import weekend.remoting.netty.NettyRequestProcessor;
import weekend.remoting.netty.NettyServerConfig;
import weekend.remoting.protocol.RemotingCommand;



/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 */
public class ExceptionTest {
    private static RemotingClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        RemotingClient client = new NettyRemotingClient(config);
        client.start();
        return client;
    }


    private static RemotingServer createRemotingServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        RemotingServer client = new NettyRemotingServer(config);
        client.registerProcessor(0, new NettyRequestProcessor() {
            private int i = 0;


            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
                System.out.println("processRequest=" + request + " " + (i++));
                request.setRemark("hello, I am respponse " + ctx.channel().remoteAddress());
                return request;
            }
        }, Executors.newCachedThreadPool());
        client.start();
        return client;
    }


    @Test
    public void test_CONNECT_EXCEPTION() {
        RemotingClient client = createRemotingClient();

        RemotingCommand request = RemotingCommand.createRequestCommand(0, null);
        RemotingCommand response = null;
        try {
            response = client.invokeSync("127.0.0.1:10911", request, 1000 * 3);
        }
        catch (RemotingConnectException e) {
            e.printStackTrace();
        }
        catch (RemotingSendRequestException e) {
            e.printStackTrace();
        }
        catch (RemotingTimeoutException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("invoke result = " + response);
        assertTrue(response != null);

        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }

}
