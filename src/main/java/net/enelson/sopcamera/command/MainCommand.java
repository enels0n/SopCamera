package net.enelson.sopcamera.command;

import net.enelson.sopcamera.utils.Utils;
import net.enelson.sopli.lib.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {

    private static final TextUtils TEXT_UTILS = new TextUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(TEXT_UTILS.color("&e/sopcamera give <player>"));
            return false;
        }

        if (args[0].equals("give")) {
            if (args.length != 2) {
                sender.sendMessage(TEXT_UTILS.color("&e/sopcamera give <player>"));
                return false;
            }

            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(TEXT_UTILS.color("&cPlayer not found."));
                return false;
            }

            ItemStack item = Utils.createCamera();
            if (player.getInventory().addItem(item).size() != 0) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
            return false;
        }
        return false;
    }
}
