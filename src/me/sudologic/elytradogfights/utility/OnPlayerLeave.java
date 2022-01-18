package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import me.sudologic.elytradogfights.commands.CommandFFAStartGame;
import me.sudologic.elytradogfights.commands.CommandTeamsStartGame;
import me.sudologic.elytradogfights.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerLeave implements Listener {
    static CommandFFAStartGame cmdFFA = new CommandFFAStartGame();
    static CommandTeamsStartGame cmdTeams = new CommandTeamsStartGame();

    GameplayTimer timer = new GameplayTimer();
    static Main main = Main.plugin;

    Inventory inv = main.inventory;

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity().getPlayer();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.teleport(p.getWorld().getSpawnLocation());
        p.setGliding(false);
        p.setSwimming(false);
        if(main.inGame.contains(p)) {
            main.inGame.remove(p);
            if(main.scoreboard().getTeam("FFA Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("FFA Team").removeEntry(p.getName());
                sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has died!");
            } else if(main.scoreboard().getTeam("Blue Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Blue Team").removeEntry(p.getName());
                main.blueInGame.remove(p);
                sendMessage(ChatColor.BLUE + p.getName() + ChatColor.GREEN + " has died!");
            } else if(main.scoreboard().getTeam("Red Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Red Team").removeEntry(p.getName());
                main.redInGame.remove(p);
                sendMessage(ChatColor.RED + p.getName() + ChatColor.GREEN + " has died!");
            }
            inv.returnInventory(p);
            checkInGame();
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(main.inGame.contains(p)) {
            main.inGame.remove(p);
            if(main.scoreboard().getTeam("FFA Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("FFA Team").removeEntry(p.getName());
                sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has left the match!");
            } else if(main.scoreboard().getTeam("Blue Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Blue Team").removeEntry(p.getName());
                main.blueInGame.remove(p);
                sendMessage(ChatColor.BLUE + p.getName() + ChatColor.GREEN + " has left the match!");
            } else if(main.scoreboard().getTeam("Red Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Red Team").removeEntry(p.getName());
                main.redInGame.remove(p);
                sendMessage(ChatColor.RED + p.getName() + ChatColor.GREEN + " has left the match!");
            }
            p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
            p.teleport(p.getWorld().getSpawnLocation());
            inv.returnInventory(p);
            checkInGame();
        }
    }

    @EventHandler
    public void playerChangeWorld(PlayerChangedWorldEvent event) {
        if(main.world == null) {
            main.world = Bukkit.getWorld(main.worldName);
        }

        Player p = event.getPlayer();
        if(main.inGame.contains(p)) {
            main.inGame.remove(p);
            checkInGame();
        }
        if(inv.savedInventories.containsKey(p)) {
            inv.returnInventory(p);
        }
        p.setScoreboard(main.getServer().getScoreboardManager().getNewScoreboard());
    }

    public static void checkInGame() {
        if(!main.TeamsMode) {
            if(main.inGame.size() <= 1) {
                cmdFFA.endGame();
            }
        } else {
            if(main.blueInGame.size() <= 0 || main.redInGame.size() <= 0) {
                cmdTeams.endGame();
            }
        }
    }

    public static void sendMessage(String message) {
        for(Player player : main.inGame) {
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }
}
