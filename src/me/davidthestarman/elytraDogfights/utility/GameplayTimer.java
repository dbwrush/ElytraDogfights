package me.davidthestarman.elytraDogfights.utility;

import me.davidthestarman.elytraDogfights.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class GameplayTimer {
    Main main = Main.plugin;
    PlayerCount playerCount = new PlayerCount();
    GamemodeAnnouncement gamemode = new GamemodeAnnouncement();

    public void startTimer(int time) {
        playerCount.startPlayerCount();
        gamemode.startGameModeAnnouncement();

        main.scoreboard().getObjective("timer").setDisplaySlot(DisplaySlot.SIDEBAR);
        main.scoreboard().getObjective("timer").setDisplayName(ChatColor.GOLD + "- Elytra Dogfight -");
        new BukkitRunnable() {
            Integer time_ = time;

            public void run() {
                main.scoreboard().getTeam("Time Left: ").addEntry("Time Left: ");
                main.scoreboard().getTeam("Time Left: ").setSuffix((time_).toString());
                main.scoreboard().getObjective("timer").getScore("Time Left: ").setScore(1);
                for (Player p : main.world.getPlayers()) {
                    p.setScoreboard(main.scoreboard());
                }
                time_--;
                if (main.gameIsRunning == false) {
                    main.scoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    cancel();
                }
                if (time_ <= 29) {
                    for (Player p : main.world.getPlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                    }
                }
                if (time_ <= -1) {
                    endgame();
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 20);
    }

    public void endgame() {
        main.scoreboard().clearSlot(DisplaySlot.SIDEBAR);
        for(Player p : main.inGame) {
            if (main.scoreboard().getTeam("Red Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Red Team").removeEntry(p.getName());
            }
            if (main.scoreboard().getTeam("Blue Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("Blue Team").removeEntry(p.getName());
            }
            if (main.scoreboard().getTeam("FFA Team").hasEntry(p.getName())) {
                main.scoreboard().getTeam("FFA Team").removeEntry(p.getName());
            }
            if (main.blueInGame.contains(p)) {
                main.blueInGame.remove(p);
            }
            if (main.redInGame.contains(p)) {
                main.redInGame.remove(p);
            }
            p.teleport(main.world.getSpawnLocation());
            p.getInventory().clear();
        }
        for(Player p : main.world.getPlayers()) {
            p.sendMessage(ChatColor.GOLD + "The Game Was a Tie!");
            p.setHealth(20);
            p.setFoodLevel(20);
            p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1, 1);
            main.inGame.remove(p);
            main.gameIsRunning = false;
        }
    }
}
