package bg;

import arc.struct.Array;
import arc.util.Structs;
import mindustry.game.Schematic;
import mindustry.game.Schematics;


public class BuilderGame {
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
            ""
        };
    };

