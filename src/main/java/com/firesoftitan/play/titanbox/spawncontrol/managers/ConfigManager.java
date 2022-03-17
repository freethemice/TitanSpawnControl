package com.firesoftitan.play.titanbox.spawncontrol.managers;

import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import com.firesoftitan.play.titanbox.spawncontrol.TitanSpawnControl;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.File;
import java.util.HashMap;


public class ConfigManager {
    private HashMap<String, SaveManager> configFiles;


    public ConfigManager() {
        reload();
    }
    public void reload()
    {
        configFiles = new HashMap<String, SaveManager>();
        SaveManager configFile = new SaveManager(TitanSpawnControl.instants.getName(), "config");
        configFiles.put("config", configFile);

        for (World world: Bukkit.getWorlds()) {
            File TitanBoxDIR = new File("plugins" + File.separator + TitanSpawnControl.instants.getName() + File.separator + world.getName());
            if (!TitanBoxDIR.exists()) {
                TitanBoxDIR.mkdir();
            }
            configFile = new SaveManager(TitanSpawnControl.instants.getName(), world.getName() + File.separator + "ALL");
            configFiles.put(world.getName() + ";ALL", configFile);
            if (!configFile.contains("settings.spawn")) {
                configFile.set("settings.DENY_ALL", false);
                for (CreatureSpawnEvent.SpawnReason spawnReason : CreatureSpawnEvent.SpawnReason.values()) {
                    configFile.set("settings.spawn." + spawnReason.name(), true);
                }
            }
            configFile.save();
            for (EntityType entityType : EntityType.values()) {
                if (entityType.isAlive() && entityType != EntityType.ARMOR_STAND && entityType != EntityType.PLAYER) {
                    configFile = new SaveManager(TitanSpawnControl.instants.getName(), world.getName() + File.separator + entityType.name());
                    configFiles.put(world.getName() + ";" + entityType.name(), configFile);
                    if (!configFile.contains("settings.spawn")) {
                        configFile.set("settings.DENY_ALL", false);
                        for (CreatureSpawnEvent.SpawnReason spawnReason : CreatureSpawnEvent.SpawnReason.values()) {
                            configFile.set("settings.spawn." + spawnReason.name(), true);
                        }
                    }
                    configFile.save();
                }
            }
        }
    }
    public Boolean canSpawn(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason)
    {
        EntityType type = entity.getType();
        String name = entity.getWorld().getName();
        if (!configFiles.containsKey(name + ";" + type.name())) return true;
        SaveManager configFile = configFiles.get(name + ";" + "ALL");
        if (configFile.getBoolean("settings.DENY_ALL")) return false;
        if (!configFile.getBoolean("settings.spawn." + spawnReason.name())) return false;
        configFile = configFiles.get(name + ";" + type.name());
        if (configFile.getBoolean("settings.DENY_ALL")) return false;
        if (!configFile.getBoolean("settings.spawn." + spawnReason.name())) return false;
        return true;
    }
}
