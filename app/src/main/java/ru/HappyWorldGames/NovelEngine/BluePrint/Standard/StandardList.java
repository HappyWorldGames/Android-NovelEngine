package ru.HappyWorldGames.NovelEngine.BluePrint.Standard;

import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.DivisionFloat;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.DivisionInt;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.MultiplicationFloat;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.MultiplicationInt;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.SubtractionFloat;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.SubtractionInt;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.AdditionFloat;
import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.Math.AdditionInt;

public class StandardList {
    final public static String[] names = {
            "Addition(Int)", "Addition(Float)",
            "Subtraction(Int)", "Subtraction(Float)",
            "Multiplication(Int)", "Multiplication(Float)",
            "Division(Int)", "Division(Float)",
            "GetValue"
    };
    final public static Class[] classes = {
            AdditionInt.class, AdditionFloat.class,
            SubtractionInt.class, SubtractionFloat.class,
            MultiplicationInt.class, MultiplicationFloat.class,
            DivisionInt.class, DivisionFloat.class,
            GetValue.class
    };

    public static Object getClassInit(int pos) throws InstantiationException, IllegalAccessException {
        return classes[pos].newInstance();
    }
}
