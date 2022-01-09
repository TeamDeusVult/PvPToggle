package tdv.pvptoggle.features.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.utils.PlayerPvPState;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class OnPlayerDeathEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity player = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (player instanceof PlayerEntity && attacker instanceof PlayerEntity && player != attacker) {
            PlayerPvPState playerPvPState = PvpToggle.pvpStatus.getOrDefault(player.getUUID(), new PlayerPvPState());
            PlayerPvPState attackerPvPState = PvpToggle.pvpStatus.getOrDefault(attacker.getUUID(), new PlayerPvPState());

            if (playerPvPState.getPlayerInDuelWith() == attacker.getUUID() && attackerPvPState.getPlayerInDuelWith() == player.getUUID()) {
                playerPvPState.setInDuel(false);
                playerPvPState.setPlayerInDuelWith(Util.NIL_UUID);

                attackerPvPState.setInDuel(false);
                attackerPvPState.setPlayerInDuelWith(Util.NIL_UUID);

                player.sendMessage(new StringTextComponent(player.getName().getString() + " has lost to " + attacker.getName().getString() + " in a duel."), Util.NIL_UUID);
                attacker.sendMessage(new StringTextComponent(player.getName().getString() + " has lost to " + attacker.getName().getString() + " in a duel."), Util.NIL_UUID);
            }
        }
    }
}
