package tdv.pvptoggle.features.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tdv.pvptoggle.PvpToggle;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class PlayerDamage {
    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity && event.getSource() != null && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity playerAttack = (PlayerEntity) event.getSource().getEntity();
            PlayerEntity playerAttacked = (PlayerEntity) event.getEntityLiving();
            if (PvpToggle.pvpStatus.getOrDefault(playerAttacked.getName().toString(), false) && PvpToggle.pvpStatus.getOrDefault(playerAttack.getName().toString(), false)) {
                event.setCanceled(false);
                PvpToggle.combatTimer.put(playerAttacked.getName().toString(), System.currentTimeMillis() / 1000L + 120000);
                PvpToggle.combatTimer.put(playerAttack.getName().toString(), System.currentTimeMillis() / 1000L + 120000);
            } else {
                event.setCanceled(true);
                ITextComponent msg = new StringTextComponent("Both players must have PvP enabled to attack each other.");
                playerAttack.sendMessage(msg, Util.NIL_UUID);
            }
        }
    }
}
