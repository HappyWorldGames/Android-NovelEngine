package ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;

public class SubtractionInt extends BluePrintEditorObject {

    public SubtractionInt(){
        super();

        setName("Subtraction(Int)");
        setDescription("-");

        addInputBluePrint(Variable.TYPE.Int);
        addInputBluePrint(Variable.TYPE.Int);

        addOutputBluePrint(Variable.TYPE.Int);
    }

    @Override
    public String toCode() {
        return getInputVariable(0) + " - " + getInputVariable(1);
    }
}
