/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Entities.Background;
import Entities.Player;
import Entities.LevelEnd;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Ellie
 */
public class GameLevel extends Scene {

    
    
    private static Player player;
    private static LevelEnd end;
    private static Background level_bg;
    private boolean reachedEnd = false;
    private Level level;
    
    
    public GameLevel(String pngpath) {
        this.init(pngpath);
    }
    
    public GameLevel(String pngpath, Level l) {
        level = l;
        this.init(pngpath);
    }
    
    public Level getLevelType() {
        return level;
    }
    
    public boolean completedLevel() {
        return reachedEnd;
    }
    
    
    @Override
    protected SceneType getSceneType() {
        return SceneType.LEVEL;
    }
    
    @Override
    public boolean drawFrame(float delta) {
        
        
        player.update(delta);
        end.update(delta);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        level_bg.draw();
        player.draw();
        end.draw();
        
        if (player.intersects(end)) {
            reachedEnd = true;
            return false;
        } 

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    private void init(String pngpath) {
        level_bg = new Background(pngpath);
        player = new Player(64f);
        end = new LevelEnd();
    }
    
}
