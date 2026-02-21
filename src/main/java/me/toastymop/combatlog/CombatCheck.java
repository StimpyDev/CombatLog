package me.toastymop.combatlog;

import me.toastymop.combatlog.util.IEntityDataSaver;
import me.toastymop.combatlog.util.TagData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public static void CheckCombat(Entity victim, Entity attacker) {
    if (!(victim instanceof LivingEntity target)) return;

    // Check if the ATTACKER is a player
    if (attacker instanceof Player playerAttacker) {
        if (((ServerPlayer) playerAttacker).gameMode.getGameModeForPlayer().isSurvival()) {
            
            // If they hit another player
            if (target instanceof Player playerTarget) {
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

    if (target instanceof Player playerTarget) {
        if (((ServerPlayer) playerTarget).gameMode.getGameModeForPlayer().isSurvival()) {
            if (CombatConfig.Config.allDamage) {
                setCombat(playerTarget);
            } else if (CombatConfig.Config.mobDamage && attacker instanceof LivingEntity) {
                setCombat(playerTarget);
            }
        }
    }
}
    public static void setCombat(Player target, Player attacker ) {
        //? if >=1.21.1 {
        tickRate = (int) target.level().getServer().tickRateManager().tickrate();
        //?} else {
        /*tickRate = 20;
        *///?}
        TagData.setTagTime((IEntityDataSaver) target);
        TagData.setTagTime((IEntityDataSaver) attacker);
        if (!CombatConfig.Config.disabledItems.isEmpty()){
            setCooldowns(CombatConfig.Config.disabledItems, target, attacker);
        }
    }
    public static void setCombat(Player target) {
        //? if >=1.21.1 {
        tickRate = (int) target.level().getServer().tickRateManager().tickrate();
         //?} else {
        /*tickRate = 20;
        *///?}
        TagData.setTagTime((IEntityDataSaver) target);
        if (!CombatConfig.Config.disabledItems.isEmpty()){
            setCooldowns(CombatConfig.Config.disabledItems, target);
        }
    }

    public static void setCooldowns(List<ItemStack> list, Player target, Player attacker){
        for (ItemStack stack : list) {
            //? if >=1.21.6 {
            target.getCooldowns().addCooldown(stack, CombatConfig.Config.combatTime * 20);
            attacker.getCooldowns().addCooldown(stack, CombatConfig.Config.combatTime * 20);
            //?} else {
            /*target.getCooldowns().addCooldown(stack.getItem(), CombatConfig.Config.combatTime * 20);
            attacker.getCooldowns().addCooldown(stack.getItem(), CombatConfig.Config.combatTime * 20);
            *///?}

        }
    }

    public static void setCooldowns(List<ItemStack> list, Player target){
        for (ItemStack stack : list) {
            //? if >=1.21.6 {
            target.getCooldowns().addCooldown(stack, CombatConfig.Config.combatTime * 20);
            //?} else {
            /*target.getCooldowns().addCooldown(stack.getItem(), CombatConfig.Config.combatTime * 20);
            *///?}
        }
    }
}
