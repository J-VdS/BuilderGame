package bg;

import arc.*;
import arc.struct.Array;
import arc.util.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import mindustry.*;
import mindustry.core.GameState;
import mindustry.entities.type.*;
import mindustry.game.EventType.*;

import mindustry.game.Rules;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.plugin.Plugin;

import static mindustry.Vars.*;
import static bg.BuilderGame.*;


public class BuilderGamePlugin extends Plugin{
    //join variables
    Boolean started = false;
    Boolean resetting = false;
    Boolean betweenRounds = false;
    //if players>2 start countdown 5 seconds
    Boolean waiting = true;
    Boolean countdown = false;
    //ints
    int round = 0;
    Long nextRound = 0L;


    public final Rules rules = new Rules();

    private PlayfieldGenerator generator;
    //schematic dict/check
    public SchematicOption schems;
    //timer
    private BuildTimer btimer;
    //scores
    private Scores scores;
    //playing players
    Array<Player> playingPlayers = new Array<>();
    int doneBuilding = 0;

    @Override
    public void init(){
        rules.canGameOver = false;
        rules.tags.put("buildergame", "true");
        //sandboxmode
        rules.infiniteResources = true;
        rules.playerDamageMultiplier = 0f;

        Events.on(PlayerJoin.class, event -> {
            if(!active())return;
            event.player.kill();
            event.player.setTeam(spectator);
            if(DEBUG){
                event.player.setTeam(Team.all()[6]);//Team.sharded);
                event.player.dead = false;

                int[] spawn = generator.getSpawns()[0];
                Call.onPositionSet(event.player.con, spawn[0]*8, spawn[1]*8);
                event.player.setNet(spawn[0]*8, spawn[1]*8);
                event.player.set(spawn[0]*8, spawn[1]*8);
            }

            if(waiting && playerGroup.all().size < 2){
                event.player.sendMessage("Waiting for players. Use [accent]/rules[] to get more info about the game.");
            }
        });

        Events.on(PlayerLeave.class, event -> {
            if(playingPlayers.contains(event.player)){
                playingPlayers.remove(event.player);
            }
        });

        //check if they are finished.
        Events.on(BlockBuildEndEvent.class, event -> {
            if(!active())return;
            //TODO remove DEBUG
            if(event.player.buildQueue().size > schemThreshold && !event.breaking && !DEBUG) {
                Call.sendMessage(event.player.name + " [scarlet] was cheating.");
                Call.onPlayerDeath(event.player);
                event.player.setTeam(finished);
                scores.updateScore(event.player, cheatPoints);
            }

            if(schems.checkSchematic(generator.getPlayerZone(event.player.getTeam().id))){
                Call.onPlayerDeath(event.player);
                event.player.setTeam(finished);
                int points = BuilderGame.Points[doneBuilding];
                Call.sendMessage(String.format("%s [] finished and gained %d points.", event.player.name, points));
            }
        });

        //main event loop
        Events.on(Trigger.update, () -> {
            if(!active())return;
            if(started) {
                if((btimer.ticking() && allFinished() || !btimer.ticking()) && !betweenRounds){
                    if(round > maxGameRounds) {
                        scores.showLeaderboardEnd();
                        started = false;
                        resetting = true;
                    }else{
                        round++;
                        btimer.stopTimer();
                        doneBuilding = 0;
                        playingPlayers.forEach(p -> p.kill());
                        generator.resetField();
                        //start next round
                        betweenRounds = true;
                        nextRound = System.currentTimeMillis() + ((round==1)?0:timeBetween);
                    }
                }else if(betweenRounds){
                    if(System.currentTimeMillis()>nextRound){
                        for(int j = 0; j<playingPlayers.size; j++){
                            spawnPlayer(playingPlayers.get(j), j);
                        }
                        schems.random();
                        generator.spawnSchematic(schems.getCurrentSchem());
                        btimer.startTimer(roundTime);
                        betweenRounds = false;
                        Call.sendMessage(Strings.format("Round %d / %d", round, maxGameRounds));
                    }
                }
            }else if(resetting){
                resetting = false;
            }else if(waiting && Vars.playerGroup.size() > 1){
                Call.onInfoMessage("[accent]The game will start soon ...[]\n"+rulesMsg);
                countdown = true;
                waiting = true;
                btimer.startTimer(joinTimer);
            }else if(countdown && !btimer.ticking()){
                round = 0;
                Array<Player> _players = Vars.playerGroup.all().copy();
                _players.shuffle();
                for(int i=0; i<maxPlayers; i++){
                    if(i >= Vars.playerGroup.size()) break;
                    playingPlayers.add(_players.get(i));
                }
                scores.setPlayers(playingPlayers.copy());
                //start game
                started = true;
            }
        });
    }

    //register event handlers and create variables in the constructor
    public BuilderGamePlugin(){
        btimer = new BuildTimer(Thread.currentThread());
        scores = new Scores();
    }

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("buildergame", "Start the builderGame", args -> {
            //Start
            schems = new SchematicOption();

            logic.reset();
            Log.info("generating map");
            world.loadGenerator(generator = new PlayfieldGenerator());
            Log.info("Map generated.");
            state.rules = rules.copy();
            logic.play();
            netServer.openServer();
        });

        handler.register("bg-list", "All the schematics used in the game.", args -> {
            Log.infoList(schems.printList());
        });

        /*
        handler.register("bg-map", "Show all schematics on a map.", args -> {
        });
        */
        /*
        handler.register("bg", "<add/remove>", "<schematic-B64>", args -> {
            switch (args[0]) {
                case "add":
                    schems.addSchematic(args[1]);
                    break;
                case "remove":
                    schems.removeSchematic(args[1]);
                    break;
                default:
                    Log.err("Invalid command", "bg <add/remove> <schematic-B64>");
            }
        });
         */
    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.<Player>register("rules", "shows the rules", (args, player) -> {
            Call.onInfoMessage(player.con, rulesMsg);
        });

        handler.<Player>register("lb", "Shows the leaderboard", (args, player) -> {
            Call.onInfoMessage(player.con, scores.LeaderboardInfo());
        });

        //DEBUG
        handler.<Player>register("queues", "ask fishbuilder", (args, player) -> {
            for(Player p: Vars.playerGroup.all()) {
                player.sendMessage(p.name + " []: " + Integer.toString(p.buildQueue().size));
            }
        });

        handler.<Player>register("check", "<x> <y>", "test schematic", (args, player)->{
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            player.sendMessage(args[0] + " : " + args[1]);
            Schematics tester = new Schematics();
            Schematic s = tester.create(x,y, x+5,y+5);
            player.sendMessage(tester.writeBase64(s));
            System.out.println(tester.writeBase64(s));
        });

        handler.<Player>register("spawn", "[a]", "", (args, player)->{
            if(args.length == 0) {
                int index = Math.round((float)Math.random());
                generator.spawnSchematic(schems.baseSchems.values().toArray().get(index));
                schems.currentSchem = schems.baseSchems.keys().toArray().get(index);
            }
        });

        handler.<Player>register("reset", "", (args, player)->{
            generator.resetField();
        });

        handler.<Player>register("tester", "test", (args, player)->{
            Boolean ret = schems.checkSchematic(generator.getPlayerZone(player.getTeam().id));
            if(ret){
                player.sendMessage("yeah");
            }else{
                player.sendMessage("meh");
            }
        });

        handler.<Player>register("timer", "", (args, player)->{
           BuildTimer bt = new BuildTimer(Thread.currentThread());
           bt.setTime(roundTime);
           bt.startTimer();
        });
    }

    public void spawnPlayer(Player player, int id){
        player.setTeam(Team.all()[6+id]);
        player.dead = false;
        int[] spawn = generator.getSpawns()[id];
        //location
        Call.onPositionSet(player.con, spawn[0]*8, spawn[1]*8);
        player.setNet(spawn[0]*8, spawn[1]*8);
        player.set(spawn[0]*8, spawn[1]*8);
    }

    public boolean allFinished(){
        /*
        int counter = 0;
        for(Player p: playerGroup){
            if(p.getTeam() == finished) counter++;
        }
        return  counter == playingPlayers.size;
         */
        return doneBuilding == playingPlayers.size;
    }


    public boolean active(){
        return state.rules.tags.getBool("buildergame") && !state.is(GameState.State.menu);
    }
}
