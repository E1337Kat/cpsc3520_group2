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
public class Background extends Entity {
    
    
    public Background (String pngpath) {
        super(pngpath, Display.getWidth(),0,0);
    }
}
