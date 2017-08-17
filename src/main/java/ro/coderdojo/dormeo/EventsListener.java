package ro.coderdojo.dormeo;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public final class EventsListener implements Listener {

	Main plugin;

	public EventsListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("Salut " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + "! Felicitări pentru primul mod de Minecraft!");

		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
		AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(10.00);

		player.teleport(new Location(plugin.world, 924.562, 37.00000, 941.547));
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
			
			Location button1 = new Location(plugin.world, 926.0, 38.0, 943.0, location.getYaw(), location.getPitch());
			Location button2 = new Location(plugin.world, 922.0, 38.0, 943.0, location.getYaw(), location.getPitch());
			if (location.equals(button2)) {
				player.teleport(new Location(plugin.world, 958.451, 65, 945.386));
			}
			if (location.equals(button1)) {
				player.teleport(new Location(plugin.world, 886.559, 65, 943.310));
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(new Location(plugin.world, 924.562, 37.00000, 941.547));
	}

}
