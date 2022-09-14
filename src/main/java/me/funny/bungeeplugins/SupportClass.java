package me.funny.bungeeplugins;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SupportClass {
    private final Bungeeplugins plugin;
    private final Map<ProxiedPlayer, Integer> positionInQueue;
    private final Queue<ProxiedPlayer> players;

    public SupportClass(Bungeeplugins plugin) {
        this.plugin = plugin;
        positionInQueue = new HashMap<>();
        players = new ArrayDeque<>();
    }

    public void addToTheQueue(ProxiedPlayer p) {
        players.add(p);
        positionInQueue.put(p, players.size());
    }

    public void removeFromTheQueue() {
        ProxiedPlayer p = players.poll();
        if (p != null) {
            p.setPermission("bungeecord.server.survival_over", true);
            p.setPermission("bungeecord.server.survival_nether", true);

            if (plugin.getProxy().getServerInfo("survival_over").canAccess(p)) {
                p.connect(plugin.getProxy().getServers().get("survival_over"));

            } else
                plugin.getLogger().info("Aint workin bro");
            plugin.getProxy().getServers().get("lobby").getPlayers().forEach(player -> {
                player.sendMessage(new TextComponent("§aYour position in the queue is: [§e" + positionInQueue.get(p) + "§a]"));
                positionInQueue.put(player, positionInQueue.get(player) - 1);
            });
        }

        plugin.getProxy().getLogger().info("\n" + "The queue size is: " + players.size() + "\n" + "removing player in next 5 minutes");

        //after a minute run this method again
        plugin.getProxy().getScheduler().schedule(plugin, this::removeFromTheQueue, 1, TimeUnit.MINUTES);

    }

    public void onDisconnectQueue(ProxiedPlayer p) {
        players.remove(p);
        positionInQueue.remove(p);
    }
}
