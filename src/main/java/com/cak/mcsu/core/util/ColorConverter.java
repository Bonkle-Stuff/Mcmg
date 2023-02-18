package com.cak.mcsu.core.util;

import java.util.Arrays;

public enum ColorConverter {
    BLACK( '0', "black", 0, 0, 0 ),
    DARK_BLUE( '1', "dark_blue", 0, 0, 255),
    DARK_GREEN( '2', "dark_green", 0, 255, 0),
    DARK_AQUA( '3', "dark_aqua", 0, 255, 255),
    DARK_RED( '4', "dark_red", 255, 0, 0),
    DARK_PURPLE( '5', "dark_purple", 255, 0, 255),
    GOLD( '6', "gold", 255, 255, 0),
    GRAY( '7', "gray", 255, 255, 255),
    DARK_GRAY( '8', "dark_gray", 85, 85, 85),
    BLUE( '9', "blue", 85, 85, 255 ),
    GREEN( 'a', "green", 85, 255, 85),
    AQUA( 'b', "aqua", 85, 255, 255 ),
    RED( 'c', "red", 255, 85, 85),
    LIGHT_PURPLE( 'd', "light_purple", 255, 85, 255 ),
    YELLOW( 'e', "yellow", 255, 255, 85),
    WHITE( 'f', "white", 255, 255, 255);

    public final char code;
    public final String name;
    public final int r;
    public final int g;
    public final int b;
    ColorConverter(char code, String name, int r, int g, int b) {
        this.code = code;
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static ColorConverter fromCode(char code) {
        return Arrays.stream(ColorConverter.values()).filter(colorConverter -> colorConverter.code == code).findFirst().orElse(null);
    }
    
}
