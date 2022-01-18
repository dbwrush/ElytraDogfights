package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamage implements Listener{
    Main main = Main.plugin;

    @EventHandler
    public void playerDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        if (event.getEntity() instanceof Player && main.inGame.contains(((Player)event.getEntity()).getPlayer())) return;
        event.setCancelled(true);
    }
}
