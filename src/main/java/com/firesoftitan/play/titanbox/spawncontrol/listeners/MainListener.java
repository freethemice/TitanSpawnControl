package com.firesoftitan.play.titanbox.spawncontrol.listeners;

import com.firesoftitan.play.titanbox.spawncontrol.TitanSpawnControl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MainListener implements Listener {
    private JavaPlugin plugin;
    public MainListener(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public void registerEvents(){
        PluginManager pm = this.plugin.getServer().getPluginManager();
        pm.registerEvents(this, this.plugin);
    }
    @EventHandler
    public void onSpawnEvent(CreatureSpawnEvent event)
    {
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
        if (TitanSpawnControl.configManager == null) {
           event.setCancelled(true);
           return;
        }
        if (!TitanSpawnControl.configManager.canSpawn(event.getEntity(),spawnReason)) event.setCancelled(true);
    }
}
