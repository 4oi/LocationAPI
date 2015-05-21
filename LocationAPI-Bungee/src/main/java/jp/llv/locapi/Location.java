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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Location
 *
 * @author toyblocks
 */
public class Location implements Cloneable {

    private static final Map<ProxiedPlayer, Location> locations = new HashMap<>();

    /**
     * プレイヤーの現在位置を取得します.
     * 最後にハンドルされたパケットによる現在位置です。
     * クライアントから送信されるパケットのため、ハッククライアント等座標を偽っている場合は
     * 正常な座標が表示できません。
     * このメソッドから返却されるLocationはイミュータブルではありません。
     * パケットの受信に応じて動的に更新されます。
     * @param player 取得したいプレイヤー
     * @return 記録されている場合は現在地、そうでなければ{@code null}
     */
    public static Location of(ProxiedPlayer player) {
        return locations.get(player);
    }

    /**
     * パラメーターに対応する位置を返却します.
     * 与えていない情報はデフォルト値が用いられます
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @return 対応する位置
     */
    public static Location of(double x, double y, double z) {
        return new Location().update(x, y, z, false);
    }

    /**
     * パラメーターに対応する位置を返却します.
     * 与えていない情報はデフォルト値が用いられます
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @param yaw 水平角度
     * @param pitch 垂直角度
     * @param onGround 地面の上かどうか
     * @return 対応する位置
     */
    public static Location of(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return new Location().update(x, y, z, onGround).update(yaw, pitch);
    }

    /**
     * パラメーターに対応する位置を返却します.
     * 与えていない情報はデフォルト値が用いられます
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @param yaw 水平角度
     * @param pitch 垂直角度
     * @param onGround 地面の上かどうか
     * @param worldName ワールド名
     * @param worldUUID ワールドのUUID
     * @return 対応する位置
     */
    public static Location of(double x, double y, double z, float yaw, float pitch, boolean onGround, String worldName, UUID worldUUID) {
        return of(x, y, z, yaw, pitch, onGround).update(worldUUID, worldName);
    }

    protected static Location getOrCreateOf(ProxiedPlayer player) {
        Location result = Location.of(player);
        if (result != null) {
            return result;
        }
        locations.put(player, result = new Location());
        return result;
    }
    
    protected static void remove(ProxiedPlayer player) {
        locations.remove(player);
    }

    /**
     * ディメンション
     */
    public enum Dimension {

        /**
         * ネザー
         */
        NETHER(-1),
        /**
         * 通常世界
         */
        OVERWORLD(0),
        /**
         * エンド
         */
        END(1),;

        private final int id;

        private Dimension(int id) {
            this.id = id;
        }

        /**
         * IDからディメンションを取得します.
         * @param id ディメンションID
         * @return IDに対応するディメンション
         */
        public static Dimension ofId(int id) {
            for (Dimension d : values()) {
                if (d.id == id) {
                    return d;
                }
            }
            return null;
        }
    }

    private double x = Double.NaN, y = Double.NaN, z = Double.NaN;
    private float pitch = -1, yaw = -1;
    private boolean onGround = false;
    private Dimension dimension = null;
    private String worldName = null;
    private UUID worldUUID = null;

    private Location() {
    }

    /**
     * x座標を取得します.
     * @return x座標
     */
    public double getX() {
        return this.x;
    }

    /**
     * 整数値でx座標を取得します.
     * @return x座標
     */
    public long getBlockX() {
        return (long) Math.floor(x);
    }

    /**
     * y座標を取得します.
     * @return y座標
     */
    public double getY() {
        return this.y;
    }

    /**
     * 整数値でy座標を取得します.
     * @return y座標
     */
    public long getBlockY() {
        return (long) Math.floor(y);
    }

    /**
     * z座標を取得します.
     * @return z座標
     */
    public double getZ() {
        return this.z;
    }

    /**
     * 整数値でz座標を取得します.
     * @return z座標
     */
    public long getBlockZ() {
        return (long) Math.floor(z);
    }

    /**
     * 位置が地上にあるか取得します.
     * @return {@code true}なら地上にある、falseならそうでない
     */
    public boolean isOnGround() {
        return this.onGround;
    }

    protected Location update(double x, double y, double z, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        return this;
    }

    /**
     * 視点の水平角度を取得します.
     * @return 記録されていれば水平角度, そうでないなら-1
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * 視点の垂直角度を取得します.
     * @return 記録されていれば水平角度, そうでないなら-1
     */
    public float getPitch() {
        return this.pitch;
    }

    protected Location update(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        return this;
    }

    /**
     * 位置するディメンションを取得します.
     * @return 記録されているならディメンション, そうでないなら{@code null}
     */
    public Dimension getDimension() {
        return this.dimension;
    }

    protected void update(Dimension dimension) {
        this.dimension = dimension;
    }

    /**
     * 位置するワールドのUUIDを取得します.
     * 同一ディメンション複数ワールドはバニラサーバーではサポートされておらず、
     * パケットから取得できないため、取得にはBukkit-APIを実装するサーバー上で
     * LocationAPI-Bukkitプラグインを実行している必要があります。
     * @return 記録されているならワールドのUUID, そうでないなら{@code null}
     */
    public UUID getWorldUUID() {
        return this.worldUUID;
    }

    /**
     * 位置するワールドの名前を取得します.
     * 同一ディメンション複数ワールドはバニラサーバーではサポートされておらず、
     * パケットから取得できないため、取得にはBukkit-APIを実装するサーバー上で
     * LocationAPI-Bukkitプラグインを実行している必要があります。
     * @return 記録されているならワールドの名前, そうでないなら{@code null}
     */
    public String getWorldName() {
        return this.worldName;
    }

    protected Location update(UUID uuid, String name) {
        this.worldName = name;
        this.worldUUID = uuid;
        return this;
    }

    /**
     * 他の位置のxyz値との和を座標とする新たな位置を取得します.
     * @param other もう一方
     * @return 自身の値をxyz値のみ更新した新たな位置
     */
    public Location add(Location other) {
        return new Location().update(this.x + other.x, this.y + other.y, this.z + other.z, this.onGround).update(this.yaw, this.pitch).update(this.worldUUID, this.worldName);
    }

    /**
     * 他の位置のxyz値との差を座標とする新たな位置を取得します.
     * @param other もう一方
     * @return 自身の値をxyz値のみ更新した新たな位置
     */
    public Location subtract(Location other) {
        return new Location().update(this.x - other.x, this.y - other.y, this.z - other.z, this.onGround).update(this.yaw, this.pitch).update(this.worldUUID, this.worldName);
    }
    
    /**
     * xyz値をを一定倍した新たな位置を取得します.
     * @param m 倍率
     * @return 自身の値をxyz値のみ更新した新たな位置
     */
    public Location multiply(double m) {
        return this.clone().update(this.x*m, this.y*m, this.z*m, this.onGround);
    }

    /**
     * 原点からの距離を取得します.
     * @return 原点からの距離
     * @see #lengthSquared() 
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * 原点からの距離をの二乗を取得します.
     * 距離の比較を行いたい場合は、{@link #length() }を用いるより、二乗を比較する方が軽量です。
     * @return 原点からの距離の二乗
     * @see #length() 
     */
    public double lengthSquared() {
        return Math.pow(this.x, 2D) + Math.pow(this.y, 2D) + Math.pow(this.z, 2D);
    }

    /**
     * 二座標間の距離を取得します.
     * @param o もう一点
     * @return 二座標間の距離
     * @see #distanceSquared(jp.llv.locapi.Location) 
     */
    public double distance(Location o) {
        return Math.sqrt(this.distanceSquared(o));
    }

     /**
     * 二座標間の距離の二乗を取得します.
     * 距離の比較を行いたい場合は、{@link #distance(jp.llv.locapi.Location) }を用いるより、二乗を比較する方が軽量です。
     * @param o もう一点
     * @return 二座標間の距離の二乗
     * @see #distance(jp.llv.locapi.Location) 
     */
    public double distanceSquared(Location o) {
        return Math.pow(this.x - o.x, 2D) + Math.pow(this.y - o.y, 2D) + Math.pow(this.z - o.z, 2D);
    }

    /**
     * xyz値とディメンションからハッシュ値を取得します
     * @return ハッシュ値
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 59 * hash + Objects.hashCode(this.dimension);
        return hash;
    }

    /**
     * 同値性を検証します
     * @param obj 比較対象
     * @return {@code true}なら同値である、falseならそうでない
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        if (this.dimension != other.dimension) {
            return false;
        }
        return Objects.equals(this.worldUUID, other.worldUUID);
    }

    /**
     * 位置を文字列に変換します.
     * すべての変数の中身が列挙されます
     * @return 文字列に変換された位置
     */
    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + ", z=" + z + ", pitch=" + pitch + ", yaw=" + yaw + ", onGround=" + onGround + ", dimension=" + dimension + ", worldName=" + worldName + ", worldUUID=" + worldUUID + '}';
    }

    /**
     * 同一の値を持つ異なるインスタンスを生成します
     * @return この位置のクローン
     */
    @Override
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }
    }

}
