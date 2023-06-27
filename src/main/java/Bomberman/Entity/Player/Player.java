package Bomberman.Entity.Player;

import Bomberman.Renderer;
import Bomberman.Animations.PlayerAnimations;
import Bomberman.Animations.Sprite;
import Bomberman.GlobalVariables.Direction;
import Bomberman.GlobalVariables.GlobalVariables;
import Bomberman.Entity.Enemy.Balloom;
import Bomberman.Entity.Entity;
import Bomberman.Entity.Tiles.BombPowerup;
import Bomberman.Entity.Tiles.FlamePowerup;
import Bomberman.Entity.Tiles.Portal;
import Bomberman.Entity.Tiles.SpeedPowerup;
import Bomberman.Entity.MovingEntity;
import Bomberman.Entity.Boundedbox.RectBoundedBox;
import Bomberman.Entity.BombnFlame.Bomb;
import Bomberman.Entity.BombnFlame.Flame;
import Bomberman.Scene.Board;

import java.util.Date;

import static Bomberman.GlobalVariables.GlobalVariables.Level;
import static Bomberman.GlobalVariables.GlobalVariables.passLevel;

public class Player extends MovingEntity {

    public static int step = 3;
    public static int bombCount = 1;

    boolean isAlive = true;
    boolean disappear = false;

    Date dieTime;
    Direction currentDirection;
    PlayerAnimations playerAnimations;

    public Player(int posX, int posY) {
        init(posX, posY);
        layer = 0;
    }

    private void init(int x, int y) {
        playerAnimations = new PlayerAnimations(this, 2.2);
        positionX = x;
        positionY = y;
        scale = 2;
        boundedBox = new RectBoundedBox(positionX + (int) (GlobalVariables.PLAYER_WIDTH),
                positionY + (int) (GlobalVariables.PLAYER_WIDTH),
                (int) (GlobalVariables.PLAYER_WIDTH * (getScale() - 0.6)),
                (int) (GlobalVariables.PLAYER_HEIGHT * (getScale() - 0.8))
        );
        sprite = playerAnimations.getPlayerIdleSprite();
    }


    private void setSprite(Sprite s) {
        if (s != null) {
            sprite = s;
        }
    }

    @Override
    public void setOffset() {
        this.positionX -= GlobalVariables.offSet;
        this.boundedBox.setOffset();
    }

    public boolean isCollideEntity(Entity b) {
        RectBoundedBox otherEntityBoundary = b.getBoundingBox();
        return boundedBox.checkCollision(otherEntityBoundary);
    }

    public void render() {
        if (sprite != null && isAlive) {
            Renderer.playAnimation(sprite);
        }
        if (!isAlive) {
            Renderer.playAnimation(playerAnimations.getPlayerDying());
            if (new Date().getTime() > (570 + dieTime.getTime())) {
                disappear = true;
                GlobalVariables.NewGame = true;
            }
        }
    }

    public void die() {
        isAlive = false;
        dieTime = new Date();
    }

    private boolean checkCollisions(int x, int y) {
        boundedBox.setPosition(x, y);
        for (Entity e : Board.getEntities()) {
            if (e instanceof Portal && isCollideEntity(e) && Board.enemy == 0) {
                Level = (Level % 4) + 1;
                GlobalVariables.NewGame = true;
                passLevel = true;
            } else {
                if (e instanceof FlamePowerup && isCollideEntity(e)) {
                    Bomb.radius++;
                    ((FlamePowerup) e).checkCollision(true);
                }
                if (e instanceof BombPowerup && isCollideEntity(e)) {
                    bombCount++;
                    ((BombPowerup) e).checkCollision(true);
                }
                if (e instanceof SpeedPowerup && isCollideEntity(e)) {
                    step++;
                    ((SpeedPowerup) e).checkCollision(true);
                }
                if (e instanceof Bomb) {
                    boolean bol1 = Math.abs(this.getPositionY() - e.getPositionY()) < 42;
                    boolean bol2 = Math.abs(this.getPositionX() - e.getPositionX()) < 42;
                    if (bol1 && bol2 && ((Bomb) e).CollidedPlayer == false && e.isCollidePlayer() == true) {
                        ((Bomb) e).CollidedPlayer = true;
                    }
                    if (!bol1 || !bol2 && ((Bomb) e).CollidedPlayer == true) {
                        ((Bomb) e).PlayerCollisionFriendly = false;
                    }
                }
                if (!(e instanceof Balloom) && e != this && isCollideEntity(e) && !e.isCollidePlayer()) {
                    boundedBox.setPosition(positionX, positionY);
                    return true;
                }
            }
        }
        boundedBox.setPosition(positionX, positionY);
        return false;
    }

    public boolean remove() {
        if (isAlive) {
            for (Entity e : Board.getEntities()) {
                if ((e instanceof Flame && ((Flame) e).getFlameState()) || e instanceof Balloom) {
                    if (isCollideEntity(e)) {
                        die();
                        break;
                    }
                }
            }
        }
        return disappear;
    }

    public boolean updatePosition() {
        if (getPositionX() - 47 < 0 && currentDirection == Direction.LEFT) {
            GlobalVariables.offSet = -96;
            return true;
        }
        if (getPositionX() - 864 > 0 && currentDirection == Direction.RIGHT) {
            GlobalVariables.offSet = 96;
            return true;
        } else {
            GlobalVariables.CameraMoving = false;
        }
        return false;
    }

    public void move(int steps, Direction direction) {
        if (isAlive) {
            if (steps == 0) {
                setSprite(playerAnimations.getPlayerIdleSprite());
                GlobalVariables.CameraMoving = false;
                return;
            } else {
                switch (direction) {
                    case UP:
                        if (!checkCollisions(positionX, positionY - steps)) {
                            positionY -= steps;
                            setSprite(playerAnimations.getMoveUpSprite());
                            currentDirection = Direction.UP;
                        }
                        break;
                    case DOWN:
                        if (!checkCollisions(positionX, positionY + steps)) {
                            positionY += steps;
                            setSprite(playerAnimations.getMoveDownSprite());
                            currentDirection = Direction.DOWN;
                        }
                        break;
                    case LEFT:
                        if (!checkCollisions(positionX - steps, positionY)) {
                            positionX -= steps;
                            setSprite(playerAnimations.getMoveLeftSprite());
                            currentDirection = Direction.LEFT;
                        }
                        break;
                    case RIGHT:
                        if (!checkCollisions(positionX + steps, positionY)) {
                            positionX += steps;
                            setSprite(playerAnimations.getMoveRightSprite());
                            currentDirection = Direction.RIGHT;
                        }
                        break;
                    default:
                        setSprite(playerAnimations.getPlayerIdleSprite());
                }
            }
        }
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        boundedBox.setPosition(positionX, positionY);
        return boundedBox;
    }

    public boolean isCollidePlayer() {
        return true;
    }

    public boolean hasMoreBombs() {
        return bombCount > 0;
    }

    public void incrementBombCount() {
        bombCount++;
    }

    public void decrementBombCount() {
        bombCount--;
    }
}
