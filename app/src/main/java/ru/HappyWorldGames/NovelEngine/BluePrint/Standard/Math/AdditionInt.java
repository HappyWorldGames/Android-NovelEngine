package ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;

public class AdditionInt extends BluePrintEditorObject {

    public AdditionInt(){
        super();

        setName("Addition(Int)");
        setDescription("+");

        addInputBluePrint(Variable.TYPE.Int);
        addInputBluePrint(Variable.TYPE.Int);

        addOutputBluePrint(Variable.TYPE.Int);
    }
/*
    @Override
    public void make() {
        getOutputVariable(1).setValue((int)getInputVariable(1).getValue() + (int)getInputVariable(2).getValue());
    }*/
    @Override
    public String toCode() {
        return getInputVariable(0) + " + " + getInputVariable(1);
    }
}
