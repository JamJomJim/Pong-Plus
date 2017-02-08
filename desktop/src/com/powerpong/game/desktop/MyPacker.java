package com.powerpong.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main (String[] args) throws Exception {
        TexturePacker.process("C:\\Users\\Nick\\Documents\\Intellij Projects\\PowerPong\\android\\assets\\unpacked",
                "C:\\Users\\Nick\\Documents\\Intellij Projects\\PowerPong\\android\\assets",
                "testStanding");
        //first string is source directory, second string is output directory, third string is name of the spritesheet and atlas
    }
}