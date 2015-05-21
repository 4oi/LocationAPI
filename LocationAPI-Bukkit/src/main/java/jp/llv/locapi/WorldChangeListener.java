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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WorldChangeListener
 * @author toyblocks
 */
public class WorldChangeListener extends JavaPlugin implements Listener {
    
    private static final String WORLD_MESSAGE_TAG = "LAPIW";
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent eve) {
        sendPM(eve.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChangeWorld(PlayerChangedWorldEvent eve) {
        sendPM(eve.getPlayer());
    }
    
    private void sendPM(Player p) {
        UUID uuid = p.getWorld().getUID();
        byte[] nameBytes = p.getWorld().getName().getBytes(Charset.forName("UTF-8"));
        ByteBuffer buf = ByteBuffer.allocate(nameBytes.length + 128);
        buf.putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
        buf.put(nameBytes);
        byte[] mes = new byte[buf.remaining()];
        buf.get(mes);
        p.sendPluginMessage(this, WORLD_MESSAGE_TAG, mes);
    }
    
}
