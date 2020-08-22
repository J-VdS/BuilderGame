package bg;

import arc.util.Structs;

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
            "bXNjaAB4nE2JXQqAIBAGP03N17qHh5KyH9AUM7p+ahKx7DI7A4megB3aGYhkzrTMFMPkQzBR3dpaZXVcDca0+bhf7u8AmRddOZSA5ilvId5CbdWxlzPxrwqQ5kSrD9KGEnw="
        };
    };

