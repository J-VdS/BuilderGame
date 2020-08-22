package bg;

import arc.struct.Array;
import arc.struct.ObjectMap;
import arc.util.Log;

import mindustry.game.Schematic;
import mindustry.game.Schematics;

import static bg.BuilderGame.*;

public class SchematicOption{
    //all the schems
    ObjectMap<String, Schematic> allSchems = new ObjectMap<>();
    //customschems - new entries via server
    ObjectMap<String, Schematic> customSchems = new ObjectMap<>();
    //baseschems - hardcoded
    ObjectMap<String, Schematic> baseSchems = new ObjectMap<>();

    public SchematicOption(){
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
            Log.err("BuilderGame<baseErrors>", baseErrors);
        }
    }

    public void addSchematic(String inputSchem){
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


}
