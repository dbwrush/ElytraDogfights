package me.davidthestarman.elytraDogfights.commands;

import me.davidthestarman.elytraDogfights.Main;
import me.davidthestarman.elytraDogfights.inventory.Inventory;
import me.davidthestarman.elytraDogfights.utility.OnPlayerLeave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandLeave implements CommandExecutor {
    Main main = Main.plugin;

    Inventory inv = main.inventory;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player == false) {
            return false;
        }
        Player p = (Player) sender;
        if(main.inGame.contains(p)) {
            main.inGame.remove(p);
            if(main.scoreboard().getTeam("FFA Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("FFA Team").removeEntry(p.getName());
                OnPlayerLeave.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has left the match!");
            } else if(main.scoreboard().getTeam("Blue Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Blue Team").removeEntry(p.getName());
                main.blueInGame.remove(p);
                OnPlayerLeave.sendMessage(ChatColor.BLUE + p.getName() + ChatColor.GREEN + " has left the match!");
            } else if(main.scoreboard().getTeam("Red Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Red Team").removeEntry(p.getName());
                main.redInGame.remove(p);
                OnPlayerLeave.sendMessage(ChatColor.RED + p.getName() + ChatColor.GREEN + " has left the match!");
            }
            p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
            p.teleport(p.getWorld().getSpawnLocation());
            inv.returnInventory(p);
            OnPlayerLeave.checkInGame();
            p.sendMessage(ChatColor.GREEN + "You have left the game!");
            return true;
        }
        p.sendMessage(ChatColor.GREEN + "You are not in a game!");
        return false;
    }
}
