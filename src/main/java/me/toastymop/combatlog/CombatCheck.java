package me.toastymop.combatlog;

import me.toastymop.combatlog.util.IEntityDataSaver;
import me.toastymop.combatlog.util.TagData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.List;

public class CombatCheck {
    public static int tickRate = 20;

    public static void CheckCombat(Entity victim, Entity attacker) {
        
        if (!(victim instanceof Player) || !(attacker instanceof Player)) return;
        if (victim == attacker) return;

        Player pVictim = (Player) victim;
        Player pAttacker = (Player) attacker;
        
        if (isSurvival(pVictim) && isSurvival(pAttacker)) {
            setCombat(pVictim, pAttacker);
        }
    }

    private static boolean isSurvival(Player player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer) player;
            return sp.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
        }
        return false;
    }

    public static void setCombat(Player... players) {
        if (players.length == 0) return;
        
        updateTickRate(players[0]);
        int duration = CombatConfig.Config.combatTime * tickRate;
        List<ItemStack> disabledItems = CombatConfig.Config.disabledItems;

        for (Player player : players) {
            TagData.setTagTime((IEntityDataSaver) player);
            
            if (disabledItems != null && !disabledItems.isEmpty()) {
                for (ItemStack stack : disabledItems) {
                    if (stack == null || stack.isEmpty()) continue;

                    // Stonecutter preprocessor voor verschillende MC versies
                    //? if >=1.21.6 {
                    player.getCooldowns().addCooldown(stack, duration);
                    //?} else {
                    /* player.getCooldowns().addCooldown(stack.getItem(), duration); */
                    //?}
                }
            }
        }
    }

    private static void updateTickRate(Player player) {
        //? if >=1.21.1 {
        if (player.level().getServer() != null) {
            tickRate = (int) player.level().getServer().tickRateManager().tickrate();
        }
        //?} else {
        /* tickRate = 20; */
        //?}
    }
}
