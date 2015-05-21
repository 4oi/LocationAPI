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
 * PositionAndLook
 * @author toyblocks
 */
public class PlayerPositionAndLook extends DefinedPacket {

    private double x, y, z;
    private float yaw, pitch;
    private byte flag;

    public PlayerPositionAndLook(){}
    
    @Override
    public void read(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.flag = buf.readByte();
    }
    
    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.getX()) ^ (Double.doubleToLongBits(this.getX()) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.getY()) ^ (Double.doubleToLongBits(this.getY()) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.getZ()) ^ (Double.doubleToLongBits(this.getZ()) >>> 32));
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
        final PlayerPositionAndLook other = (PlayerPositionAndLook) obj;
        if (Double.doubleToLongBits(this.getX()) != Double.doubleToLongBits(other.getX())) {
            return false;
        }
        if (Double.doubleToLongBits(this.getY()) != Double.doubleToLongBits(other.getY())) {
            return false;
        }
        if (Double.doubleToLongBits(this.getZ()) != Double.doubleToLongBits(other.getZ())) {
            return false;
        }
        if (Float.floatToIntBits(this.getYaw()) != Float.floatToIntBits(other.getYaw())) {
            return false;
        }
        if (Float.floatToIntBits(this.getPitch()) != Float.floatToIntBits(other.getPitch())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PlayerPositionAndLook{" + "x=" + getX() + ", y=" + getY() + ", z=" + getZ() + ", yaw=" + getYaw() + ", pitch=" + getPitch() + '}';
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public byte getFlag() {
        return flag;
    }
    
}
