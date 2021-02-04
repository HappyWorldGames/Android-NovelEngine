package ru.HappyWorldGames.NovelEngine.BluePrint.Standard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorObject;
import ru.HappyWorldGames.NovelEngine.BluePrint.Variable;
import ru.HappyWorldGames.NovelEngine.EditorActivity;
import ru.HappyWorldGames.NovelEngine.R;

public class GetValue extends BluePrintEditorObject {

    private Variable.TYPE type = Variable.TYPE.Boolean;

    public GetValue(){
        super();

        setName("GetValue");
        setDescription(type.getName() + ":" + 0);

        addOutputBluePrint(Variable.TYPE.Boolean);
    }

    @Override
    public String toCode() {
        return String.valueOf(getOutputVariable(0).getValue());
    }

    @Override
    public AlertDialog crtAlertDialog(Context c){
        LinearLayout main = new LinearLayout(c);
        final Spinner spinner = new Spinner(c);
        spinner.setAdapter(new ArrayAdapter(c, android.R.layout.simple_spinner_dropdown_item, Variable.TYPE.values()));
        final EditText editText = new EditText(c);
        editText.setText("0");

        main.addView(spinner);
        main.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Get Value");
        builder.setView(main);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = (Variable.TYPE)spinner.getSelectedItem();
                Variable variable = new Variable(type);
                variable.setValue(editText.getText().toString());
                setOutputVariable(0, variable);
                setDescription(type.getName() + ":" + getOutputVariable(0).getValue());
            }
        });
        return builder.create();
    }
}
