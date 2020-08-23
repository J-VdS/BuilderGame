package bg;

import arc.util.Structs;
import mindustry.game.Team;

public class BuilderGame {
    //DEBUG
    static boolean DEBUG = true;

    //Teams
    static Team spectator = Team.purple;
    static Team finished = Team.sharded;

    //roundtime (in millis)
    static long roundTime = 30000L;
    //joinStarter
    static long joinTimer = 5000L;
    static long timeBetween = 5000L;

    //players
    static int maxPlayers = 5;

    //rounds
    static final int maxGameRounds = 5;

    //Points
    public static int[] Points = {5,4,3,2,1};
    public static int cheatPoints = -5;
    public static int unfinishedPoints = 0;

    //map
    public final static String mapnamepre = "BuilderZone: ";
    public static Pallete pallete = Structs.random(Pallete.values());

    //anti schematic threshold
    public static int schemThreshold = 8;

    //base Schematics
    public static String[] baseSchemsRaw = {
            "bXNjaAB4nGOQZOBiYGTgTs4vKEgt0i1PzMlhYGBgYkACEgycYBoAjNEEuQ==",
            "bXNjaAB4nF2P2w7CIBBEV3qzEmsvNtF3X/tRaIg2qW2DGOPfS2EzaeRpmFl2DnSmHSUkb9M8a9N91DBQY3urxv799NduUOauqbaPyfx5+6uyVpsvX+VrcqKb1agHIroQzobiIIRwSngVudrEqyXN2Mvc7KJiSiniFynSbdgjlvmUvZxT4RRxmqND8pbYfzO0SbDIlZexKpAW2HLAXAGWCqQlSEukNUgrkDYgbUDaoKMF6RFtLVjalRe2EJ34xz/VUiAW",
            "bXNjaAB4nD2OSxKCMBBEO2AUQX660YVH4FAUlUWqwqcganl7ExJ7snkz0z0dPJAjRTHMy6LW7tMbg9Zq20/6NXbDPL3Vd15Rb9po13XbqIxVK4AnWAIH9/4kI0l310PiaN8KgWOcCZzCLPUkoy6jLnPKsD2Tcjry6BAomFHQe2FGSUfJjIq6ipdrUkNHw4yWGS29V2bcwizxtDsS4O5+7esHTZoUEA=="
        };

    //rules
    public final static String rulesMsg = "[][white]*** RULES ***[]\n* Copy the schematic in your zone.\nThe player who is fastest gets the most points.\n* If you fail to copy withing the timeframe you will get no points.\n*[scarlet] Copy/schematics === CHEATING\n[]\n Use /rules to view this message.";
    };

