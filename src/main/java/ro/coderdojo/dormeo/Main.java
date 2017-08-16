package ro.coderdojo.dormeo;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	World world;
	
    @Override
    public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventsListener(this), this);
		world = this.getServer().createWorld(new WorldCreator("wow_world"));
		world.setGameRuleValue("doMobSpawning", "false");
		world.setDifficulty(Difficulty.HARD);
		
    }

}
