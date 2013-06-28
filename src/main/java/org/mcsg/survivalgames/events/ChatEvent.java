package org.mcsg.survivalgames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mcsg.survivalgames.util.NameUtil;

public class ChatEvent implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        
        event.setFormat(event.getFormat().replace(event.getPlayer().getName(), NameUtil.stylize(event.getPlayer().getName(), true, true)));
    }
}