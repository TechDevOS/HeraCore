package fr.whitefox.heracore.commands;

import fr.whitefox.heracore.Main;
import fr.whitefox.heracore.db.PlayerInfos;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class HistoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cVous devez spécifier un joueur : /history <player>");
            return false;
        }

        if (!PlayerInfos.exist(String.valueOf(args[0]))) {
            sender.sendMessage("§cCe joueur ne s'est jamais connecté au serveur !");
            return false;
        }

        UUID targetUUID = PlayerInfos.getUUID(args[0]);

        if (InfCommand.getBansCount(targetUUID) == 0 && InfCommand.getMutesCount(targetUUID) == 0 && InfCommand.getWarnsCount(targetUUID) == 0) {
            sender.sendMessage("§aCe joueur n'a pas d'infractions.");
            return false;
        }

        sender.sendMessage("\n§7" + ChatColor.STRIKETHROUGH + "----------------" + "§9§lPLAYER HISTORY§7" + ChatColor.STRIKETHROUGH + "----------------");
        sender.sendMessage("\n\n§cPseudo : §6§l" + args[0] + "\n");
        sender.sendMessage(getHistory(targetUUID.toString()));
        sender.sendMessage("\n§7" + ChatColor.STRIKETHROUGH + "---------------------------------------------------");
        sender.sendMessage(" ");


        return true;
    }

    public String getHistory(String targetUUID) {
        StringBuilder result = new StringBuilder();

        try {
            PreparedStatement sts = Main.getInstance().sqlite.getConnection().prepareStatement("SELECT * FROM mod_history WHERE player_uuid=?");
            sts.setString(1, targetUUID);
            ResultSet rs = sts.executeQuery();

            while (rs.next()) {
                String duration = BanCommand.getSanctionTime(rs.getLong("duration"));
                Timestamp ts = new Timestamp(rs.getLong("time"));
                String moderator = getModeratorName(rs.getString("moderator"));
                String type = rs.getString("type");

                result.append("\n§r - §c").append(type);
                if (rs.getLong("duration") != -1) {
                    result.append("§r d'une durée de §e").append(duration);
                }
                result.append("§r par §6§l").append(moderator).append("§r le §d").append(ts);
                if (rs.getLong("duration") != -1) {
                    result.append("§r pour la raison suivante : §a").append(rs.getString("reason"));
                }
                result.append("§r.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String getModeratorName(String moderatorUUID) {
        if (moderatorUUID.equalsIgnoreCase("CONSOLE"))
            return "CONSOLE";

        return PlayerInfos.getName(moderatorUUID);
    }
}
