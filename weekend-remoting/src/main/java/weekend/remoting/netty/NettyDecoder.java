/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package weekend.remoting.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weekend.remoting.common.RemotingHelper;
import weekend.remoting.common.RemotingUtil;
import weekend.remoting.protocol.RemotingCommand;



/**
 * 协议解码器
 * 
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-7-13
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {
    private static final Logger log = LoggerFactory.getLogger(RemotingHelper.RemotingLogName);
    private static final int FRAME_MAX_LENGTH = //
            Integer.parseInt(System.getProperty("com.rocketmq.remoting.frameMaxLength", "8388608"));


    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }


    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        try {
            ByteBuf frame = (ByteBuf) super.decode(ctx, in);
            if (frame == null) {
                return null;
            }

            byte[] tmpBuf = new byte[frame.capacity()];
            frame.getBytes(0, tmpBuf);
            frame.release();

            return RemotingCommand.decode(tmpBuf);
        }
        catch (Exception e) {
            log.error("decode exception, " + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);
            // 这里关闭后， 会在pipeline中产生事件，通过具体的close事件来清理数据结构
            RemotingUtil.closeChannel(ctx.channel());
        }

        return null;
    }
}
