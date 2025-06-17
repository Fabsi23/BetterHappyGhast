package me.fabsi.betterhappyghast.listeners;

import lombok.AllArgsConstructor;
import me.fabsi.betterhappyghast.config.DefaultConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HappyGhast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@AllArgsConstructor
public class BuildListener implements Listener {

    private DefaultConfig config;

    @EventHandler
    private void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!config.getBuildOnHappyGhastEnabled()) return;
        if (!player.isSneaking()) return;

        Entity ent = event.getRightClicked();
        if(!(ent instanceof HappyGhast)) return;

        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.CREATIVE) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Material mat = item.getType();
        EquipmentSlot slot = EquipmentSlot.HAND;
        if (mat.isAir() || !mat.isBlock() || !mat.isSolid()) { // mainhand can't be placed, check offhand
            item = player.getInventory().getItemInOffHand();
            mat = item.getType();
            if (mat.isAir() || !mat.isBlock() || !mat.isSolid()) return;
            slot = EquipmentSlot.OFF_HAND;
        }

        Location entLoc = ent.getLocation();
        Location placeLoc = entLoc.clone().add(event.getClickedPosition());

        if (placeLoc.getBlockY() - entLoc.getBlockY() < ent.getHeight()) return;
        if (entLoc.getY() != entLoc.getBlockY()) { // if ghast is perfectly aligned we don't need to add a block
            placeLoc.add(0, 1, 0);
        }

        Vector playerEyes = player.getEyeLocation().toVector();
        Vector relative = playerEyes.subtract(placeLoc.toVector());
        BlockFace face = getDominantFace(relative);

        Block blockToSet = placeLoc.getBlock();
        if (!blockToSet.getType().isAir()) return;

        placeBlockWithBlockPlaceEvent(player, blockToSet, face, item, slot);
        event.setCancelled(true);
    }

    private void placeBlockWithBlockPlaceEvent(
            Player player,
            Block block,
            BlockFace face,
            ItemStack placedItem,
            EquipmentSlot slot) {
        BlockState replacedState = block.getState();
        BlockPlaceEvent placeEvent = new BlockPlaceEvent(
                block,
                replacedState,
                block.getRelative(face),
                placedItem,
                player,
                true,
                slot
        );

        // Call the event so other plugins can react to it
        Bukkit.getPluginManager().callEvent(placeEvent);
        if (placeEvent.isCancelled()) return;

        if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
            placedItem.setAmount(placedItem.getAmount() - 1);
        }

        BlockData data = Bukkit.createBlockData(placedItem.getType());
        if (data instanceof Directional directional) {
            face = face.getOppositeFace();
            if (face != BlockFace.UP && face != BlockFace.DOWN) { // breaks for stairs
                directional.setFacing(face);
            }
        }
        block.setBlockData(data, true);
    }

    private BlockFace getDominantFace(Vector vec) {
        BlockFace bestFace = BlockFace.DOWN;
        double bestDot = -1.0;

        for (BlockFace face : BlockFace.values()) {
            if (!face.isCartesian()) continue;
            Vector dir = face.getDirection();
            double dot = vec.clone().normalize().dot(dir);
            if (dot > bestDot) {
                bestDot = dot;
                bestFace = face;
            }
        }

        return bestFace;
    }
}
