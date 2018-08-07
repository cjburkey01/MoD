package com.cjburkey.mod.core;

import org.joml.Vector2f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Input {
    
    private static Set<Key> firstKeys = new HashSet<>();
    private static Set<MouseButton> firstBtns = new HashSet<>();
    private static Map<Key, Boolean> keys = new HashMap<>();
    private static Map<MouseButton, Boolean> btns = new HashMap<>();
    private static final Vector2f cursorPos = new Vector2f();
    private static final Vector2f prevCursorPos = new Vector2f();
    private static final Vector2f cursorDelta = new Vector2f();
    
    static void onKeyPress(int code) {
        Key pressed = Key.getKey(code);
        if (!pressed.equals(Key.UNKNOWN)) {
            keys.put(pressed, true);
            firstKeys.add(pressed);
        }
    }
    
    static void onKeyRelease(int code) {
        Key released = Key.getKey(code);
        if (keys.containsKey(released)) {
            keys.put(released, false);
        }
    }
    
    static void onMousePress(int code) {
        MouseButton pressed = MouseButton.getButton(code);
        if (!pressed.equals(MouseButton.UNKNOWN)) {
            btns.put(pressed, true);
            firstBtns.add(pressed);
        }
    }
    
    static void onMouseRelease(int code) {
        MouseButton released = MouseButton.getButton(code);
        if (btns.containsKey(released)) {
            btns.put(released, false);
        }
    }
    
    static void onUpdate() {
        firstKeys.clear();
        firstBtns.clear();
        Set<Key> keysToRemove = new HashSet<>();
        Set<MouseButton> btnsToRemove = new HashSet<>();
        for (Map.Entry<Key, Boolean> key : keys.entrySet()) {
            if (!key.getValue()) {
                keysToRemove.add(key.getKey());
            }
        }
        for (Key key : keysToRemove) {
            keys.remove(key);
        }
        for (Map.Entry<MouseButton, Boolean> btn : btns.entrySet()) {
            if (!btn.getValue()) {
                btnsToRemove.add(btn.getKey());
            }
        }
        for (MouseButton btn : btnsToRemove) {
            btns.remove(btn);
        }
    }
    
    static void onMouseMove(float x, float y) {
        cursorPos.set(x, y);
        cursorPos.sub(prevCursorPos, cursorDelta);
        prevCursorPos.set(cursorPos);
    }
    
    public static boolean getKey(Key key) {
        return keys.containsKey(key) && keys.get(key);
    }
    
    public static boolean getKeyDown(Key key) {
        return firstKeys.contains(key);
    }
    
    public static boolean getKeyUp(Key key) {
        return keys.containsKey(key) && !keys.get(key);
    }
    
    public static boolean getMouseButton(MouseButton btn) {
        return btns.containsKey(btn) && btns.get(btn);
    }
    
    public static boolean getMouseButtonDown(MouseButton btn) {
        return firstBtns.contains(btn);
    }
    
    public static boolean getMouseButtonUp(MouseButton btn) {
        return btns.containsKey(btn) && !btns.get(btn);
    }
    
    public static Vector2f getCursorPos() {
        return new Vector2f(cursorPos);
    }
    
    public static Vector2f getCursorDelta() {
        return new Vector2f(cursorDelta);
    }
    
}