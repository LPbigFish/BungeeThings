package me.funny.bungeeplugins;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public final class Bungeeplugins extends Plugin implements Listener {

    private final SupportClass supportClass = new SupportClass(this);
    @Override
    public void onEnable() {

        getProxy().getPluginManager().registerListener(this, this);


        getLogger().info("Queue system has been initialized!");
        supportClass.removeFromTheQueue();
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        if (e.getPlayer().getServer().getInfo().getName().equals("survival_over") || e.getPlayer().getServer().getInfo().getName().equals("survival_nether")) {
            e.getPlayer().setPermission("bungeecord.server.survival_over", false);
            e.getPlayer().setPermission("bungeecord.server.survival_nether", false);
        } else {
            supportClass.onDisconnectQueue(e.getPlayer());
        }
    }
    //player connect to lobby
    @EventHandler
    public void onPlayerJoinQueue(PostLoginEvent e) {
            e.getPlayer().setPermission("bungeecord.server.survival_over", false);
            e.getPlayer().setPermission("bungeecord.server.survival_nether", false);
            e.getPlayer().sendMessage(new TextComponent("§aYou have been added to the queue!"));
            supportClass.addToTheQueue(e.getPlayer());
    }
}
