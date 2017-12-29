package com.talend.jreactive8583;

import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by jcl on 17.11.17.
 */
public class MyISO8583Client {

    final static String SERVER_HOST = "localhost";
    final static int SERVER_PORT = 5150;

    public static void main(String args[]) {

        try {
            //Create a Iso8583Server providing MessageFactory and SocketAddress
            MessageFactory mf = MyISO8583Factory.create();
            final Iso8583Client<IsoMessage> client = new Iso8583Client(
                    new InetSocketAddress(SERVER_HOST, SERVER_PORT), mf);
            //Initialize client and connect to server
            client.init();
            ChannelFuture channelFuture = client.connect();
            channelFuture.channel().pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    System.out.println("Got response: " + ((IsoMessage)msg).debugString());
                }
            });

            //Send IsoMessage if connection is established
            if (client.isConnected()) {
                IsoMessage message = mf.newMessage(0x0200);
                System.out.println("Send message: " + message.debugString());
                //message.setField(39, IsoType.ALPHA.value("00", 2));
                //message.setField(60, IsoType.LLLVAR.value("XXX", 3));

                //sends asynchronously
                client.send(message);
            }
            // Shutdown client
            if (client != null) client.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
