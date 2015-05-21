/*
 * The MIT License
 *
 * Copyright 2015 toyblocks.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.llv.locapi;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;

/**
 * ProtocolListener. note: this is not implementation of
 * {@link net.md_5.bungee.api.plugin.Listener}
 *
 * @author toyblocks
 */
public class PacketHandler extends ChannelDuplexHandler {

    private static final String PACKET_LISTENER = "packet_listener_player";
    private static final String WORLD_MESSAGE_TAG = "LAPIW";

    protected static void handle(ProxiedPlayer player) {
        ChannelPipeline chp = getPipeline(player);
        chp.addBefore(PipelineUtils.BOSS_HANDLER, PACKET_LISTENER, new PacketHandler(player));
    }

    protected static void unhandle(ProxiedPlayer player) {
        ChannelPipeline chp = getPipeline(player);
        try {
            chp.remove(PACKET_LISTENER);
        } catch (NoSuchElementException ex) {
            //do nothing
        }
    }

    private static ChannelPipeline getPipeline(ProxiedPlayer player) {
        try {
            Field fCh = player.getClass().getDeclaredField("ch");
            fCh.setAccessible(true);
            return ((ChannelWrapper) fCh.get(player)).getHandle().pipeline();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException ex) {
            throw new IllegalArgumentException("Not supported proxied player type");
        }
    }

    private final ProxiedPlayer player;

    protected PacketHandler(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        DefinedPacket p = ((PacketWrapper) msg).packet;
        if (p instanceof PlayerPositionAndLook) {
            PlayerPositionAndLook pos = (PlayerPositionAndLook) p;
            Location.of(player).update(pos.getX(), pos.getY(), pos.getZ(), false).update(pos.getYaw(), pos.getPitch());
        } else if (p instanceof Login) {
            Login pos = (Login) p;
            Location.of(player).update(Location.Dimension.ofId(pos.getDimension()));
        } else if (p instanceof Respawn) {
            Respawn pos = (Respawn) p;
            Location.of(player).update(Location.Dimension.ofId(pos.getDimension()));
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DefinedPacket p = ((PacketWrapper) msg).packet;
        if (p instanceof PlayerPosition) {
            PlayerPosition pos = (PlayerPosition) p;
            Location.getOrCreateOf(player).update(pos.getX(), pos.getY(), pos.getZ(), pos.isOnGround());
        } else if (p instanceof PlayerLook) {
            PlayerLook pos = (PlayerLook) p;
            Location.getOrCreateOf(player).update(pos.getYaw(), pos.getPitch());
        } else if (p instanceof PluginMessage) {
            PluginMessage pos = (PluginMessage) p;
            if (pos.getTag().equals(WORLD_MESSAGE_TAG)) {
                ByteBuffer buf = ByteBuffer.wrap(pos.getData());
                UUID uuid = new UUID(buf.getLong(), buf.getLong());
                byte[] ary = new byte[buf.remaining()];
                buf.get(ary);
                String name = new String(ary, Charset.forName("UTF-8"));
                Location.getOrCreateOf(player).update(uuid, name);
            }
        }
        super.channelRead(ctx, msg);
    }

}
