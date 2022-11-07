package fr.whitefox.heracore.events;

import fr.whitefox.heracore.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onFreeze(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (Main.getInstance().freeze_list.contains(player)) {
            event.setCancelled(true);
        }
    }
}