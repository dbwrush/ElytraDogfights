package me.davidthestarman.elytraDogfights.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;

public class Inventory {

    //hashmap of all saved player inventories.
    public static HashMap<Player, ItemStack[]> savedInventories = new HashMap<Player, ItemStack[]>();

    //Itemstacks for all the items a player will need in Teams mode.
    private static ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
    private static ItemMeta swordMeta = sword.getItemMeta();

    private static ItemStack bow = new ItemStack(Material.BOW, 1);
    private static ItemMeta bowMeta = bow.getItemMeta();

    private static ItemStack arrow = new ItemStack(Material.ARROW, 1);

    private static ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET, 64);
    private static FireworkMeta rocketMeta = (FireworkMeta) rocket.getItemMeta();

    private static ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
    private static ItemMeta elytraMeta = elytra.getItemMeta();

    private static ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
    private static LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();

    private static ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    private static LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

    private static ItemStack[] ffaArmor = new ItemStack[4];
    private static ItemStack[] redArmor = new ItemStack[4];
    private static ItemStack[] blueArmor = new ItemStack[4];

    public Inventory() {
        //adds attributes to each item before giving it to the player
        swordMeta.setDisplayName(ChatColor.AQUA + "Whacker");
        swordMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        swordMeta.setUnbreakable(true);
        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        sword.setItemMeta(swordMeta);

        bowMeta.setDisplayName(ChatColor.RED + "Pew Pew");
        bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        bowMeta.setUnbreakable(true);
        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        bow.setItemMeta(bowMeta);

        rocketMeta.setPower(2);
        rocket.setItemMeta(rocketMeta);

        armorSetup(Color.GRAY, ffaArmor);
        armorSetup(Color.RED, redArmor);
        armorSetup(Color.BLUE, blueArmor);
    }

    public void armorSetup(Color color, ItemStack[] armor) {
        elytraMeta.setUnbreakable(true);
        elytra.setItemMeta(elytraMeta);

        helmetMeta.setColor(color);
        helmetMeta.setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);

        bootsMeta.setColor(color);
        bootsMeta.setUnbreakable(true);
        bootsMeta.addEnchant(Enchantment.PROTECTION_FALL, 3, true);
        boots.setItemMeta(bootsMeta);

        armor[0] = boots.clone();
        armor[1] = null;
        armor[2] = elytra;
        armor[3] = helmet.clone();

    }

    public void giveInventory(Player player, String team) {
        saveInventory(player);
        player.getInventory().clear();
        player.getInventory().setItem(0, sword);
        player.getInventory().setItem(1, bow);
        player.getInventory().setItem(9, arrow);
        player.getInventory().setItemInOffHand(rocket);
        switch (team) {
            case "ffa":
                ((HumanEntity) player).getInventory().setArmorContents(ffaArmor);
                break;
            case "red":
                ((HumanEntity) player).getInventory().setArmorContents(redArmor);
                break;
            case "blue":
                ((HumanEntity) player).getInventory().setArmorContents(blueArmor);
                break;
        }
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    //save player's inventory to savedInventories
    public void saveInventory(Player p) {
        savedInventories.put(p, p.getInventory().getContents());
    }

    //return player's inventory after a game and removes the save from savedInventories
    public void returnInventory(Player p) {
        p.getInventory().setContents(savedInventories.get(p));
        savedInventories.remove(p);
    }
}
