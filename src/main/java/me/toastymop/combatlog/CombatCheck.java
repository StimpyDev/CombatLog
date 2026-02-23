package me.toastymop.combatlog;

import me.toastymop.combatlog.util.IEntityDataSaver;
import me.toastymop.combatlog.util.TagData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.List;

public class CombatCheck {
    public static int tickRate = 20;

    public static void CheckCombat(Entity victim, Entity attacker) {
        if (!(victim instanceof LivingEntity)) return;
        if (victim == attacker) return;

        if (attacker instanceof Player pAttacker && victim instanceof Player pVictim) {
            if (isSurvival(pAttacker) && isSurvival(pVictim)) {
                setCombat(pVictim, pAttacker);
            }
            return;
        }

        if (victim instanceof Player pVictim && !(attacker instanceof Player)) {
            if (isSurvival(pVictim)) {
                if (CombatConfig.Config.allDamage || (CombatConfig.Config.mobDamage && attacker instanceof LivingEntity)) {
                    setCombat(pVictim);
                }
            }
        }
    }

    private static boolean isSurvival(Player player) {
        return player instanceof ServerPlayer sp && sp.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
    }

    public static void setCombat(Player... players) {
        if (players.length == 0) return;
        
        updateTickRate(players[0]);
        int duration = CombatConfig.Config.combatTime * tickRate;
        List<ItemStack> disabledItems = CombatConfig.Config.disabledItems;

        for (Player player : players) {
            TagData.setTagTime((IEntityDataSaver) player);
            
            if (!disabledItems.isEmpty()) {
                for (ItemStack stack : disabledItems) {
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
        tickRate = (int) player.level().getServer().tickRateManager().tickrate();
        //?} else {
        /* tickRate = 20; */
        //?}
    }
}
