package tdv.pvptoggle.features.utils;

import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerPvPState {
    private boolean pvpState;
    private List<UUID> duelRequest;
    private boolean isInDuel;
    private UUID inDuelWith;
    private long combatTimer;

    public PlayerPvPState() {
        this.pvpState = false;
        this.duelRequest = new ArrayList<>();
        this.isInDuel = false;
        this.inDuelWith = Util.NIL_UUID;
        this.combatTimer = 0L;
    }

    public boolean isInPvP() {
        return this.pvpState;
    }

    public UUID getPlayerInDuelWith() {
        return this.inDuelWith;
    }

    public void setPvPState(boolean state) {
        this.pvpState = state;
    }

    public void setPlayerInDuelWith(UUID duel) {
        this.inDuelWith = duel;
    }

    public boolean isCombatBlocked() {
        return System.currentTimeMillis() / 1000L < this.combatTimer;
    }

    public void combatBlock() {
        this.combatTimer = System.currentTimeMillis() / 1000L + 120;
    }

    public long getCombatTimer() {
        return this.combatTimer;
    }

    public List<UUID> getDuelRequests() {
        return this.duelRequest;
    }

    public boolean hasDuelRequest(UUID duelRequest) {
        return this.duelRequest.contains(duelRequest);
    }

    public boolean sendDuelRequest(UUID request) {
        if (!(this.duelRequest.contains(request))) {
            this.duelRequest.add(request);
            return true;
        }
        return false;
    }

    public boolean removeDuelRequest(UUID request) {
        return this.duelRequest.remove(request);
    }

    public boolean isInDuel() {
        return this.isInDuel;
    }

    public void setInDuel(boolean isInDuel) {
        this.isInDuel = isInDuel;
    }
}
