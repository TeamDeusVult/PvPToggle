package tdv.pvptoggle.features.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.commands.*;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class OnRegisterCommandsEvent
{
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        registerCommand(dispatcher, PvpToggleCommand.PvpToggleCommand);
        registerCommand(dispatcher, DuelCommand.DuelCommand);
        registerCommand(dispatcher, DuelAcceptCommand.DuelAcceptCommand);
        registerCommand(dispatcher, DuelDeclineCommand.DuelDeclineCommand);
        registerCommand(dispatcher, DuelCancelCommand.DuelCancelCommand);

        ConfigCommand.register(event.getDispatcher());
    }
    public static void registerCommand(CommandDispatcher<CommandSource> dispatcher, LiteralArgumentBuilder<CommandSource> source)
    {
        dispatcher.register(source);
    }
}
