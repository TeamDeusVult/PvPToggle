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

public class DuelDeclineCommand {
    public static LiteralArgumentBuilder<CommandSource> DuelDeclineCommand = Commands.literal("dueldecline")
            .then(Commands.argument("target", EntityArgument.player())
                    .executes(command -> execute(command, EntityArgument.getPlayer(command, "target"))));

    public static int execute(CommandContext<CommandSource> context, ServerPlayerEntity opponent) throws CommandSyntaxException
    {
        CommandSource source = context.getSource();
        ServerPlayerEntity dueler = source.getPlayerOrException();

        PvpToggle.pvpStatus.putIfAbsent(dueler.getUUID(), new PlayerPvPState());
        PvpToggle.pvpStatus.putIfAbsent(opponent.getUUID(), new PlayerPvPState());

        PlayerPvPState opponentState = PvpToggle.pvpStatus.get(opponent.getUUID());

        if (opponentState.hasDuelRequest(dueler.getUUID())) {
            opponentState.removeDuelRequest(dueler.getUUID());
            source.sendSuccess(new StringTextComponent("You have declined the duel request from " + opponent.getName().getString()), true);
            opponent.sendMessage(new StringTextComponent("Your duel request to " + dueler.getName().getString() + " has been declined."), Util.NIL_UUID);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent("You do not have a pending duel request!"));
            return 0;
        }
    }
}
