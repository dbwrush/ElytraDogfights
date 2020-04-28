package me.davidthestarman.elytraDogfights.utility;

import me.davidthestarman.elytraDogfights.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerManagement implements Listener {
    Main main = Main.plugin;
    @EventHandler
    public void playerHunger(FoodLevelChangeEvent e) {
        if(e.getEntity().getWorld().equals(main.world)) {
            e.setCancelled(true);
        }
    }
}
