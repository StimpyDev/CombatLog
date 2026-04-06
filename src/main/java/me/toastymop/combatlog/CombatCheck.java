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
    public static final int tickRate = 20;

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
        if (players == null || players.length == 0) return;
        
        int duration = CombatConfig.Config.combatTime * tickRate;
        List<ItemStack> disabledItems = CombatConfig.Config.disabledItems;

        for (Player player : players) {
            if (player == null) continue;

            TagData.setTagTime((IEntityDataSaver) player);
            
            if (disabledItems != null && !disabledItems.isEmpty()) {
                for (ItemStack stack : disabledItems) {
                    if (stack != null && !stack.isEmpty()) {
                        player.getCooldowns().addCooldown(stack, duration);
                    }
                }
            }
        }
    }
}
