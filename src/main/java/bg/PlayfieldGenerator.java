package bg;

import arc.struct.StringMap;
import mindustry.maps.Map;
import mindustry.maps.generators.Generator;
import mindustry.world.Tile;

import static mindustry.Vars.world;
import static bg.BuilderGame.*;

public class PlayfieldGenerator extends Generator {
    //mapsize
    static int border = 5;
    static int schemWidth = 25;
    static int schemHeight = 10;
    static int distPlayer = 5;
    static int betweenPlayer = 10;

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
                        if(border + i*schemWidth+(i-1)*betweenPlayer <= x && x < border + i*(schemWidth+betweenPlayer)){
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
}
