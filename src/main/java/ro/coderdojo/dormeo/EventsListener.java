package ro.coderdojo.dormeo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public final class EventsListener implements Listener {

	Main plugin;

	Scoreboard scoreboard;

	int redKills = 0;
	int blueKills = 0;

	public EventsListener(Main plugin) {
		this.plugin = plugin;

		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		scoreboard.registerNewTeam("blueTeam");
		scoreboard.getTeam("blueTeam").setPrefix(ChatColor.BLUE + "[ALBASTRU] " + ChatColor.WHITE + "[");
		scoreboard.getTeam("blueTeam").setSuffix("]");
		scoreboard.getTeam("blueTeam").setDisplayName("Echipa Albastră");
		scoreboard.getTeam("blueTeam").setCanSeeFriendlyInvisibles(true);
		scoreboard.getTeam("blueTeam").setAllowFriendlyFire(false);

		scoreboard.registerNewTeam("redTeam");
		scoreboard.getTeam("redTeam").setPrefix(ChatColor.RED + "[ROȘU] " + ChatColor.WHITE + "[");
		scoreboard.getTeam("redTeam").setSuffix("]");
		scoreboard.getTeam("redTeam").setDisplayName("Echipa Roșie");
		scoreboard.getTeam("redTeam").setCanSeeFriendlyInvisibles(true);
		scoreboard.getTeam("redTeam").setAllowFriendlyFire(false);

		Objective playersObjective = scoreboard.registerNewObjective("players", "dummy");
		playersObjective.setDisplayName("");
		playersObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		Player killer = event.getEntity().getKiller();

		if (killer == null) {
			return;
		}
		if (scoreboard.getTeam("redTeam").getEntries().contains(killer.getName())) {
			redKills++;
		}
		if (scoreboard.getTeam("blueTeam").getEntries().contains(killer.getName())) {
			blueKills++;
		}

		scoreboard.getTeam("redTeam").removeEntry(event.getEntity().getName());
		scoreboard.getTeam("blueTeam").addEntry(event.getEntity().getName());

		refreshScoreBoard(event.getEntity());
	}

	public void refreshScoreBoard(Player player) {
		if (player != null) {
			if (player.getScoreboard().getObjective("players") == null) {
				player.setScoreboard(scoreboard);
			}
		}

		Objective objective = scoreboard.getObjective("players");

		if (objective != null) {
			objective.unregister();
		}
		
		Objective playersObjective = scoreboard.registerNewObjective("players", "dummy");
		playersObjective.setDisplayName("");
		playersObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

		scoreboard.getObjective("players").getScore(ChatColor.DARK_GRAY + " Killuri").setScore(-1);

		scoreboard.getObjective("players").getScore(ChatColor.RED + "Killuri Roșii: " + ChatColor.WHITE + redKills).setScore(-2);
		scoreboard.getObjective("players").getScore(ChatColor.BLUE + "Killuri Albaștrii: " + ChatColor.WHITE + blueKills).setScore(-3);
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("Salut " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + "! Felicitări pentru primul mod de Minecraft!");

		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
		player.getInventory().addItem(new ItemStack(Material.BOAT, 1));
		AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(10.00);

		player.teleport(new Location(plugin.world, 924.562, 37.00000, 941.547));
		refreshScoreBoard(player);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerThrow(PlayerInteractEvent event) {

		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (event.getItem() != null && event.getItem().getType() == Material.BLAZE_ROD) {
				Player p = event.getPlayer();
				Projectile fireball;
				int fb_speed = 1;
				final Vector fb_direction = p.getEyeLocation().getDirection().multiply(fb_speed);
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 10, 1);
				fireball = p.getWorld().spawn(p.getEyeLocation().add(fb_direction.getX(), fb_direction.getY(), fb_direction.getZ()), Fireball.class);
				fireball.setShooter((ProjectileSource) p);
				fireball.setVelocity(fb_direction);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
		event.getEntity().remove();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (event.getClickedBlock() == null) {
			return;
		}

		Material material = event.getClickedBlock().getState().getType();
		Location location = event.getClickedBlock().getState().getLocation();
		if (action == Action.RIGHT_CLICK_BLOCK && material == Material.STONE_BUTTON) {
			System.out.println("Click: " + location);

			Location button_blue = new Location(plugin.world, 926.0, 38.0, 943.0, location.getYaw(), location.getPitch());
			Location button_red = new Location(plugin.world, 922.0, 38.0, 943.0, location.getYaw(), location.getPitch());
			if (location.equals(button_red)) {
				player.teleport(new Location(plugin.world, 958.451, 65, 945.386));
				scoreboard.getTeam("blueTeam").removeEntry(event.getPlayer().getName());
				scoreboard.getTeam("redTeam").addEntry(event.getPlayer().getName());
			}
			if (location.equals(button_blue)) {
				player.teleport(new Location(plugin.world, 886.559, 65, 943.310));
				scoreboard.getTeam("redTeam").removeEntry(event.getPlayer().getName());
				scoreboard.getTeam("blueTeam").addEntry(event.getPlayer().getName());
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(new Location(plugin.world, 924.562, 37.00000, 941.547));
		
		event.getPlayer().getInventory().clear();
		event.getPlayer().getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
		event.getPlayer().getInventory().addItem(new ItemStack(Material.BOAT, 1));
		AttributeInstance healthAttribute = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(10.00);
	}

}
