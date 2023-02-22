package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import com.cak.mcsu.core.game.helpers.EventListener;
import org.bukkit.event.Event;

public class CustomEventListener<T extends Event> extends GameFunction {

    public CustomEventListener(EventListener<T> listener) {
        setListener(listener);
    }

}
