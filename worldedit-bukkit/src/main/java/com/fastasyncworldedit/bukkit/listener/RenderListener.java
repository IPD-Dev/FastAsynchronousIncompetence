package com.fastasyncworldedit.bukkit.listener;

import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.configuration.Settings;
import com.fastasyncworldedit.core.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RenderListener implements Listener {

    private final Map<UUID, int[]> views = new ConcurrentHashMap<>();
    private Iterator<Map.Entry<UUID, int[]>> entrySet;
    private int OFFSET = 6;

    public RenderListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        TaskManager.taskManager().repeat(new Runnable() {
            private long last = 0;

            @Override
            public void run() {
                if (views.isEmpty()) {
                    return;
                }

                long now = System.currentTimeMillis();
                int tps32 = (int) (Math.round(Fawe.instance().getTimer().getTPS()) * 32);
                long diff = now - last;
                last = now;
                if (diff > 75) {
                    OFFSET = diff > 100 ? 0 : 4;
                    return;
                }
                int timeOut;
                if (diff < 55 && tps32 > 608) {
                    OFFSET = 8;
                    timeOut = 2;
                } else {
                    OFFSET = 1 + (tps32 / 102400);
                    timeOut = 162 - (tps32 / 2560);
                }
                if (entrySet == null || !entrySet.hasNext()) {
                    entrySet = views.entrySet().iterator();
                }
                int nowTick = (int) (Fawe.instance().getTimer().getTick());
                while (entrySet.hasNext()) {
                    Map.Entry<UUID, int[]> entry = entrySet.next();
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player != null) {
                        int[] value = entry.getValue();
                        if (nowTick - value[1] >= timeOut) {
                            value[1] = nowTick + 1;
                            setViewDistance(player, Math.max(4, value[0] + 1));
                            long spent = System.currentTimeMillis() - now;
                            if (spent > 5) {
                                if (spent > 10) {
                                    value[1] = nowTick + 20;
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }, 1);
    }

    private void setViewDistance(Player player, int value) {
        UUID uuid = player.getUniqueId();
        if (value == Settings.settings().EXPERIMENTAL.DYNAMIC_CHUNK_RENDERING) {
            views.remove(uuid);
        } else {
            int[] val = views.get(uuid);
            if (val == null) {
                val = new int[]{value, (int) Fawe.instance().getTimer().getTick()};
                UUID uid = player.getUniqueId();
                views.put(uid, val);
            } else {
                if (value <= val[0]) {
                    val[1] = (int) Fawe.instance().getTimer().getTick();
                }
                if (val[0] == value) {
                    return;
                } else {
                    val[0] = value;
                }
            }
        }
        player.setViewDistance(value);
    }

    private int getViewDistance(Player player) {
        int[] value = views.get(player.getUniqueId());
        return value == null ? Settings.settings().EXPERIMENTAL.DYNAMIC_CHUNK_RENDERING : value[0];
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        setViewDistance(event.getPlayer(), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() >> OFFSET != to.getBlockX() >> OFFSET || from.getBlockZ() >> OFFSET != to.getBlockZ() >> OFFSET) {
            Player player = event.getPlayer();
            int currentView = getViewDistance(player);
            setViewDistance(player, Math.max(currentView - 1, 1));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setViewDistance(player, 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uid = player.getUniqueId();
        views.remove(uid);
    }

}
