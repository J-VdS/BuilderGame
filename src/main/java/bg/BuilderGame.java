package bg;

import arc.util.Structs;

public class BuilderGame {
    //DEBUG
    static boolean DEBUG = true;


    //roundtime (in millis)
    static long roundTime = 30000L;

    //players
    static int maxPlayers = 5;

    //map
    public final static String mapnamepre = "BuilderZone: ";
    public static Pallete pallete = Structs.random(Pallete.values());

    //anti schematic threshold
    public static int schemThreshold = 3;

    //base Schematics
    public static String[] baseSchemsRaw = {
            "bXNjaAB4nGOQZOBiYGTgTs4vKEgt0i1PzMlhYGBgYkACEgycYBoAjNEEuQ=="
            };
    };

