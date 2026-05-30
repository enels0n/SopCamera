package net.enelson.sopcamera.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.enelson.sopcamera.SopCamera;
import net.enelson.sopcamera.utils.Utils;

import java.util.List;
import java.util.Objects;

public class CameraClick implements Listener {

	@EventHandler
	public void cameraClicked(final PlayerInteractEvent e) {
		final Action action = e.getAction();
		if (action != Action.RIGHT_CLICK_AIR) {
			if (action != Action.RIGHT_CLICK_BLOCK
					|| Objects.requireNonNull(e.getClickedBlock()).getType().isInteractable()) {
				return;
			}
		}

		final ItemStack camera = e.getItem();
		if (camera == null || camera.getType() != Material.SHEARS) {
			return;
		}

		int durability = Utils.getDurability(camera);

		if (durability == 0)
			return;

		e.setCancelled(true);

		Player player = e.getPlayer();
		ItemStack paper = null;

		for (ItemStack p : player.getInventory()) {
			if (p == null || !p.getType().equals(Material.PAPER))
				continue;

			if (p.getItemMeta().hasCustomModelData())
				continue;

			if (Utils.hasNBT(p))
				continue;

			paper = p;
			break;
		}

		if (paper == null || paper.getAmount() <= 0)
			return;

		paper.setAmount(paper.getAmount() - 1);
		player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 0.8F, 2.0F);
		ItemStack item = Utils.setParams(e.getItem(), durability - 1);
		
		List<String> commands = SopCamera.getInstance().getConfig().getStringList("photoCommands");
		if(commands != null) {
			for(String cmd : commands) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", e.getPlayer().getDisplayName()));
			}
		}
		
		Bukkit.getScheduler().runTaskLater(SopCamera.getInstance(), new Runnable() {
			@Override
			public void run() {
				ItemStack photo = Utils.takePicture(player);
				if (player.getInventory().addItem(photo).size() != 0) {
					player.getWorld().dropItem(player.getLocation(), photo);
				}
				player.getEquipment().setItem(e.getHand(), item);
			}
		}, 1);
	}
}


