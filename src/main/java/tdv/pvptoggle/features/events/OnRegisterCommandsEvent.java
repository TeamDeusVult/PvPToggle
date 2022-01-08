package tdv.pvptoggle.features.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.commands.PvpToggleCommand;

@Mod.EventBusSubscriber(modid = PvpToggle.MODID)
public class OnRegisterCommandsEvent
{
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        registerCommand("pvp", dispatcher, PvpToggleCommand::execute);

        ConfigCommand.register(event.getDispatcher());
    }


    public static void registerCommand(String name, CommandDispatcher<CommandSource> dispatcher, Command<CommandSource> command)
    {
        dispatcher.register(Commands.literal(name).executes(command));
    }
}
