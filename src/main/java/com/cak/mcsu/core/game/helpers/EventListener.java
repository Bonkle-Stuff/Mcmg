package com.cak.mcsu.core.game.helpers;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public class EventListener<T extends Event> implements Listener {

    Consumer<T> onEvent;

    public EventListener(Consumer<T> onEvent) {
        this.onEvent = onEvent;
    }

    @EventHandler
    public void onEvent(T event) {
        onEvent.accept(event);
    }

}
