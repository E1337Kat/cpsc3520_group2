/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.lwjgl.opengl.Display;

/**
 *
 * @author Ellie
 */
public class LevelEnd extends Entity {
    
    
    public LevelEnd () {
        super(Display.getWidth()-10, Display.getHeight()-20, 10, 20);
    }
    
}
