
package Bomberman.Entity.Tiles;

import Bomberman.Animations.Sprite;
import Bomberman.Entity.Entity;
import Bomberman.Entity.Boundedbox.RectBoundedBox;

public class Wall extends Tile {
    public Wall(int x, int y) {
        super(x, y);
        scale = 3.2;
        sprite = new Sprite(this, 16, 0, 0, 225, 1, width, height, getScale());
        boundedBox = new RectBoundedBox(positionX, positionY, (int) (width * (getScale() + 0.9)), (int) (height * (getScale() + 0.9)));
    }

    @Override
    public boolean isCollideEntity(Entity b) {
        return true;
    }

    @Override
    public boolean remove() {
        return false;
    }
}
