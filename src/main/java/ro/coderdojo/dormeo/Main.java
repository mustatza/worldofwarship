package ro.coderdojo.dormeo;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

	World world;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventsListener(this), this);
		world = this.getServer().createWorld(new WorldCreator("wow_world"));
		world.setGameRuleValue("doMobSpawning", "false");
		world.setDifficulty(Difficulty.HARD);

		new BukkitRunnable() {
			public void run() {
				world.setTime(8000L);
				world.setStorm(false);
				world.setThundering(false);;
			}
		}.runTaskTimer(this, 20, 20);

	}

}
