package com.talend.jreactive8583;


import com.github.kpavlov.jreactive8583.server.Iso8583Server;
import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * Created by jcl on 17.11.17.
 */
public class MyISO8583Server {

    final static int SERVER_PORT = 7777;

    public static void main(String args[]) {

        try {
            //Create a Iso8583Server providing MessageFactory and port to bind to
            final Iso8583Server<IsoMessage> server = new Iso8583Server(
                    SERVER_PORT,
                    MyISO8583Factory.create());
            //Add one or more custom IsoMessageListeners to handle IsoMessages
            server.addMessageListener(new IsoMessageListener<IsoMessage>() {
                public boolean applies(IsoMessage msg) {
                    System.out.println("Received message");
                    return msg.getType() ==  0x0200;
                }
                public boolean onMessage(ChannelHandlerContext ctx, IsoMessage msg) {
                    System.out.println(msg.debugString());
                    System.out.println("Size: " + msg.debugString().length());
                    final IsoMessage response =
                            server.getIsoMessageFactory().createResponse(msg);
                    response.setField(39, IsoType.ALPHA.value("00", 2));
                    response.setField(60, IsoType.LLLVAR.value("XXX", 3));
                    ctx.writeAndFlush(response);
                    return true;
                }
            });
            //Initialize and start server, then ready to accept client connections
            server.getConfiguration().replyOnError();
            server.init();
            server.start();
            // Shutdown server when you're done
            //if (server != null && server.isStarted()) server.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
