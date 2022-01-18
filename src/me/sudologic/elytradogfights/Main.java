package me.sudologic.elytradogfights;

import me.sudologic.elytradogfights.commands.CommandFFAStartGame;
import me.sudologic.elytradogfights.commands.CommandLeave;
import me.sudologic.elytradogfights.commands.CommandTeamsStartGame;
import me.sudologic.elytradogfights.commands.CommandToggleGameIsRunning;
import me.sudologic.elytradogfights.inventory.Inventory;
import me.sudologic.elytradogfights.inventory.InventoryManager;
import me.sudologic.elytradogfights.inventory.ThrownItemDetector;
import me.sudologic.elytradogfights.utility.*;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    //These vars will be filled with data from the config file
    public String worldName;
    public int ffax;
    public int ffay;
    public int ffaz;
    public int redx;
    public int redy;
    public int redz;
    public int bluex;
    public int bluey;
    public int bluez;
    public String jumpPadType;
    public int jumpPadStrength;
    public int ffaTime;
    public int teamsTime;
    public boolean useAimAssist;
    public double aimAssistStrength;
    public boolean useQuickFire;
    public int aimAssistTolerance;
    public double autoFireArrowSpeed;

    public boolean gameIsRunning = false;
    public List<Player> inGame = new ArrayList<>();
    public List<Player> redInGame = new ArrayList<>();
    public List<Player> blueInGame = new ArrayList<>();

    public boolean TeamsMode = false;

    public World world;

    public Inventory inventory = new Inventory();

    //End vars for gameplay

    public static Main plugin;
    public void Plugin(Main plugin) {Main.plugin = plugin;}
    public static Main getPlugin() {return plugin;}

    @Override
    public void onEnable() {
        Plugin(this);
        Bukkit.getLogger().log(Level.INFO, "[ElytraDogfights] Plugin has started!");

        createCustomConfig();
        createConfigs();

        createScoreboard();
        registerCommands();
        registerListeners();

        world = Bukkit.getWorld(worldName);
        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "[ElytraDogfights] Plugin has shut down!");
    }

    public void registerCommands() {
        this.getCommand("FFAStartGame").setExecutor(new CommandFFAStartGame());
        this.getCommand("ToggleGameIsRunning").setExecutor(new CommandToggleGameIsRunning());
        this.getCommand("TeamsStartGame").setExecutor(new CommandTeamsStartGame());
        this.getCommand("Leave").setExecutor(new CommandLeave());
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnPlayerJoin(), this);
        pluginManager.registerEvents(new InventoryManager(), this);
        pluginManager.registerEvents(new ThrownItemDetector(), this);
        pluginManager.registerEvents(new OnPlayerLeave(), this);
        pluginManager.registerEvents(new OnPlayerDamage(), this);
        pluginManager.registerEvents(new JumpPad(), this);
        pluginManager.registerEvents(new AimAssist(), this);
        pluginManager.registerEvents(new QuickFire(), this);
        pluginManager.registerEvents(new HungerManagement(), this);
    }

    public Scoreboard board;

    public void createScoreboard() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective gameTimer = board.registerNewObjective("timer", "dummy", "timer");
        Team timeLeft = board.registerNewTeam("Time Left: ");
        Team playerCountTeam = board.registerNewTeam("Players: ");
        Team gamemodeAnnounceTeam = board.registerNewTeam("Game Mode: ");

        Team FFATeam = board.registerNewTeam("FFA Team");
        Team RedTeam = board.registerNewTeam("Red Team");
        Team BlueTeam = board.registerNewTeam("Blue Team");

        RedTeam.setAllowFriendlyFire(false);
        BlueTeam.setAllowFriendlyFire(false);
        FFATeam.setColor(ChatColor.GRAY);
        RedTeam.setColor(ChatColor.RED);
        BlueTeam.setColor(ChatColor.BLUE);
    }

    public Scoreboard scoreboard() {
        return board;
    }

    public FileConfiguration getCustomConfig() {return this.customConfig;}

    public void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try{
            customConfig.load(customConfigFile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createConfigs() {
        this.saveDefaultConfig();
        this.getConfig();
        FileConfiguration config = this.getConfig();

        worldName = config.getString("world");
        ffax = config.getInt("ffax");
        ffay = config.getInt("ffay");
        ffaz = config.getInt("ffaz");
        redx = config.getInt("redx");
        redy = config.getInt("redy");
        redz = config.getInt("redz");
        bluex = config.getInt("bluex");
        bluey = config.getInt("bluey");
        bluez = config.getInt("bluez");
        jumpPadType = config.getString("jumpPadType");
        jumpPadStrength = config.getInt("jumpPadStrength");
        ffaTime = config.getInt("ffaTime");
        teamsTime = config.getInt("teamTime");
        useAimAssist = config.getBoolean("useAimAssist");
        aimAssistStrength = config.getDouble("aimAssistStrength");
        useQuickFire = config.getBoolean("useQuickFire");
        aimAssistTolerance = config.getInt("aimAssistTolerance");
        autoFireArrowSpeed = config.getDouble("autoFireArrowSpeed");
    }

    /*
    todo:
        1. Support for 3+ teams
        2. Custom team names and colors
        3. Spectator mode
        4. Join command / opt-in
        5. Multiple arenas
        6. Multiple simultaneous games

     */
}