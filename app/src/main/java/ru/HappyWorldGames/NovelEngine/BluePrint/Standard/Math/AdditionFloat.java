package ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;

public class AdditionFloat extends BluePrintEditorObject {

    public AdditionFloat(){
        super();

        setName("Addition(Float)");
        setDescription("+");

        addInputBluePrint(Variable.TYPE.Float);
        addInputBluePrint(Variable.TYPE.Float);

        addOutputBluePrint(Variable.TYPE.Float);
    }

    @Override
    public String toCode() {
        return getInputVariable(0) + " + " + getInputVariable(1);
    }
}
