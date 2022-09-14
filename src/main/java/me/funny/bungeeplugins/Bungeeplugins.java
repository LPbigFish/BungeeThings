package me.funny.bungeeplugins;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public final class Bungeeplugins extends Plugin implements Listener {

    private final SupportClass supportClass = new SupportClass(this);
    @Override
    public void onEnable() {
        getProxy().getServers().get("lobby").getPlayers().forEach(player -> {
            player.connect(getProxy().getServerInfo("survival_over"));

            //give player permission to connect to survival_over and survival_nether
            player.setPermission("bungeecord.server.survival_over", true);
            player.setPermission("bungeecord.server.survival_nether", true);
            supportClass.removeFromTheQueue();
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        supportClass.addToTheQueue(e.getPlayer());
    }
}
