package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class GamemodeAnnouncement {
    Main main = Main.plugin;

    public void startGameModeAnnouncement() {
        main.scoreboard().getObjective("timer").setDisplaySlot(DisplaySlot.SIDEBAR);
        main.scoreboard().getObjective("timer").setDisplayName(ChatColor.GOLD + "-Game Mode-");
        main.scoreboard().getTeam("Game Mode: ").addEntry("Game Mode: ");
        if (main.TeamsMode == true) {
            main.scoreboard().getTeam("Game Mode: ").setSuffix("Teams");
        } else {
            main.scoreboard().getTeam("Game Mode: ").setSuffix("Solo");
        }
        main.scoreboard().getObjective("timer").getScore("Game Mode: ").setScore(3);
        for (Player p : main.world.getPlayers()) {

            p.setScoreboard(main.scoreboard());
        }
        new BukkitRunnable() {
            @Override
            public void run() {

                if (main.gameIsRunning == false) {
                    main.scoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 20);
    }
}
