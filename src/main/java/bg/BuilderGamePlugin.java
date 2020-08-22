package bg;

import arc.*;
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
    //join variables
    Boolean started = false;
    Boolean waiting = true;
    static Team dead = Team.crux;

    //schematic dict/check
    public SchematicOption schems = new SchematicOption();
    public final Rules rules = new Rules();

    private PlayfieldGenerator generator;


    @Override
    public void init(){
        rules.canGameOver = false;
        rules.tags.put("buildergame", "true");
        //sandboxmode
        rules.infiniteResources = true;

        Events.on(PlayerJoin.class, event -> {
            if(!active())return;
            event.player.kill();
            event.player.setTeam(Team.sharded);
            event.player.dead = false;

            Call.onPositionSet(event.player.con, 10*8, 10*8);
        });

        //main event loop
        Events.on(Trigger.update, () -> {
            if(!active())return;
        });

        //check if they are finished.
        Events.on(BlockBuildEndEvent.class, event -> {
            if(!active())return;
            if(event.player.buildQueue().size > schemThreshold && !event.breaking){
                Call.sendMessage(event.player + "[scarlet] was cheating.");
                Call.onPlayerDeath(event.player);
            }
        });
    }

    //register event handlers and create variables in the constructor
    public BuilderGamePlugin(){
        //testing
        /*
        Events.on(BlockBuildEndEvent.class, event -> {
            if(event.player.buildQueue().size > schemThreshold){
                Call.sendMessage(event.player + "[scarlet] was cheating.");
                Call.onPlayerDeath(event.player);
            }
        });
        */
    }

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("buildergame", "Start the builderGame", args -> {
            //Start
            logic.reset();
            Log.info("generating map");
            world.loadGenerator(generator = new PlayfieldGenerator());
            Log.info("Map generated.");
            state.rules = rules.copy();
            logic.play();
            netServer.openServer();
        });

        handler.register("bg-list", "All the schematics used in the game.", args -> {
            //Log.infoList(SchematicOption.list());
        });

        handler.register("bg-map", "Show all schematics on a map.", args -> {

        });

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
    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){
        //change teams
        handler.<Player>register("test", "ask fishbuilder", (args, player) -> {
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
    }


    public boolean active(){
        return state.rules.tags.getBool("buildergame") && !state.is(GameState.State.menu);
    }
}
