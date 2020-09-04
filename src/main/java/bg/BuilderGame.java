package bg;

import arc.util.Structs;
import mindustry.game.Team;

public class BuilderGame {
    //DEBUG
    static boolean DEBUG = false;
    static int minPlayers = 1;

    //Teams
    static Team spectator = Team.purple;
    static Team finished = Team.sharded;

    //roundtime (in millis)
    static long roundTime = 90000L;
    //joinStarter
    static long joinTimer = 5000L;
    static long timeBetweenRounds = 3000L;
    static long timeBetweenGames = 5000L;
    static long timePenalty = 10000L;

    //players
    static int maxPlayers = 5;

    //rounds
    static final int maxGameRounds = 5;

    //Points
    public static int[] Points = {5,4,3,2,1};
    public static int cheatPoints = -5;
    public static int shootingPenalty = -1;

    //map
    public final static String mapnamepre = "BuilderZone: ";
    public static Pallete pallete = Structs.random(Pallete.values());

    //anti schematic threshold
    public static int schemThreshold = 8;

    //base Schematics
    public static String[] baseSchemsRaw = {
            "bXNjaAB4nGOQZOBiYGTgTs4vKEgt0i1PzMlhYGBgYoABIEuCgRPMAgCM6QS9",
            };

    //rules
    public final static String rulesMsg = "[][sky]*** RULES ***[]\n* Copy the schematic in your zone.\n\nThe player who is fastest gets the most points.\n\n* If you fail to copy withing the timeframe you will get no points.\n\n*[scarlet] Copy/schematics === CHEATING\nShooting === Penalty\n\n[]Use /rules to view this message.";

    public static enum playState{
        waiting,
        started,
        reset,
    }
};

