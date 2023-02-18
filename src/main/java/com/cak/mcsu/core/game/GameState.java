package com.cak.mcsu.core.game;

import java.util.ArrayList;

public class GameState {

    public static boolean postRegister = false;
    String id;
    boolean enabled = false;
    boolean enabledDefault;
    Runnable onEnable;
    Runnable onDisable;

    ArrayList<GameFunction> gameFunctions = new ArrayList<>();

    public GameState(String id, boolean enabledDefault) { this.id = id; this.enabledDefault = enabledDefault; }
    public GameState(String id) { this.id = id; this.enabledDefault = false; }
    public GameState addGameFunction(GameFunction gameFunction) {
        gameFunctions.add(gameFunction);
        if (postRegister) { gameFunction.setEnabled(enabled); gameFunction.setIsTemporary(true); }
        return this;
    }

    public GameState addGameFunctions(GameFunction... gameFunctions) {
        for (GameFunction gameFunction : gameFunctions) {
            addGameFunction(gameFunction);
        }
        return this;
    }

    public ArrayList<GameFunction> getGameFunctions() { return this.gameFunctions; }

    public boolean getEnabled() { return enabled; }

    public String getId() { return id; }

    public void setEnabled(boolean enabled) {

        if (this.enabled != enabled) {//If there is a change in enabled state

            this.enabled = enabled;

            for (GameFunction gameFunction : gameFunctions) {

                gameFunction.setEnabled(enabled);

            }

            if ( enabled ) { //Execute on enable or on disable
                if (onEnable != null) {
                    onEnable.run();
                }
            } else {
                if (onDisable != null) {
                    onDisable.run();
                }
            }

        }

    }

    public void setup() {
        if (enabledDefault) {
            setEnabled(true);
        }
    }

    public void reset() {

        setEnabled(false);

        gameFunctions.removeIf(GameFunction::getIsTemporary);

        this.enabled = enabledDefault;

    }

    public GameState setOnEnable(Runnable onEnable) { this.onEnable = onEnable; return this; }
    public GameState setOnDisable(Runnable onDisable) { this.onDisable = onDisable; return this; }

}
