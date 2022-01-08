package tdv.pvptoggle.features.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.commands.PvpToggleCommand;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class PvpToggleRegisterCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        new PvpToggleCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
