package me.sudologic.elytradogfights.utility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import me.sudologic.elytradogfights.Main;

public class PlayerCount {
    Main main = Main.plugin;
    Scoreboard board = main.scoreboard();

    public void startPlayerCount() {
        main.scoreboard().getObjective("timer").setDisplaySlot(DisplaySlot.SIDEBAR);
        main.scoreboard().getObjective("timer").setDisplayName(ChatColor.GOLD + "-Player Count-");
        new BukkitRunnable() {
            @Override
            public void run() {
                main.scoreboard().getTeam("Players: ").addEntry("Players: ");
                main.scoreboard().getTeam("Players: ").setSuffix(((Integer)main.inGame.size()).toString());
                main.scoreboard().getObjective("timer").getScore("Players: ").setScore(2);
                for(Player p : main.world.getPlayers()) {
                    p.setScoreboard(board);
                }
                if(main.gameIsRunning == false) {
                    board.clearSlot(DisplaySlot.SIDEBAR);
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 20);
    }
}
