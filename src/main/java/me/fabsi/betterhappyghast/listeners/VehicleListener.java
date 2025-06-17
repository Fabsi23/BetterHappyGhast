package me.fabsi.betterhappyghast.listeners;

import lombok.AllArgsConstructor;
import me.fabsi.betterhappyghast.config.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.HappyGhast;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

@AllArgsConstructor
public class VehicleListener implements Listener {

    private final Plugin plugin;
    private final DefaultConfig config;

    @EventHandler
    private void onVehicleEnter(VehicleEnterEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (!(vehicle instanceof HappyGhast happyGhast)) return;
        Bukkit.getScheduler().runTask(plugin, () -> setNewSpeed(happyGhast));
    }

    @EventHandler
    private void onVehicleExit(VehicleExitEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (!(vehicle instanceof HappyGhast happyGhast)) return;
        Bukkit.getScheduler().runTask(plugin, () -> setNewSpeed(happyGhast));
    }

    private void setNewSpeed(HappyGhast happyGhast) {
        int passengers = happyGhast.getPassengers().size();

        double speed = config.getHappyGhastIdleSpeed();
        if (passengers > 0) {
            speed = config.getHappyGhastMountedSpeed(passengers);
        }
        if (speed < 0) speed = 0;
        if (speed > 1) speed = 1;
        happyGhast.getAttribute(Attribute.FLYING_SPEED).setBaseValue(speed);
    }

}
