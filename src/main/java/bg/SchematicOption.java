package bg;

import arc.struct.Array;
import arc.struct.ObjectMap;
import arc.util.Log;

import arc.util.Structs;
import mindustry.game.Schematic;
import mindustry.game.Schematics;


import java.util.HashMap;

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
    String currentSchemRaw = "";
    Schematic currentSchem = null;
    HashMap<Short, HashMap<Short, Schematic.Stile>> currentSchemMap = new HashMap<>();
    //{x:{y: Stile}

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
        if(currentSchem == null) return false;
        Schematic found = tester.create(loc[0], loc[1], loc[2], loc[3]);

        System.out.println(tester.writeBase64(found));
        //System.out.println(currentSchem.toString());
        if(found==null) return false;
        if(currentSchem.tiles.size != found.tiles.size){
            return false;
        }
        for(Schematic.Stile st: found.tiles){
            if(currentSchemMap.get(st.x) == null){
                System.out.println("NO x");
                return false;
            }
            printComp(st, currentSchemMap.get(st.x).get(st.y));
            if(!compareStiles(st, currentSchemMap.get(st.x).get(st.y))){
                return false;
            }
        }
        return true;
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

    public void random() {
        this.currentSchemRaw = Structs.random(baseSchemsRaw);
        this.currentSchem = baseSchems.get(this.currentSchemRaw);
        this.currentSchemMap.clear();
        Log.info("START PRINT");
        HashMap<Short, Schematic.Stile> obj = new HashMap<>();
        for(Schematic.Stile st: this.currentSchem.tiles){
            obj.put(st.y, st);
            this.currentSchemMap.put(st.x, (HashMap<Short, Schematic.Stile>) obj.clone());
            obj.clear();
        }

        for(short x:this.currentSchemMap.keySet()){
                System.out.println(x);
                System.out.println(this.currentSchemMap.get(x));

        };
        Log.info("END PRINT");
    }

    public Schematic getCurrentSchemRaw(){
        return baseSchems.get(currentSchemRaw);
    }

    private boolean compareStiles(Schematic.Stile a, Schematic.Stile b){
        if(b == null){
            return false;
        }
        return (a.block.name.equals(b.block.name));// && a.config == b.config && a.rotation == b.rotation); //(a.config == b.config)
    }

    private void printComp(Schematic.Stile a, Schematic.Stile b){
        System.out.println(Short.toString(a.x)+' '+Short.toString(b.x));
        System.out.println(Short.toString(a.y)+' '+Short.toString(b.y));
        System.out.println(a.block.name + ' ' +b.block.name);
    }
}
