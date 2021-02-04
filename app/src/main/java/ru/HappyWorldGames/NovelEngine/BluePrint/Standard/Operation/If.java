package ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Operation;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;

public class If extends BluePrintEditorObject {

    public If(){
        super();

        setName("Addition(Float)");
        setDescription("+");

        addInputBluePrint(Variable.TYPE.Float);
        addInputBluePrint(Variable.TYPE.Float);

        addOutputBluePrint(Variable.TYPE.Float);
    }

    @Override
    public String toCode() {
        return getInputVariable(0) +  + getInputVariable(1);
    }
}
