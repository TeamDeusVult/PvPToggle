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

public class DuelAcceptCommand {
    public static LiteralArgumentBuilder<CommandSource> DuelAcceptCommand = Commands.literal("duelaccept")
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

        if (duelerState.isCombatBlocked()) {
            source.sendFailure(new StringTextComponent("You're on combat timer!"));
            return 0;
        }

        if (duelerState.isInDuel()) {
            source.sendFailure(new StringTextComponent("You're already in a duel!"));
        } else if (opponentState.isInDuel()) {
            source.sendFailure(new StringTextComponent("Opponent is already in a duel!"));
        } else {
            if (opponentState.hasDuelRequest(dueler.getUUID())) {
                opponentState.removeDuelRequest(dueler.getUUID());
                // setup duel
                duelerState.setInDuel(true);
                duelerState.setPlayerInDuelWith(opponent.getUUID());

                opponentState.setInDuel(true);
                opponentState.setPlayerInDuelWith(dueler.getUUID());

                source.sendSuccess(new StringTextComponent("Duel request accepted!"), true);
                opponent.sendMessage(new StringTextComponent("Duel started with " + dueler.getName().getString()), Util.NIL_UUID);
            } else {
                source.sendFailure(new StringTextComponent("You do not have a pending duel request!"));
            }
        }

        return 1;
    }
}
