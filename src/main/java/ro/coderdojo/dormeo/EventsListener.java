package ro.coderdojo.dormeo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public final class EventsListener implements Listener {

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("Salut " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + "! Felicitări pentru primul mod de Minecraft!");
	}

	@EventHandler
	public void onPlayerThrow(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (event.getItem() != null){
				if (event.getItem().getType() == (Material.FIREBALL)) {
					Player p = event.getPlayer();
					Projectile fireball;
					int fb_speed = 1;
					int fb_amount = event.getItem().getAmount();
					final Vector fb_direction = p.getEyeLocation().getDirection().multiply(fb_speed);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 10, 1);
					fireball = p.getWorld().spawn(p.getEyeLocation().add(fb_direction.getX(), fb_direction.getY(), fb_direction.getZ()), Fireball.class);
					fireball.setShooter((ProjectileSource) p);
					fireball.setVelocity(fb_direction);
					if (fb_amount > 1) {
						event.getItem().setAmount(fb_amount - 1);
					} else {
						p.getInventory().clear(p.getInventory().getHeldItemSlot());
					}
				}
			}

		}
	}
}
