package me.davidthestarman.elytraDogfights.inventory;

import me.davidthestarman.elytraDogfights.Main;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ThrownItemDetector implements Listener {
    Main main = Main.plugin;

    @EventHandler
    public void playerThrownItem(PlayerDropItemEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE && event.getPlayer().isOp()) return;
        if(event.getPlayer().getWorld() != main.world) return;
        event.setCancelled(true);
    }
}