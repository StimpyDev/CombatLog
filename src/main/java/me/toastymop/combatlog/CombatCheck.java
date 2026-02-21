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
        if (!(victim instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) victim;

        // 1. Logic for the ATTACKER
        if (attacker instanceof Player) {
            Player playerAttacker = (Player) attacker;
            if (((ServerPlayer) playerAttacker).gameMode.getGameModeForPlayer().isSurvival()) {
                
                // If they hit another player
                if (target instanceof Player) {
                    Player playerTarget = (Player) target;
                    if (((ServerPlayer) playerTarget).gameMode.getGameModeForPlayer().isSurvival()) {
                        setCombat(playerTarget, playerAttacker);
                    }
                } 
                // If they hit a mob
                else if (CombatConfig.Config.mobDamage) {
                    setCombat(playerAttacker);
                }
            }
        }

        if (target instanceof Player && !(attacker instanceof Player)) {
            Player playerTarget = (Player) target;
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
