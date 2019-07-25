package me.abhi.hcf.events;

import lombok.Getter;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.cooldown.ServerCooldown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerCooldownEndEvent extends Event {

    private ServerCooldown cooldown;

    private static HandlerList handlerList = new HandlerList();

    public ServerCooldownEndEvent(ServerCooldown cooldown) {
        this.cooldown = cooldown;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
