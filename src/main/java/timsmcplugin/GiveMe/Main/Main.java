package timsmcplugin.GiveMe.Main;

import org.bukkit.plugin.java.JavaPlugin;
import timsmcplugin.GiveMe.TimsCustomCommands.TimsCustomCommands;

//ONLY the Main class needs to extends JavaPlugin! and ONLY ONE class can extend JavaPlugin
//https://bukkit.org/threads/pluginalreadyinitialized-exception.214917/
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Enabled!");
        this.getCommand("giveme").setExecutor(new TimsCustomCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}