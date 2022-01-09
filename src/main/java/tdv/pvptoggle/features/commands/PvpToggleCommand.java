package tdv.pvptoggle.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.utils.PlayerPvPState;

public class PvpToggleCommand
{
    public static LiteralArgumentBuilder<CommandSource> PvpToggleCommand = Commands.literal("pvp").executes(command -> execute(command));

    public static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrException();

        PvpToggle.pvpStatus.putIfAbsent(player.getUUID(), new PlayerPvPState());
        PlayerPvPState playerPvPState = PvpToggle.pvpStatus.getOrDefault(player.getUUID(), new PlayerPvPState());

        if (playerPvPState.isCombatBlocked()) {
            source.sendFailure(new StringTextComponent("You're on combat timer!"));
            return 0;
        }

        boolean pvpStatus = playerPvPState.isInPvP();
        playerPvPState.setPvPState(!pvpStatus);
        source.sendSuccess(new StringTextComponent(!pvpStatus ? "PvP is now enabled." : "PvP is now disabled."), true);
        return 1;
    }
}
