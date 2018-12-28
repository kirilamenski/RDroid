package com.ansgar.rdroidpc.utils.listeners;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

    private VideoFrame frame;

    public KeyboardListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed" + e.getKeyChar());
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.execute("adb shell input text " + e.getKeyChar());
        frame.getChimpDevice().type("Hello, world!");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("released" + e.getKeyCode());
    }
}
