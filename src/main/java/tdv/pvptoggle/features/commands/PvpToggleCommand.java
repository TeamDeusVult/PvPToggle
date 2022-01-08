package tdv.pvptoggle.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import tdv.pvptoggle.PvpToggle;

public class PvpToggleCommand
{
    public static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!(System.currentTimeMillis() / 1000L > PvpToggle.combatTimer.getOrDefault(player.getName().toString(), 0L))) {
            source.sendFailure(new StringTextComponent("You're on combat timer!"));
            return 0;
        }
        boolean pvpStatus = PvpToggle.pvpStatus.getOrDefault(player.getName().toString(), false);
        PvpToggle.pvpStatus.put(player.getName().toString(), !pvpStatus);
        source.sendSuccess(new StringTextComponent(!pvpStatus ? "PvP is now enabled." : "PvP is now disabled."), true);
        return 1;
    }
}
