package me.toastymop.combatlog;

import me.toastymop.combatlog.util.IEntityDataSaver;
import me.toastymop.combatlog.util.TagData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class CombatCheck {
    public static int tickRate = 20;

    public static void CheckCombat(Entity victim, Entity attacker) {
        if (!(victim instanceof LivingEntity target)) return;

        if (attacker instanceof Player playerAttacker) {
            if (((ServerPlayer) playerAttacker).gameMode.getGameModeForPlayer().isSurvival()) {
                if (target instanceof Player playerTarget) {
                    if (((ServerPlayer) playerTarget).gameMode.getGameModeForPlayer().isSurvival()) {
                        setCombat(playerTarget, playerAttacker);
                    }
                } else if (CombatConfig.Config.mobDamage) {
                    setCombat(playerAttacker);
                }
            }
        }

        if (target instanceof Player playerTarget && !(attacker instanceof Player)) {
            if (((ServerPlayer) playerTarget).gameMode.getGameModeForPlayer().isSurvival()) {
                if (CombatConfig.Config.allDamage || (CombatConfig.Config.mobDamage && attacker instanceof LivingEntity)) {
                    setCombat(playerTarget);
                }
            }
        }
    }

    public static void setCombat(Player target, Player attacker) {
        updateTickRate(target);
        
        TagData.setTagTime((IEntityDataSaver) target);
        TagData.setTagTime((IEntityDataSaver) attacker);
        
        if (!CombatConfig.Config.disabledItems.isEmpty()){
            setCooldowns(CombatConfig.Config.disabledItems, target, attacker);
        }
    }

    public static void setCombat(Player target) {
        updateTickRate(target);
        
        TagData.setTagTime((IEntityDataSaver) target);
        
        if (!CombatConfig.Config.disabledItems.isEmpty()){
            setCooldowns(CombatConfig.Config.disabledItems, target);
        }
    }

    private static void updateTickRate(Player player) {
        //? if >=1.21.1 {
        tickRate = (int) player.level().getServer().tickRateManager().tickrate();
        //?} else {
        /* tickRate = 20; */
        //?}
    }

    public static void setCooldowns(List<ItemStack> list, Player... players) {
        // Calculate duration once using the optimized tickRate
        int duration = CombatConfig.Config.combatTime * tickRate;
        
        for (Player player : players) {
            for (ItemStack stack : list) {
                //? if >=1.21.6 {
                player.getCooldowns().addCooldown(stack, duration);
                //?} else {
                /* player.getCooldowns().addCooldown(stack.getItem(), duration); */
                //?}
            }
        }
    }
}
