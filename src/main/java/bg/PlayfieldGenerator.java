package bg;

import arc.struct.StringMap;
import mindustry.content.Blocks;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.maps.Map;
import mindustry.maps.generators.Generator;
import mindustry.world.Tile;
import mindustry.gen.*;

import static mindustry.Vars.world;
import static bg.BuilderGame.*;

public class PlayfieldGenerator extends Generator {
    //mapsize
    static int border = 5;
    static int schemWidth = 25;
    static int schemHeight = 10;
    static int betweenPlayer = 5;

    public PlayfieldGenerator(){
        super(5*schemWidth+4*betweenPlayer+2*border, 2*schemHeight+betweenPlayer+2*border);
    }

    @Override
    public void generate(Tile[][] tiles){
        System.out.println(pallete.name());
        // init tiles (1x1)
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles[x][y] = new Tile(x, y);
                //tiles[x][y].setFloor(pallete.floor);
                tiles[x][y].setFloor((x%5==0)?pallete.secondFloor:pallete.floor);
            }
        }

        //walls
        int bsh = border+schemHeight;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++) {
                //border
                if (x < border || width - border <= x) {tiles[x][y].setBlock(pallete.wall);}
                else if(y<border || height-border <= y){tiles[x][y].setBlock(pallete.wall);}
                //between players
                else if(bsh<=y && y < bsh+betweenPlayer){tiles[x][y].setBlock(pallete.wall);}
                else {

                    for (int i = 1; i < maxPlayers; i++) {
                        if (border + i * schemWidth + (i - 1) * betweenPlayer <= x && x < border + i * (schemWidth + betweenPlayer)) {
                            tiles[x][y].setBlock(pallete.wall);
                        }
                    }
                }
            }
        }
        world.setMap(new Map(StringMap.of("name", mapnamepre + pallete.name())));
    }

    public int[][] getSpawns(){
        int[][] ret = new int[maxPlayers][2];
        int sw = Math.round(schemWidth/2);
        int sh = Math.round(schemHeight/2) + border;
        for(int i=0; i<maxPlayers; i++){
            ret[i][0] = border + sw + i*(schemWidth+betweenPlayer);
            ret[i][1] = sh;
        }
        return ret;
    }

    public int[][] getPlayerZones(){
        int[][] ret = new int[maxPlayers][2];
        for(int i=0; i<maxPlayers; i++){
            ret[i][0] = border + i*(schemWidth+betweenPlayer);
            ret[i][1] = border;
        }
        return ret;
    }

    //O intensive!
    public int[] getPlayerZone(int id){
        int[] ret = new int[]{border + (id-6)*(schemWidth+betweenPlayer),
                              border,
                              border + (id-5)*(schemWidth+betweenPlayer) - betweenPlayer - 1,
                              border + schemHeight - 1};
        for(int i: ret){
            System.out.println(i);
        }
        return ret;
    };

    public void spawnSchematic(Schematic genSchem){
        int y1 = border+betweenPlayer+schemHeight;
        int x1;
        for(Schematic.Stile st: genSchem.tiles){
            for(int i=0; i<maxPlayers; i++){
                x1 = border + i*(schemWidth+betweenPlayer);
                Call.onConstructFinish(world.tile(x1+st.x, y1+st.y), st.block, 0, st.rotation, Team.green, true);
                if(st.config!=0){
                    //TODO
                }
            }
        }
    }

    public void resetField(){
        Tile t;
        for(int x=border; x<=width-border; x++){
            for(int y=border; y<=height-border; y++){
                t = world.tile(x, y);
                if(!t.block().isStatic() && t.block() != Blocks.air){
                    Call.onDeconstructFinish(t, t.block(),0);
                }
            }
        }
    }
}
