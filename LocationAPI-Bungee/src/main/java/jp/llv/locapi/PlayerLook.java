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

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

/**
 * PlayerLook
 *
 * @author toyblocks
 */
public class PlayerLook extends DefinedPacket {

    private float yaw, pitch;
    private boolean onGround;

    public PlayerLook() {
    }

    public PlayerLook(float yaw, float pitch, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void read(ByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Float.floatToIntBits(this.yaw);
        hash = 29 * hash + Float.floatToIntBits(this.pitch);
        hash = 29 * hash + (this.onGround ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerLook other = (PlayerLook) obj;
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (this.onGround != other.onGround) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PlayerLook{" + "yaw=" + yaw + ", pitch=" + pitch + ", onGround=" + onGround + '}';
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

}
