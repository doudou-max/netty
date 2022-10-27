/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端数据
     * @param msg 客户传的数据，转成 ByteBuf 读取数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 将客户端的数据转成 string
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String text = new String(bytes, 0, bytes.length, Charset.forName("utf8"));
        System.out.println("接收客户端消息：" + text);

        // 将消息写回客户端
        String response = "hello " + text;
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        //ctx.write(Unpooled.copiedBuffer(response.getBytes()));
    }

    /**
     * 读取完成
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    /**
     * 异常情况
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        System.out.println(cause.getMessage());
        ctx.close();
    }

}
