package net.cosmosmc.mcze.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class Particles {

    private static String version = "";

    private static Object packet;

    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Method valueOf;

    private static Class<?> packetType;

    static {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            final String OBC = "org.bukkit.craftbukkit." + version;
            final String NMS = "net.minecraft.server." + version;

            packetType = Class.forName(NMS + ".PacketPlayOutWorldParticles");

            Class<?> typeEnumParticle = Class.forName(NMS + ".EnumParticle");
            valueOf = typeEnumParticle.getMethod("valueOf", String.class);

            Class<?> typeCraftPlayer = Class.forName(OBC + ".entity.CraftPlayer");
            Class<?> typeNMSPlayer = Class.forName(NMS + ".EntityPlayer");
            Class<?> typePlayerConnection = Class.forName(NMS + ".PlayerConnection");
            getHandle = typeCraftPlayer.getMethod("getHandle");
            playerConnection = typeNMSPlayer.getField("playerConnection");
            sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName(NMS + ".Packet"));
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Error {0}", ex);
        }
    }

    private static void setField(String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = packet.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(packet, value);
    }

    private static void sendPacket(Player receivingPacket, Object packet) throws Exception {
        Object player = getHandle.invoke(receivingPacket);
        Object connection = playerConnection.get(player);
        sendPacket.invoke(connection, packet);
    }

    public static void spawnParticles(Location loc, Player receivingPacket, int amount, String... packetName) {
        try {
            packet = packetType.newInstance();

            if (Integer.valueOf(version.split("_")[1]) > 7 && Integer.valueOf(version.toLowerCase().split("_")[0].replace("v", "")) == 1) {
                setField("a", valueOf.invoke(null, packetName[0]));
                setField("b", loc.getBlockX());
                setField("c", loc.getBlockY());
                setField("d", loc.getBlockZ());
                setField("e", 1);
                setField("f", 1);
                setField("g", 1);
                setField("h", 0);
                setField("i", amount);
                setField("j", true);
            }

            if(receivingPacket == null) {
                for(Player receivingPacket2 : Bukkit.getOnlinePlayers()) {
                    sendPacket(receivingPacket2, packet);
                }
            } else {
                sendPacket(receivingPacket, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}