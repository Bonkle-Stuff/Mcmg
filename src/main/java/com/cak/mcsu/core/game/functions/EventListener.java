package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.event.Listener;

public class EventListener extends GameFunction {

    public EventListener(Listener listener) {

        setListener(listener);

    }

}
