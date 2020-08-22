package bg;

import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.blocks.Floor;
import mindustry.world.blocks.StaticWall;

public enum Pallete {
    cavern(Blocks.darksand, Blocks.metalFloor,  Blocks.duneRocks),
    desert(Blocks.sand,     Blocks.magmarock,   Blocks.saltRocks),
    fields(Blocks.grass,    Blocks.metalFloor5, Blocks.rocks),
    snowey(Blocks.snow,     Blocks.metalFloor,   Blocks.snowrocks);

    public final Floor floor;
    public final Floor secondFloor;
    public final StaticWall wall;

    Pallete(Block floor, Block secondfloor, Block wall){
        this.floor = (Floor)floor;
        this.secondFloor = (Floor)secondfloor;
        this.wall = (StaticWall)wall;
    }
}
