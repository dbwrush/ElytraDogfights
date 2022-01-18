package me.sudologic.elytradogfights.commands;

import me.sudologic.elytradogfights.Main;
import me.sudologic.elytradogfights.inventory.Inventory;
import me.sudologic.elytradogfights.utility.GameplayTimer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.logging.Level;

public class CommandSoloStartGame implements CommandExecutor {
    Main main = Main.plugin;
    GameplayTimer timer = new GameplayTimer();
    Inventory inv = main.inventory;
    int time = main.soloTime;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        main.world = Bukkit.getWorld(main.worldName);
        if(sender instanceof Player && !sender.isOp()) {
            return false;
        }
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.getWorld() != main.world) {
                return false;
            }
        }
        if(main.gameIsRunning) {
            sender.sendMessage(ChatColor.RED + "Game is already running!");
            return false;
        }
        if(main.world.getPlayers().size() <= 1) {
            sender.sendMessage(ChatColor.RED + "Not enough players!");
            Bukkit.getLogger().log(Level.INFO, "[ElytraDogfights] Command /SoloStartGame recieved. Not enough players, cancelled.");
            for(Player p : main.world.getPlayers()) {
                p.sendMessage("[ElytraDogfights] Could not start game, too few players.");
            }
            return false;
        }
        main.TeamsMode = false;
        Location loc = new Location(main.world, main.solox, main.soloy, main.soloz);
        timer.startTimer(time);
        for(Player p : main.world.getPlayers()) {
            main.scoreboard().getTeam("Solo Team").addEntry(p.getName());
            p.setScoreboard((main.scoreboard()));
            inv.giveInventory(p, "solo");
            p.teleport(loc);
            main.inGame.add(p);
            p.setGameMode(GameMode.ADVENTURE);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }
        main.gameIsRunning = true;
        return true;
    }

    public void endGame() {
        main.scoreboard().clearSlot(DisplaySlot.SIDEBAR);
        main.inGame.get(0).teleport(main.world.getSpawnLocation());
        inv.returnInventory(main.inGame.get(0).getPlayer());

        for(Player p : main.world.getPlayers()) {
            p.sendMessage(ChatColor.GREEN + "A Winner is " + ChatColor.GOLD + main.inGame.get(0).getName() + ChatColor.GREEN + "!");
            p.setHealth(20);
            p.setFoodLevel(20);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        main.inGame.clear();
        main.gameIsRunning = false;
    }
}
