package net.enelson.sopcamera;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;

import net.enelson.sopcamera.command.MainCommand;
import net.enelson.sopcamera.data.ResourcePackManager;
import net.enelson.sopcamera.utils.Utils;

public class SopCamera extends JavaPlugin {

	private static SopCamera instance;
	private YamlConfiguration config;
	private File fileMissed;
	private FileConfiguration missed;
	private ResourcePackManager rpManager;
	
	public void onEnable() {
		instance = this;

		File fileConfig = new File(getDataFolder(), "config.yml");
		if (!fileConfig.exists()) saveResource("config.yml", true);
		config = YamlConfiguration.loadConfiguration(fileConfig);
		
		this.fileMissed = new File(getDataFolder(), "missed.yml");
		if (!fileMissed.exists()) saveResource("missed.yml", true);
		missed = YamlConfiguration.loadConfiguration(fileMissed);
		
		File mapDir = new File(getDataFolder(), "maps");
		if (!mapDir.exists()) {
			mapDir.mkdir();
		}
		
		this.rpManager = new ResourcePackManager();
		this.rpManager.initialize();
		
		Utils.loadColors();
		
		this.getCommand("sopcamera").setExecutor(new MainCommand());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		try {
			String pac = "net.enelson.sopcamera.listeners";
			for (ClassPath.ClassInfo clazzInfo : ClassPath.from(getClassLoader()).getTopLevelClasses(pac)) {
				Class<?> clazz = Class.forName(clazzInfo.getName());
				if (Listener.class.isAssignableFrom(clazz)) {
					pluginManager.registerEvents((Listener) clazz.getDeclaredConstructor().newInstance(), this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				saveMissed();
			}
		}, 20*60*10, 20*60*10);
	}
	
	public static SopCamera getInstance() {
		return instance;
	}
	
	public YamlConfiguration getConfig() {
		return this.config;
	}
	
	public ResourcePackManager getResourcePackManager() {
		return this.rpManager;
	}
	
	private void saveMissed() {
		try {
			this.missed.save(this.fileMissed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateMissed(String missed) {
		if(!this.missed.getStringList("missed").contains(missed)) {
			List<String> list = this.missed.getStringList("missed");
			list.add(missed);
			this.missed.set("missed", list);
		}
	}
	
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}
}


