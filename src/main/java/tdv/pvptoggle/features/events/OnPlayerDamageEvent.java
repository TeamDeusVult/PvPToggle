package tdv.pvptoggle.features.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.utils.PlayerPvPState;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class OnPlayerDamageEvent
{
    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event)
    {
        if (event.getEntityLiving() instanceof PlayerEntity &&
                event.getSource() != null &&
                event.getSource().getEntity() != null &&
                event.getSource().getEntity() instanceof PlayerEntity &&
                event.getSource().getEntity() != event.getEntityLiving())
        {
            PlayerEntity playerAttack = (PlayerEntity) event.getSource().getEntity();
            PlayerEntity playerAttacked = (PlayerEntity) event.getEntityLiving();

            PlayerPvPState playerAttackedState = PvpToggle.pvpStatus.getOrDefault(playerAttacked.getUUID(), new PlayerPvPState());
            PlayerPvPState playerAttackState = PvpToggle.pvpStatus.getOrDefault(playerAttack.getUUID(), new PlayerPvPState());

            if (playerAttackState.isInDuel() && playerAttackedState.isInDuel() && playerAttackedState.getPlayerInDuelWith() == playerAttack.getUUID() && playerAttackState.getPlayerInDuelWith() == playerAttacked.getUUID()) {
                event.setCanceled(false);
                playerAttackState.combatBlock();
                playerAttackedState.combatBlock();
            } else {
                if (playerAttackState.isInPvP() && playerAttackedState.isInPvP() && !playerAttackState.isInDuel() && !playerAttackedState.isInDuel()) {
                    event.setCanceled(false);
                    playerAttackState.combatBlock();
                    playerAttackedState.combatBlock();
                } else {
                    event.setCanceled(true);
                    ITextComponent msg = new StringTextComponent("Both players must have PvP enabled (and not be in a duel) to attack each other.");
                    playerAttack.sendMessage(msg, Util.NIL_UUID);
                }
            }
        }
    }
}
