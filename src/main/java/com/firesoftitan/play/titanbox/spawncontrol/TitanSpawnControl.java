package com.firesoftitan.play.titanbox.spawncontrol;

import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.firesoftitan.play.titanbox.spawncontrol.listeners.MainListener;
import com.firesoftitan.play.titanbox.spawncontrol.managers.ConfigManager;
import com.firesoftitan.play.titanbox.spawncontrol.runnables.SaveRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class TitanSpawnControl extends JavaPlugin {

    public static MainListener mainListener;
    public static Tools tools;
    public static TitanSpawnControl instants;
    public static ConfigManager configManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instants = this;
        mainListener = new MainListener(this);
        mainListener.registerEvents();
        tools = new Tools(this, new SaveRunnable(), -1);
        new BukkitRunnable() {
            @Override
            public void run() {
                configManager = new ConfigManager();
            }
        }.runTaskLater(this, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
