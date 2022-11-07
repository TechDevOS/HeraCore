package fr.whitefox.heracore.commands;

import fr.whitefox.heracore.Main;
import fr.whitefox.heracore.db.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class InfCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("inf")) {

            if (args.length < 1) {
                sender.sendMessage(helpMessage());
                return false;
            }

            String targetName = args[0];

            if (!PlayerInfos.exist(targetName)) {
                sender.sendMessage("§cCe joueur ne s'est jamais connecté au serveur !");
                return false;
            }

            UUID targetUUID = PlayerInfos.getUUID(targetName);
            String isBanned = isBanned(targetUUID);
            String isMuted = isMuted(targetUUID);
            String lastConnection = "§6§l" + getLastConnexion(targetUUID);

            sender.sendMessage("\n§7" + ChatColor.STRIKETHROUGH + "----------------" + "§9§lPLAYER INFORMATIONS§7" + ChatColor.STRIKETHROUGH + "----------------");
            sender.sendMessage("\n\n§cPseudo : §6§l" + targetName);
            sender.sendMessage("§cDernière connexion : " + lastConnection);
            if (sender.hasPermission("hera.dupeip")) {
                sender.sendMessage("§cDernière IP connue : §6§l" + Main.getInstance().playerInfos.getIPAddress(targetUUID));
            }
            sender.sendMessage("\n§cMuet : " + isMuted);
            sender.sendMessage("§cBanni : " + isBanned);
            sender.sendMessage("§6Ban(s) : §l§e" + getBans(targetUUID));
            sender.sendMessage("§6Mute(s) : §l§e" + getMutes(targetUUID));
            sender.sendMessage("§6Warn(s) : §l§e" + getWarns(targetUUID));


            sender.sendMessage("\n§7" + ChatColor.STRIKETHROUGH + "---------------------------------------------------");
            sender.sendMessage(" ");
        }

        return true;
    }

    public int getBans(UUID playerUUID) {
        try {
            PreparedStatement sts = Main.getInstance().sqlite.getConnection().prepareStatement("SELECT COUNT(*) AS total FROM mod_history WHERE (player_uuid=? AND type=?)");
            sts.setString(1, playerUUID.toString());
            sts.setString(2, "ban");
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Le joueur n'a pas d'informations dans la table");
    }

    public int getMutes(UUID playerUUID) {
        try {
            PreparedStatement sts = Main.getInstance().sqlite.getConnection().prepareStatement("SELECT COUNT(*) AS total FROM mod_history WHERE (player_uuid=? AND type=?)");
            sts.setString(1, playerUUID.toString());
            sts.setString(2, "mute");
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Le joueur n'a pas d'informations dans la table");
    }

    public int getWarns(UUID playerUUID) {
        try {
            PreparedStatement sts = Main.getInstance().sqlite.getConnection().prepareStatement("SELECT COUNT(*) AS total FROM mod_history WHERE (player_uuid=? AND type=?)");
            sts.setString(1, playerUUID.toString());
            sts.setString(2, "warn");
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Le joueur n'a pas d'informations dans la table");
    }

    public String helpMessage() {
        return "§cSyntaxe : /inf <joueur>";
    }

    private String isBanned(UUID playerUUID) {
        return Main.getInstance().banManager.isBanned(playerUUID) ? "§4§lOui" : "§a§lNon";
    }

    private String isMuted(UUID playerUUID) {
        return Main.getInstance().muteManager.isMuted(playerUUID) ? "§4§lOui" : "§a§lNon";
    }

    private String getLastConnexion(UUID playerUUID) {
        if (Bukkit.getPlayer(playerUUID) != null) {
            return "§a§lJoueur actuellement connecté";
        } else {
            Date lastConnection = new Date(Main.getInstance().playerInfos.getLastConnexion(playerUUID));
            return lastConnection.toString();
        }
    }
}
