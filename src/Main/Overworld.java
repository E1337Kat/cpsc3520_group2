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
package Main;

import Entities.Background;
import Entities.Player;

import java.util.LinkedList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Ellie
 */
public class Overworld extends Scene{
    
    /**
     * helper class that represents a pair of objects
     * In this class, It is an ordered Integer pair.
     * @param <L>
     * @param <R> 
     */
    public class Pair<L,R> {
        private L l;
        private R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }
        public L getL(){ return l; }
        public R getR(){ return r; }
        public void setL(L l){ this.l = l; }
        public void setR(R r){ this.r = r; }
    }
    
    
    private Player player;
    private List<Pair<Integer,Integer>> locations;
    private Background overworld_bg;
    
    private final List<GameLevel> levels;
    private int currentLevel;
    private static Scene nextScene;
   
    public Overworld() {
        this.init();
        levels = new LinkedList<>();
    }
    
    public Overworld(List<GameLevel> g) {
        this.init();
        levels = g;
        currentLevel = 0;
    }
    
    /**
     * sets the location of a level
     * @param x The Display x coord
     * @param y The Display y coord
     */
    public void addLocation (int x, int y) {
        locations.add( new Pair<>(x,y) );
    }
    
    public void locToPlayer() {
        player.addLocations(locations);
    }
    
    
    /**
     * checks to see if player finished level. If they did, they move on or win
     * If not, they stay on same level.
     * @return next scene to show.
     */
    @Override
    public Scene nextScene() {
        return nextScene;
    }
    
    @Override
    public boolean drawFrame(float delta) {
        
        overworld_bg.update(delta);
        player.update(delta, currentLevel);
        
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        overworld_bg.draw();
        player.draw();
        
          
        // Determine next Scene to display
        
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_RETURN:
                        if (levels.get(currentLevel).completedLevel()) {
                            currentLevel++;
                            // Go to next level
                            if (currentLevel < levels.size()) {
                                nextScene = levels.get(currentLevel);
                            } else {
                                // finished game
                                currentLevel--;
                                nextScene = null;
                            }
                        } else if ( !(levels.get(currentLevel).completedLevel())) {
                            // Stays on current level 
                            nextScene = levels.get(currentLevel);
                        } else {
                            // do nothing
                            nextScene = this;
                        }
                        return false;
                    case Keyboard.KEY_ESCAPE:
                        super.exit();
                        return false;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    @Override
    protected SceneType getSceneType() {
        return SceneType.OVERWORLD; 
    }
    
    private void init() {
        player = new Player(64f);
        overworld_bg = new Background("res/overworld.png");
        locations = new LinkedList<>();
    }
}
