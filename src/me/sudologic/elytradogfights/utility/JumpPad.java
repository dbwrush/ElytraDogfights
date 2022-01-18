package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class JumpPad implements Listener {

    Main main = Main.plugin;
    String jumpPadType = main.jumpPadType;
    int jumpPadStrength = main.jumpPadStrength;

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        Location location = event.getPlayer().getLocation();
        Block block = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() -1, location.getBlockZ()).getBlock();
        String blockString = block.getType().toString();
        Vector up = new Vector(0, jumpPadStrength, 0);
        if (blockString.equals(jumpPadType)) {
            event.getPlayer().setVelocity(up);
            event.getPlayer().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 10, 1);
        }
    }
}