package fr.whitefox.heracore;

import fr.whitefox.heracore.commands.*;
import fr.whitefox.heracore.db.*;
import fr.whitefox.heracore.events.*;
import net.luckperms.api.LuckPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin {

    private static Main instance;
    public ArrayList<Player> invisible_list = new ArrayList<>();
    public ArrayList<Player> freeze_list = new ArrayList<>();
    public ArrayList<Player> fly_list = new ArrayList<>();
    public ArrayList<Player> pday_list = new ArrayList<>();
    public ArrayList<Player> pnight_list = new ArrayList<>();
    public ArrayList<Player> antiflood_list = new ArrayList<>();
    public List<String> automod;
    public SQLite sqlite = new SQLite();
    public PlayerInfos playerInfos = new PlayerInfos();
    public BanManager banManager = new BanManager();
    public MuteManager muteManager = new MuteManager();
    public HistoryManager historyManager = new HistoryManager();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[HeraCore] Starting...");

        instance = this;
        LuckPerms luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        try {
            sqlite.connect("HeraCore.db");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            sqlite.initTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        saveDefaultConfig();

        automod = getConfig().getStringList("blacklist");

        getCommand("dupeip").setExecutor(new DupeipCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("tpall").setExecutor(new TeleportationCommand());
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SpawnCommand());
        getCommand("shutdown").setExecutor(new ShutdownCommand());
        getCommand("sun").setExecutor(new WeatherCommand());
        getCommand("rain").setExecutor(new WeatherCommand());
        getCommand("thunder").setExecutor(new WeatherCommand());
        getCommand("antivpn").setExecutor(new AntiVPNCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("day").setExecutor(new TimeCommand());
        getCommand("night").setExecutor(new TimeCommand());
        getCommand("pday").setExecutor(new TimeCommand());
        getCommand("pnight").setExecutor(new TimeCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new BanCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new MuteCommand());
        getCommand("inf").setExecutor(new InfCommand());
        getCommand("history").setExecutor(new HistoryCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("sethome").setExecutor(new HomeCommand());
        getCommand("delhome").setExecutor(new HomeCommand());
        getCommand("homes").setExecutor(new HomeCommand());

        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(luckPerms), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);

        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[HeraCore] Up !");
        getServer().getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        instance = null;
        sqlite.disconnect();

        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[HeraCore] Goodbye :)");
        getServer().getConsoleSender().sendMessage(" ");
    }
}