package me.funny.bungeeplugins;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class SupportClass {
    private final Bungeeplugins plugin;
    private Map<ProxiedPlayer, Integer> positionInQueue;
    private Queue<ProxiedPlayer> players;

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
            p.connect(plugin.getProxy().getServerInfo("survival_over"));
            p.setPermission("bungeecord.server.survival_over", true);
            p.setPermission("bungeecord.server.survival_nether", true);
            plugin.getProxy().getServers().get("lobby").getPlayers().forEach(player -> {
                player.sendMessage(UUID.fromString("Queue"), new TextComponent("§aYour position in the queue is: [§e" + positionInQueue.get(p) + "§a]"));
                positionInQueue.put(player, positionInQueue.get(player) - 1);
            });
        }

        //each 5 minutes remove player from queue
        plugin.getProxy().getScheduler().schedule(plugin, this::removeFromTheQueue, 5, 5, java.util.concurrent.TimeUnit.MINUTES);
    }

    public void onDisconnectQueue(ProxiedPlayer p) {
        players.remove(p);
        positionInQueue.remove(p);
    }
}
