package me.abhi.hcf.events;

import lombok.Getter;
import me.abhi.hcf.cooldown.Cooldown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CooldownEndEvent extends Event {

    private Cooldown cooldown;

    private static HandlerList handlerList = new HandlerList();

    public CooldownEndEvent(Cooldown cooldown) {
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