package bg;

import arc.util.Structs;

public class BuilderGame {
    //players
    static int maxPlayers = 5;

    //map
    public final static String mapnamepre = "BuilderZone: ";
    public static Pallete pallete = Structs.random(Pallete.values());

    //anti schematic threshold
    public static int schemThreshold = 3;
}
