package bg;

import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.blocks.Floor;
import mindustry.world.blocks.StaticWall;

public enum Pallete {
    cavern(Blocks.darksand,    Blocks.duneRocks),
    //spored(Blocks.shale,       Blocks.sporerocks),
    desert(Blocks.sand,        Blocks.saltRocks),
    //forged(Blocks.hotrock,     Blocks.cliffs),
    fields(Blocks.grass,       Blocks.rocks),
    snowey(Blocks.snow,        Blocks.snowrocks);

    public final Floor floor;
    public final StaticWall wall;

    Pallete(Block floor, Block wall){
        this.floor = (Floor)floor;
        this.wall = (StaticWall)wall;
    }
}
