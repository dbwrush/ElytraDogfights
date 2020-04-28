package me.davidthestarman.elytraDogfights.commands;

import me.davidthestarman.elytraDogfights.Main;
import me.davidthestarman.elytraDogfights.inventory.Inventory;
import me.davidthestarman.elytraDogfights.utility.GameplayTimer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Collections;
import java.util.logging.Level;

public class CommandTeamsStartGame implements CommandExecutor {
    Main main = Main.plugin;
    GameplayTimer timer = new GameplayTimer();

    Inventory inv = main.inventory;

    int time = main.teamsTime;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
        if(main.gameIsRunning == true) {
            sender.sendMessage(ChatColor.RED + "Game is already running!");
            return false;
        }
        if(main.world.getPlayers().size() <= 1) {
            sender.sendMessage(ChatColor.RED + "Not enough players!");
            Bukkit.getLogger().log(Level.INFO, "[ElytraDogfights] Command /teamsStartGame recieved. Not enough players, cancelled.");
            for(Player p : main.world.getPlayers()) {
                p.sendMessage("[ElytraDogfights] Could not start game, too few players.");
            }
            return false;
        }
        main.TeamsMode = true;
        Location bLoc = new Location(main.world, main.bluex, main.bluey, main.bluez);
        Location rLoc = new Location(main.world, main.redx, main.redy, main.redz);
        timer.startTimer(time);
        for(Player p : main.world.getPlayers()) {
            main.inGame.add(p);
        }
        Collections.shuffle(main.inGame);
        for(Player p : main.inGame) {
            if(main.inGame.indexOf(p) % 2 == 0) {
                main.scoreboard().getTeam("Blue Team").addEntry(p.getName());
                p.setScoreboard(main.scoreboard());
                main.blueInGame.add(p);
                p.teleport(bLoc);
                inv.giveInventory(p, "blue");
            } else {
                main.scoreboard().getTeam("Red Team").addEntry(p.getName());
                p.setScoreboard(main.scoreboard());
                main.redInGame.add(p);
                p.teleport(rLoc);
                inv.giveInventory(p, "red");
            }
            p.setGameMode(GameMode.ADVENTURE);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }
        main.gameIsRunning = true;
        return true;
    }

    public void endGame() {
        main.scoreboard().clearSlot(DisplaySlot.SIDEBAR);
        String winningTeamName = null;
        ChatColor teamColor = null;
        main.inGame.get(0).teleport(main.world.getSpawnLocation());
        main.inGame.get(0).getPlayer().getInventory().clear();
        if(main.blueInGame.size() <= 0) {
            winningTeamName = "Red Team";
            teamColor = ChatColor.RED;
            for(Player p : main.redInGame) {
                main.scoreboard().getTeam("Red Team").removeEntry(p.getName());
                p.teleport(main.world.getSpawnLocation());
                inv.returnInventory(p);
            }
        } else {
            winningTeamName = "Blue Team";
            teamColor = ChatColor.BLUE;
            for(Player p : main.blueInGame) {
                main.scoreboard().getTeam("Blue Team").removeEntry(p.getName());
                p.teleport(main.world.getSpawnLocation());
                inv.returnInventory(p);
            }
        }

        for(Player p : main.world.getPlayers()) {
            p.sendMessage(ChatColor.GREEN + "A winner is " + teamColor + winningTeamName + ChatColor.GREEN + "!");
            p.setHealth(20);
            p.setFoodLevel(20);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        main.inGame.clear();
        main.blueInGame.clear();
        main.redInGame.clear();
        main.gameIsRunning = false;
    }
}
