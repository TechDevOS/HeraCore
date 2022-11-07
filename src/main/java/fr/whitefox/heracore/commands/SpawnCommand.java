package fr.whitefox.heracore.commands;

import fr.whitefox.heracore.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("spawn")) {

            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            String coordinates = main.getConfig().getString("config.spawn");
            String[] parts = coordinates.split(",");
            String x = parts[0];
            String y = parts[1];
            String z = parts[2];

            Location spawn = new Location(player.getWorld(), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z), 0f, 0f);
            player.teleport(spawn);

            player.sendMessage("§6[§9Hera§6] §aVous venez d'être téléporté au spawn.");

            return true;
        }

        if (cmd.getName().equalsIgnoreCase("setspawn")) {

            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            Location loc = player.getLocation();
            String x = String.valueOf(loc.getX());
            String y = String.valueOf(loc.getY());
            String z = String.valueOf(loc.getZ());
            String coordinates = x + "," + y + "," + z;

            main.getConfig().set("config.spawn", coordinates);

            player.sendMessage("§6[§9Hera§6] §aPoint de spawn correctement enregistré.");
        }

        return true;
    }
}
