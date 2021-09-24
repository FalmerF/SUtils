package net.smb.moderutils;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.eq2online.console.Log;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketChatMessage;

public class PacketHandler {
    
    public PacketHandler(NetHandlerPlayClient clientHandler) {
        if (clientHandler != null) {
            clientHandler.getNetworkManager().channel().pipeline().addBefore("packet_handler", str(), new ChannelDuplexHandler() {
                @Override 
                public void channelRead(ChannelHandlerContext ctx, Object in) throws Exception {
                	super.channelRead(ctx, in);
                }
                @Override 
                public void write(ChannelHandlerContext ctx, Object out, ChannelPromise pr) throws Exception {
                	if(out instanceof CPacketChatMessage) {
                		if(!VariableProviderModer.instance.onSendChatMessage(((CPacketChatMessage)out).getMessage())) return;
                	}
                	super.write(ctx, out, pr);
                }
                @Override 
                public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                }
            });
        }
    }
    
    public static String str() {
        return str(UUID.randomUUID().toString());
    }
    
    public static String str(String in) {
        return DigestUtils.md5Hex(in);
    }
}
