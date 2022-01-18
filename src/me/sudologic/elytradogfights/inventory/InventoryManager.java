package me.sudologic.elytradogfights.inventory;

import me.sudologic.elytradogfights.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryManager implements Listener {
    Main main = Main.plugin;

    @EventHandler
    public void playerInventoryChanged(InventoryClickEvent event)
    {
        //check for null clicks, creative players, and ops. Otherwise, cancel the inventory click.
        if(event.getClickedInventory()  ==  null || (event.getWhoClicked().getGameMode()  ==  GameMode.CREATIVE && event.getWhoClicked().isOp())) return;
        if(main.gameIsRunning==false) {
            event.setCancelled(true);
        }
        else if(event.getSlotType()== InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }
}
