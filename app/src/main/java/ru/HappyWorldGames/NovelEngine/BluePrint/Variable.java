package ru.HappyWorldGames.NovelEngine.BluePrint;

import android.graphics.Color;

import androidx.annotation.NonNull;

public class Variable {
    public enum TYPE {
        Boolean("Boolean", Color.RED),
        Int("Int", Color.BLUE),
        Float("Float", Color.GREEN),
        String("String", Color.rgb(192, 0, 255)),
        Object("Object", Color.MAGENTA);

        final private String name;
        final private int color;
        TYPE(String name, int color){
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return name;
        }
        public int getColor(){ return color; }
    }

    final private TYPE type;
    private Object value;

    public Variable(@NonNull TYPE type){
        this.type = type;

        switch (type){
            case Boolean:
                setValue(false);
                break;
            case Int:
            case Float:
                setValue(0);
            case String:
                setValue("");
            case Object:
                setValue(new Object());
        }
    }

    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue(){
        return value;
    }
    public TYPE getType(){
        return type;
    }
}
