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
        // init tiles (1x1)
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles[x][y] = new Tile(x, y);
                tiles[x][y].setFloor(pallete.floor);
            }
        }

        //walls
        int bsh = border+schemHeight;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++) {
                //border
                if (x < border || width - border <= x) {tiles[x][y].setBlock(pallete.wall);}
                else if(y<border || height-border <= y){tiles[x][y].setBlock(pallete.wall);}
                else if(bsh<=y && y < bsh+betweenPlayer){tiles[x][y].setBlock(pallete.wall);}
                else {
                    //between players
                    for (int i = 1; i < maxPlayers; i++) {
                        if(border + i*schemWidth+(i-1)*betweenPlayer <= x && x < border + i*(schemWidth+betweenPlayer)){
                            System.out.println(x);
                            System.out.println(y);
                            tiles[x][y].setBlock(pallete.wall);
                        }
                    }
                }
            }
        }

        world.setMap(new Map(StringMap.of("name", mapnamepre + pallete.name())));
    }
}
