package bg;

import arc.*;
import arc.struct.Array;
import arc.struct.IntArray;
import arc.struct.ObjectMap;
import arc.util.*;
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
    playState bgState = playState.waiting;

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
    private BuildTimer2 btimer;
    //scores
    private Scores scores;
    //playing players
    Array<Player> playingPlayers = new Array<>();
    ObjectMap<Player, Long> shootingPlayers = new ObjectMap<>();
    int doneBuilding = 0;

    @Override
    public void init(){
        rules.canGameOver = false;
        rules.tags.put("buildergame", "true");
        //sandboxmode
        rules.infiniteResources = true;
        rules.playerDamageMultiplier = 0f;
        rules.unitDamageMultiplier = 0f;
        rules.playerHealthMultiplier = 1000f;

        Events.on(PlayerJoin.class, event -> {
            if(!active())return;
            if(event.player.isMobile){
                Call.onInfoMessage(event.player.con, "[scarlet]TURN OFF AUTO-AIM![]\n\nShooting == dead!");
            }
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


            if(waiting && playerGroup.all().size < minPlayers){
                event.player.sendMessage("Waiting for more players. Use [accent]/rules[] to get more info about the game.");
            }else if(bgState == playState.started){
                Call.onInfoMessage(event.player.con, "[accent]*** SPECTATING ***[]\n\nThe game already started. You can check the current leaderboard with [accent]/lb[] and rules with [accent]/rules[]");
            }else if(bgState == playState.reset){
                event.player.sendMessage("Game ended. [sky]NEW[] game will end in less than "+Long.toString(timeBetweenGames/1000L)+" seconds");
            }else{
                event.player.sendMessage("Game is starting read the rules via /rules");
            }
        });

        Events.on(PlayerLeave.class, event -> {
            if(playingPlayers.contains(event.player)){
                playingPlayers.remove(event.player);
            }
        });

        //check if they are finished.
        Events.on(BlockBuildEndEvent.class, event -> {
            if(!active() || event.player == null)return;
            //TODO remove DEBUG
            if(event.player.buildQueue().size > schemThreshold && !event.breaking && !DEBUG){
                Call.sendMessage(event.player.name + " [scarlet] was cheating.");
                Call.onPlayerDeath(event.player);
                event.player.setTeam(finished);
                scores.updateScore(event.player, cheatPoints);
            }

            if(schems.checkSchematic(generator.getPlayerZone(event.player.getTeam().id))){
                Call.onPlayerDeath(event.player);
                event.player.setTeam(finished);
                int points = BuilderGame.Points[doneBuilding++];
                Call.sendMessage(String.format("%s [] finished and gained %d points.", event.player.name, points));
                scores.updateScore(event.player, points);
            }
        });

        //main event loop
        Events.on(Trigger.update, () -> {
            if(!active())return;
            btimer.update();
            if(bgState == playState.waiting)

            /*
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
                        nextRound = System.currentTimeMillis() + ((round==1)?0:timeBetweenRounds);
                    }
                }else if(betweenRounds){
                    if(System.currentTimeMillis()>nextRound){
                        for(int j = 0; j<playingPlayers.size; j++){
                            spawnPlayer(playingPlayers.get(j), j);
                        }
                        schems.random();
                        generator.spawnSchematic(schems.currentSchem);
                        btimer.startTimer(roundTime);
                        betweenRounds = false;
                        Call.sendMessage("Round "+ Integer.toString(round)+'/'+Integer.toString(maxGameRounds));
                    }
                }
            }else if(resetting){
                resetting = false;
            }else if(waiting && Vars.playerGroup.size() >= minPlayers){
                Call.onInfoMessage("[accent]The game will start soon ...[]\n"+rulesMsg);
                countdown = true;
                waiting = false;
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

             */
            /*
            if(gameReset){
                if(timeNewStart <= System.currentTimeMillis()){
                    gameReset = false;
                    started = false;
                    waiting = true;
                }else{
                    return;
                }
            }

            if(waiting && playerGroup.size() >= minPlayers){
                Call.sendMessage("[green]Enough players[]\nGame will start in 5 seconds.");
                Call.onInfoMessage(rulesMsg);
                btimer.startTimer(joinTimer);
                waiting = false;
            }else if(!started){
                if(!btimer.ticking() && !waiting){
                    round = 0;
                    scores.reset();
                    Array<Player> _players = Vars.playerGroup.all().copy();
                    _players.shuffle();
                    for(int i=0; i<maxPlayers; i++){
                        if(i >= Vars.playerGroup.size()) break;
                        playingPlayers.add(_players.get(i));
                    }
                    scores.setPlayers(playingPlayers.copy());
                    //start game
                    started = true;
                    betweenRounds = true;
                }
            }
            if(started){
                //game started
                if((btimer.ticking() && allFinished()) || (!btimer.ticking() && !betweenRounds)){
                    //end Game
                    System.out.println("finished " + Boolean.toString(allFinished()));
                    if(round == maxGameRounds){
                        scores.showLeaderboardEnd();
                        playingPlayers.clear();
                        gameReset = true;
                        this.timeNewStart = System.currentTimeMillis() + timeBetweenGames;
                    }else{
                        btimer.stopTimer();
                        betweenRounds = true;
                        Call.sendMessage("Next round start in "+Long.toString(timeBetweenRounds/1000L)+" seconds...");
                        this.startTime =  System.currentTimeMillis() + timeBetweenRounds;

                    }
                    generator.resetField();
                }
                if(betweenRounds){
                    if(this.startTime < System.currentTimeMillis() || round == 0){
                        betweenRounds = false;
                        round++;
                        //select schematic
                        schems.random();
                        //spawn players
                        for(int j = 0; j<playingPlayers.size; j++){
                            playingPlayers.get(j).setTeam(Team.all()[6+j]);
                            spawnPlayer(playingPlayers.get(j), j);
                        }
                        //build schematic
                        generator.spawnSchematic(schems.currentSchem);
                        //start timer
                        btimer.startTimer(roundTime);
                        StringBuilder sb = new StringBuilder();
                        sb.append("\nRound ").append(round).append('/').append(maxGameRounds).append(" [green]started...\n");
                        Call.sendMessage(sb.toString());
                    }
                }
            }
            */

            for(Player p:playingPlayers.copy()){
                if(p.isShooting){
                    shootingPlayers.put(p, System.currentTimeMillis() + timePenalty);
                    scores.updateScore(p, shootingPenalty);
                }else if(p.dead && !shootingPlayers.containsKey(p) && !betweenRounds && p.getTeam()!=finished && started){
                    System.out.println("Revive");
                    spawnPlayer(p, p.id);
                }else if(shootingPlayers.containsKey(p)){
                    if(System.currentTimeMillis() > shootingPlayers.get(p)){
                        shootingPlayers.remove(p);
                        spawnPlayer(p, p.id);
                    }
                }
            }
        });
    }

    //register event handlers and create variables in the constructor
    public BuilderGamePlugin(){
        //btimer = new BuildTimer(Thread.currentThread());
        btimer = new BuildTimer2();
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
            if(resetting || gameReset){
                Call.onInfoMessage(player.con, scores.LeaderboardInfoEnd());
            }else{
                Call.onInfoMessage(player.con, scores.LeaderboardInfo());
            }
        });

        //DEBUG
        if(DEBUG) {
            handler.<Player>register("queues", "ask fishbuilder", (args, player) -> {
                for (Player p : Vars.playerGroup.all()) {
                    player.sendMessage(p.name + " []: " + Integer.toString(p.buildQueue().size));
                }
            });

            handler.<Player>register("check", "<x> <y>", "test schematic", (args, player) -> {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                player.sendMessage(args[0] + " : " + args[1]);
                Schematics tester = new Schematics();
                Schematic s = tester.create(x, y, x + 5, y + 5);
                player.sendMessage(tester.writeBase64(s));
                System.out.println(tester.writeBase64(s));
            });

            handler.<Player>register("spawn", "[a]", "", (args, player) -> {
                schems.random();
                generator.spawnSchematic(schems.currentSchem);
            });

            handler.<Player>register("reset", "", (args, player) -> {
                generator.resetField();
            });

            handler.<Player>register("tester", "test", (args, player) -> {
                Boolean ret = schems.checkSchematic(generator.getPlayerZone(player.getTeam().id));
                if (ret) {
                    player.sendMessage("yeah");
                } else {
                    player.sendMessage("meh");
                }
            });

            handler.<Player>register("timer", "", (args, player) -> {
                BuildTimer bt = new BuildTimer(Thread.currentThread());
                bt.setTime(roundTime);
                bt.startTimer();
            });
        }
    }

    public void spawnPlayer(Player player, int id){
        player.setTeam(Team.all()[6+id]);
        int[] spawn = generator.getSpawns()[id];
        player.dead = false;
        //location
        Call.onPositionSet(player.con, spawn[0]*8, spawn[1]*8);
        player.setNet(spawn[0]*8, spawn[1]*8);
        player.set(spawn[0]*8, spawn[1]*8);


    }

    public boolean allFinished(){
        return doneBuilding == playingPlayers.size;
    }


    public boolean active(){
        return state.rules.tags.getBool("buildergame") && !state.is(GameState.State.menu);
    }
}
