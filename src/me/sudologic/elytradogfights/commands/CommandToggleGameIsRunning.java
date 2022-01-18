package me.sudologic.elytradogfights.commands;

import me.sudologic.elytradogfights.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class CommandToggleGameIsRunning implements CommandExecutor {
    Main main = Main.plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Bukkit.getLogger().log(Level.INFO, "[ElytraDogfights] Command /toggleGameIsRunning recieved");
        if(!sender.isOp()) {
            return false;
        }
        main.gameIsRunning = !main.gameIsRunning;
        sender.sendMessage("gameIsRunning has been set to" + main.gameIsRunning);
        return true;
    }
}
