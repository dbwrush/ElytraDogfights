package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

public class QuickFire implements Listener {
    Main main = Main.plugin;

    @EventHandler
    public void quickFire(PlayerInteractEvent e) {
        if(main.useQuickFire == false || e.getPlayer().getWorld().equals(main.world) == false) {
            return;
        }
        if(main.gameIsRunning == false ) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if(item == null) {
            return;
        }
        if(item.getType().equals(Material.BOW)) {
            Arrow arrow = p.getLocation().getWorld().spawnArrow(p.getEyeLocation(), p.getEyeLocation().getDirection(), (float) main.autoFireArrowSpeed, (float) 0);
            AimAssist.adjustArrow(arrow, p);
            arrow.setShooter(p);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
            p.updateInventory();
            e.setCancelled(true);
        }
    }
}