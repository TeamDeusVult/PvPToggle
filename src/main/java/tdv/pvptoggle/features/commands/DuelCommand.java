package tdv.pvptoggle.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import tdv.pvptoggle.PvpToggle;
import tdv.pvptoggle.features.utils.PlayerPvPState;

public class DuelCommand {
    public static LiteralArgumentBuilder<CommandSource> DuelCommand = Commands.literal("duel")
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
            PvpToggle.LOGGER.info(System.currentTimeMillis() / 1000L);
            PvpToggle.LOGGER.info(duelerState.getCombatTimer());
            return 0;
        }

        // TODO: Change this so if the opponent has requested a duel and you attempt to request a duel it will start the duel.
        if (!(duelerState.hasDuelRequest(opponent.getUUID()) || opponentState.hasDuelRequest(dueler.getUUID()) || duelerState.isInDuel() || opponentState.isInDuel())) {
            duelerState.sendDuelRequest(opponent.getUUID());

            ITextComponent duelRequest = new StringTextComponent(dueler.getName().getString() + " has challenged you to a fight for the ages!\n");
            Style emptyStyle = Style.EMPTY;

            ClickEvent acceptClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelaccept " + dueler.getName().getString());
            duelRequest.getSiblings().add(new StringTextComponent("[Accept]").setStyle(emptyStyle.withColor(TextFormatting.GREEN).withClickEvent(acceptClickEvent)));

            duelRequest.getSiblings().add(new StringTextComponent(" "));

            ClickEvent declineClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueldecline " + dueler.getName().getString());
            duelRequest.getSiblings().add(new StringTextComponent("[Decline]").setStyle(emptyStyle.withColor(TextFormatting.RED).withClickEvent(declineClickEvent)));

            opponent.sendMessage(duelRequest, Util.NIL_UUID);
            source.sendSuccess(new StringTextComponent("You have challenged " + opponent.getName().getString() + " to a fight for the ages!"), true);

            return 1;
        } else if (duelerState.isInDuel() || opponentState.isInDuel()) {
            if (duelerState.isInDuel()) source.sendFailure(new StringTextComponent("You're already in a duel!"));
            else source.sendFailure(new StringTextComponent("That player is already in a duel!"));
            return 0;
        } else {
            source.sendFailure(new StringTextComponent("A duel request is already pending!"));
            return 0;
        }
    }
}
