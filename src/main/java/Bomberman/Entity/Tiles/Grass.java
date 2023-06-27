package Bomberman.Entity.Tiles;

import Bomberman.Animations.Sprite;
import Bomberman.GlobalVariables.GlobalVariables;
import Bomberman.Entity.Entity;
import Bomberman.Entity.Boundedbox.RectBoundedBox;

public class Grass extends Tile {

    public Grass(int x, int y) {
        super(x,y);
        scale = 3.2;
        layer = -3;
        sprite = new Sprite(this, 16, 0, 0, 245, 1, width, height, getScale());
        boundedBox = new RectBoundedBox(positionX, positionY, (int) (width * (getScale() + 0.4)), (int) (height * (getScale() + 0.4)));
    }
    @Override
    public boolean isCollideEntity(Entity b) {
        return false;
    }
    @Override
    public void setOffset() {
        this.positionX -= GlobalVariables.offSet;
        this.boundedBox.setOffset();
    }
    @Override
    public boolean remove() {
        return false;
    }
    @Override
    public boolean isCollidePlayer() {
        return true;
    }

}
