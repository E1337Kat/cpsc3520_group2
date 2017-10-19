/*
 * Copyright (C) 2017 Ellie
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Entities;

import Main.Overworld;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Player extends InertialEntity {

    
    private static enum State  { LEVEL, JUMPING, FALLING, WALKING };
    private static enum Anim { WALK_LEFT, WALK_RIGHT, JUMP_LEFT, JUMP_RIGHT,
                               JUMP_CENTER, CROUCH_CENTER, STAND_CENTER,
                               STAND_LEFT, STAND_RIGHT, IDLE_LEFT, 
                               IDLE_RIGHT, IDLE_CENTER };
    
    private List<Entity> entities;
    
    private State state = State.LEVEL;
    private Anim anim = Anim.STAND_CENTER;
    
    private int jumpTime;
    private int jumpTimeTex;
    private int loc = 0;
    private List<Overworld.Pair<Integer, Integer>> locations;
    
    private boolean hasDoubleJump = false;


    public Player(float spriteSize) {
        super(spriteSize, 1.0f, "res/heck.png");
        xSpriteCoord = 0;
        ySpriteCoord = 2;
    }
    
    public void addLocations(List<Overworld.Pair<Integer, Integer>> locations) {
        this.locations = locations;
    }
    
    public void update(float delta, int loc) {
        super.update(delta);
        
        
        
        if (state == State.LEVEL) {
            anim = Anim.IDLE_CENTER;
            xSpriteCoord++;  
            
            if (xSpriteCoord >= 13)
                xSpriteCoord = 0;
            if (this.loc != loc) {
                state = State.WALKING;
            }
        
        }
        
        if (state == State.WALKING) {
            anim = Anim.WALK_RIGHT;
            xSpriteCoord++;  
            
            if (xSpriteCoord >= 9 && moving)
                xSpriteCoord = 0;
            if (!moving)
                xSpriteCoord = 0;
            
            super.translate(locations.get(loc).getL(), locations.get(loc).getR());
            state = State.LEVEL;
        }
        
        setAnim(anim);
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (state == State.LEVEL) {
            
            xSpriteCoord++;  
            
            if (xSpriteCoord >= 9 && moving)
                xSpriteCoord = 0;
            if (!moving)
                xSpriteCoord = 0;
            
            
            // Player is safe on ground
            this.hasDoubleJump = true;                      
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                anim = Anim.WALK_LEFT;
                super.translate((int)(-.50*delta), 0);
                // TODO translate left
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                anim = Anim.WALK_RIGHT;
                super.translate((int)(.50*delta), 0);
                // TODO translate right
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                anim = Anim.CROUCH_CENTER;
                // TODO translate down
            } else {
                anim = Anim.STAND_CENTER;
            }




            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && 
                    (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                        !Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                moving = false;
                jumpTime = 14;
                jumpTimeTex = jumpTime/7;
                state = State.JUMPING;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&
                    (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                        Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                moving = true;
                jumpTime = 14;
                jumpTimeTex = jumpTime/7;
                state = State.JUMPING;
            } 
            
        }
        
        if (state == State.JUMPING) {
            
            // Check direction for animations;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                anim = Anim.JUMP_LEFT;
                // TODO translate left
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                anim = Anim.JUMP_RIGHT;
                //translate right
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                anim = Anim.CROUCH_CENTER;
                // TODO crouch
            } else { // Neither left or right is held down.
                anim = Anim.JUMP_CENTER;
            }
            
            // Just jumping
            if (jumpTime <= 0) {
                state = State.FALLING;
            }
            jumpTime--;
            
            // Attempt to change animation frame smoothly.
            if((jumpTimeTex > 0) && (jumpTime%jumpTimeTex == 0)) {
                
                xSpriteCoord++;
                if (xSpriteCoord >= 7)
                    xSpriteCoord =0;
            }
            if (moving && anim == Anim.JUMP_LEFT) {
                super.translate((int)(-.50*delta), (int)(-0.50*delta));
            } else if (moving && anim == Anim.JUMP_RIGHT) {
                super.translate((int)(.50*delta), (int)(-0.50*delta));
            } else {
                super.translate(0, (int)(-0.50*delta));
            }
            
        }
        
        if (state == State.FALLING) {

            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))  {
                anim = Anim.JUMP_LEFT;
            } else  if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                anim = Anim.JUMP_RIGHT;
            } else if ((Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
                    || !(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT))) {
                anim = Anim.JUMP_CENTER;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
                anim = Anim.CROUCH_CENTER;
            } else {
                anim = Anim.STAND_CENTER;
            }

            // Check to see if player has a double jump still
            if (hasDoubleJump) {
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && 
                        (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                            !Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                    moving = false;
                    jumpTime = 10;
                    state = State.JUMPING;
                    this.hasDoubleJump = false;
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&
                        (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                            Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                    moving = true;
                    jumpTime = 10;
                    state = State.JUMPING;
                    this.hasDoubleJump = false;
                }


            }
            super.translate(0, (int)(.5 * delta));

            // check if player falls off the level
            if (super.getY()  > Display.getHeight() ) {
                super.setY(Display.getHeight()-super.getHeight());
                state = State.LEVEL;
            }
        
        }
        
        setAnim(anim);
    }
    
    public void setEntities(List entities) {
        this.entities = entities;
    }
    
    // override Entity method since we can answer this question
    @Override
    public boolean intersects(Entity other)
    {
        return super.intersects(other);
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.PLAYER;
    }

    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return super.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return super.getY();
    }
    
    private void setAnim(Anim a) {
        switch (a) {
            case JUMP_LEFT:
                moving = true;
                ySpriteCoord = 1;
                break;
            case JUMP_CENTER:
                moving = true;
                ySpriteCoord = 2;
                break;
            case JUMP_RIGHT:
                moving = true;
                ySpriteCoord = 3;
                break;
            case STAND_LEFT:
                moving = false;
                ySpriteCoord = 1;
                break;
            case STAND_CENTER:
                moving = false;
                ySpriteCoord = 2;
                break;
            case STAND_RIGHT:
                moving = false;
                ySpriteCoord = 3;
                break;
            case WALK_LEFT:
                moving = true;
                ySpriteCoord = 9;
                break;
            case WALK_RIGHT:
                moving = true;
                ySpriteCoord = 11;
                break;
            case IDLE_LEFT:
                
                moving = true;
                ySpriteCoord = 17;
                break;
            case IDLE_CENTER:
                moving = true;
                ySpriteCoord = 18;
                break;
            case IDLE_RIGHT:
                moving = true;
                ySpriteCoord = 19;
                break;
            case CROUCH_CENTER:
                ySpriteCoord = 20;
                break;
            default:
                moving = false;
                ySpriteCoord = 2;
                break;
        }
    }
    
}
