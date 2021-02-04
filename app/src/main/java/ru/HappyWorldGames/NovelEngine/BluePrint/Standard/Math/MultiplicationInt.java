package ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;

public class MultiplicationInt extends BluePrintEditorObject {

    public MultiplicationInt(){
        super();

        setName("Multiplication(Int)");
        setDescription("*");

        addInputBluePrint(Variable.TYPE.Int);
        addInputBluePrint(Variable.TYPE.Int);

        addOutputBluePrint(Variable.TYPE.Int);
    }

    @Override
    public String toCode() {
        return getInputVariable(0) + " * " + getInputVariable(1);
    }
}
