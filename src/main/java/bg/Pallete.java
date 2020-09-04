package bg;

import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.blocks.Floor;
import mindustry.world.blocks.StaticWall;

public enum Pallete {
    cavern(Blocks.ignarock, Blocks.metalFloor5,  Blocks.duneRocks),
    //desert(Blocks.sand,     Blocks.magmarock,   Blocks.saltRocks),
    fields(Blocks.grass,    Blocks.metalFloor, Blocks.rocks),
    snowey(Blocks.snow,     Blocks.metalFloor,  Blocks.snowrocks);

    public final Floor floor;
    public final Floor secondFloor;
    public final StaticWall wall;

    Pallete(Block floor, Block secondFloor, Block wall){
        this.floor = (Floor)floor;
        this.secondFloor = (Floor)secondFloor;
        this.wall = (StaticWall)wall;
    }
}
