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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Protocol;

/**
 * LocationAPI
 * @author toyblocks
 */
public class LocationAPI implements Listener {
    
    private static Plugin owner;
    
    private LocationAPI(){}
    
    /**
     * パケットのハンドルを開始します.
     * プラグインを通してリスナを登録します
     * @param p プラグイン
     * @return trueならば開始ができた、falseなら既に開始されていた
     */
    public static boolean init(Plugin p) {
        if (owner != null) {
            return false;
        }
        owner = p;
        owner.getProxy().getPluginManager().registerListener(owner, new LocationAPI());
        
        try {//Bungeecordに0x04 PlayerPositionのPacketがないせいで勝手に追加することに
            Method mRegisterPacket = Protocol.GAME.TO_SERVER.getClass().getDeclaredMethod("registerPacket", int.class, Class.class);
            mRegisterPacket.setAccessible(true);
            mRegisterPacket.invoke(Protocol.GAME.TO_SERVER, 0x04, PlayerPosition.class);
            mRegisterPacket.invoke(Protocol.GAME.TO_SERVER, 0x05, PlayerLook.class);
            mRegisterPacket.invoke(Protocol.GAME.TO_CLIENT, 0x08, PlayerPositionAndLook.class);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            p.getLogger().log(Level.WARNING, "Failed to register custom packet", ex);
        }
        return true;
    }
    
    @EventHandler
    public void onJoin(PostLoginEvent eve) {
        PacketHandler.handle(eve.getPlayer());
    }
    
    @EventHandler
    public void onQuit(PlayerDisconnectEvent eve) {
        PacketHandler.unhandle(eve.getPlayer());
    }
    
}
