package com.cak.mcsu.core.game.helpers;

import com.cak.mcsu.Main;
import com.sun.jdi.Method;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.function.Consumer;

public class EventListener<T extends Event> implements Listener {

    Consumer<T> onEvent;

    public EventListener(Class<T> tClass, Consumer<T> onEvent) {
        this.onEvent = onEvent;
        try {
            Main.plugin.getServer().getPluginManager().registerEvent(
                    tClass, this,
                    EventPriority.LOW,
                    EventExecutor.create(, tClass),
                    Main.plugin
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onEvent(<? extends Event> event) {
        onEvent.accept(event);
    }

}
