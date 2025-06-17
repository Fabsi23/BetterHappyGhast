package me.fabsi.betterhappyghast.listeners;

import lombok.AllArgsConstructor;
import me.fabsi.betterhappyghast.config.DefaultConfig;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.HappyGhast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@AllArgsConstructor
public class SpawnListener implements Listener {

    private final DefaultConfig config;

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof HappyGhast happyGhast)) return;

        double speed = config.getHappyGhastIdleSpeed();
        if (speed < 0) speed = 0;
        if (speed > 1) speed = 1;
        happyGhast.getAttribute(Attribute.FLYING_SPEED).setBaseValue(speed);
    }

}
