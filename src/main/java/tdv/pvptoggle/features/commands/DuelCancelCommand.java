package tdv.pvptoggle.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.utils.PlayerPvPState;

public class DuelCancelCommand {
    public static LiteralArgumentBuilder<CommandSource> DuelCancelCommand = Commands.literal("duelcancel")
            .then(Commands.argument("target", EntityArgument.player())
                    .executes(command -> execute(command, EntityArgument.getPlayer(command, "target"))));

    public static int execute(CommandContext<CommandSource> context, ServerPlayerEntity opponent) throws CommandSyntaxException
    {
        CommandSource source = context.getSource();
        ServerPlayerEntity dueler = source.getPlayerOrException();

        PvpToggle.pvpStatus.putIfAbsent(dueler.getUUID(), new PlayerPvPState());
        PvpToggle.pvpStatus.putIfAbsent(opponent.getUUID(), new PlayerPvPState());

        PlayerPvPState duelerState = PvpToggle.pvpStatus.get(dueler.getUUID());
        PlayerPvPState opponentState = PvpToggle.pvpStatus.get(opponent.getUUID());

        if (duelerState.isInDuel() && opponentState.isInDuel() && duelerState.getPlayerInDuelWith() == opponent.getUUID() && opponentState.getPlayerInDuelWith() == dueler.getUUID()) {
            if (duelerState.isCombatBlocked()) {
                source.sendFailure(new StringTextComponent("You're on combat timer!"));
                return 0;
            }

            duelerState.setInDuel(false);
            duelerState.setPlayerInDuelWith(Util.NIL_UUID);


            opponentState.setInDuel(false);
            opponentState.setPlayerInDuelWith(Util.NIL_UUID);
        }

        if (duelerState.hasDuelRequest(opponent.getUUID())) {
            duelerState.removeDuelRequest(opponent.getUUID());
            source.sendSuccess(new StringTextComponent("Duel request cancelled."), true);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent("You do not have a pending duel request!"));
            return 0;
        }
    }
}
