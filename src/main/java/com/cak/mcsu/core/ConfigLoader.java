package com.cak.mcsu.core;

import com.cak.mcsu.Main;

public class ConfigLoader {

    public static boolean warnNullAny(String source, String suffix, Object... args) {
        boolean anyNull = false;

        for (int i=0; i<args.length; i+=2) {
            if (args[i+1] == null) {
                Main.warn("[" + source + "] " + args[i] + " is null " + suffix);
                anyNull = true;
            }
        }

        return anyNull;

    }

}
