package bg;

import arc.struct.Array;
import arc.struct.ObjectMap;
import arc.util.Log;

import mindustry.game.Schematic;
import mindustry.game.Schematics;

import javax.sound.midi.SysexMessage;

import static bg.BuilderGame.*;

public class SchematicOption{
    private Schematics tester;
    //all the schematics
    ObjectMap<String, Schematic> allSchems = new ObjectMap<>();
    //custom schematics- new entries via server
    ObjectMap<String, Schematic> customSchems = new ObjectMap<>();
    //base schematics - hardcoded
    ObjectMap<String, Schematic> baseSchems = new ObjectMap<>();

    //current
    String currentSchem = null;

    public SchematicOption(){
        tester = new Schematics();
        int baseErrors = 0;
        for(String s: baseSchemsRaw){
            try {
                Schematic _schem = Schematics.readBase64(s);
                if(_schem == null) {
                    baseErrors++;
                }else if(_schem.tiles.size != 0){
                    baseSchems.put(s, _schem);
                }else{
                    baseErrors++;
                }
            }catch(Exception _){
                baseErrors++;
            }
        }
        if(baseErrors != 0){
            Log.info("BuilderGame<baseErrors>", baseErrors);
        }
    }
    /** loc = (x,y)*/
    public boolean checkSchematic(int[] loc){
        //TODO if false try currentschem with tags(name, unkown)
        System.out.println(tester.writeBase64(tester.create(loc[0], loc[1], loc[2], loc[3])));
        return currentSchem.equals(tester.writeBase64(tester.create(loc[0], loc[1], loc[2], loc[3])));
    }


    public void addSchematic(String inputSchem){
        //IMPORTANT: make schematic.name = unknown and size schemwidthxschemheight
        try{
            Schematic _schem = Schematics.readBase64(inputSchem);
            if(_schem == null){
                Log.err("BuilderGame<addSchem>", "invalid schematic <null>");
            }else if(_schem.tiles.size == 0){
                Log.err("BuilderGame<addSchem>", "schematic contains 0 blocks!");
            }else{
                customSchems.put(inputSchem, _schem);
                Log.info("BuilderGame<addSchem>", "succesfully added schematic");
            }
        }catch(Exception _){
            Log.err("BuilderGame<addSchem>", "invalid schematic <b64>");
        }
    }

    public void removeSchematic(String inputSchem){
        if(allSchems.containsKey(inputSchem)){
            allSchems.remove(inputSchem);
        }

        if(customSchems.containsKey(inputSchem)){
            allSchems.remove(inputSchem);
        }
    }

    public String printList(){
        StringBuilder sb = new StringBuilder();
        for(String _s: allSchems.keys()){
            sb.append(_s).append('\n');
        }
        return sb.toString();
    }
}
