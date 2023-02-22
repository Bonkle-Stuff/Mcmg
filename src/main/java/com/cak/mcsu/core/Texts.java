package com.cak.mcsu.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Texts {

    static TextColor heartColor = TextColor.color(255, 98, 76);
    static TextColor skullColor = TextColor.color(57, 66, 73);

    public static Component life(boolean alive) {
        return (alive ? Component.text("[☠] ").color(skullColor) :
                Component.text("[❤] ").color(heartColor));
    }

    public static Component lives(int lives) {
        return (lives == 0 ? Component.text("[☠] ").color(skullColor) :
                Component.text("["+lives+"❤] ").color(heartColor));
    }

    public static Component livesBar(int maxLives, int lives) {
        return (lives == 0 ? Component.text("-< ").color(skullColor) :
                Component.text("Lives: ").color(heartColor)).append(
                        Component.text("❤ ".repeat(lives)).color(heartColor)
        ).append(
                Component.text("☠ ".repeat(maxLives-lives)).color(skullColor)
        ).append(
                (lives == 0 ? Component.text(">-").color(skullColor) :
                        Component.text("").color(heartColor))
        );
        /*return (lives == 0 ? skullColor + "-< ": heartColor + "Lives: ") +
                heartColor +
                "❤ ".repeat(lives)+
                skullColor +
                "☠ ".repeat(maxLives-lives) +
                (lives == 0 ? ">-" : "");*/
    }

}
