package ru.HappyWorldGames.NovelEngine.BluePrint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ru.HappyWorldGames.NovelEngine.Editor.EditorObject;
import ru.HappyWorldGames.NovelEngine.EditorActivity;
import ru.HappyWorldGames.NovelEngine.R;

public abstract class BluePrintEditorObject extends EditorObject {

    private static AlertDialog alertDialog;

    final private Paint inputPaint, outputPaint;
    public BluePrintEditorObject(){
        super();

        inputPaint = new Paint();
        inputPaint.setTextSize(InputObject.size);
        inputPaint.setColor(Color.BLUE);
        outputPaint = new Paint();
        outputPaint.setTextSize(OutputObject.size);
        outputPaint.setColor(Color.BLUE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i = 0; i < getInputs().size(); i++)
            canvas.drawText(((BluePrintInputObject)getInputs().get(i)).getName(), getX() + InputObject.size, getY() + ((i + i) * InputObject.size) + InputObject.size + InputObject.size, inputPaint);

        for(int i = 0; i < getOutputs().size(); i++) {
            String name = ((BluePrintOutputObject) getOutputs().get(i)).getName();
            canvas.drawText(name, getX() + getWidth() - (OutputObject.size + outputPaint.measureText(name)), getY() + ((i + i) * OutputObject.size) + OutputObject.size + OutputObject.size, outputPaint);
        }
    }

    @Override
    public boolean click() {
        showAlertDialog();
        return super.click();
    }

    //public abstract void make();
    public abstract String toCode();

    public Variable getOutputVariable(int pos){
        return getOutputBluePrint(pos).getVariable();
    }
    public void setOutputVariable(int pos, Variable variable){
        getOutputBluePrint(pos).setVariable(variable);
    }
    public Variable getInputVariable(int pos){
        return getInputBluePrint(pos).getOutputObjectBluePrint().getVariable();
    }
    public void setInputVariable(int pos, Variable variable){
        getInputBluePrint(pos).getOutputObjectBluePrint().setVariable(variable);
    }

    public BluePrintOutputObject getOutputBluePrint(int pos){
        return (BluePrintOutputObject)getOutput(pos);
    }
    public BluePrintInputObject getInputBluePrint(int pos){
        return (BluePrintInputObject) getInput(pos);
    }

    public void addInputBluePrint(Variable.TYPE acceptType){
        addInput(new BluePrintInputObject(this, acceptType));
    }
    public void addOutputBluePrint(Variable.TYPE type){
        addOutput(new BluePrintOutputObject(this, type));
    }

    public static class BluePrintInputObject extends InputObject {

        private String nameInput = "input";

        private Variable variable;

        public BluePrintInputObject(EditorObject editorObject, Variable.TYPE acceptType) {
            super(editorObject);
            setVariableType(acceptType);
        }

        public void setVariableType(Variable.TYPE type){
            variable = type != null ? new Variable(type) : null;
            setInputColor(type != null ? type.getColor() : Color.GRAY);
        }
        public Variable getVariable(){
            return variable;
        }

        @Override
        public void setOutputObject(OutputObject bluePrintOutputObject) {
            BluePrintOutputObject outputObject = (BluePrintOutputObject)bluePrintOutputObject;
            if(outputObject == null) super.setOutputObject(null);
            else if((outputObject.getVariable() == null && getVariable().getType() == null) || (outputObject.getVariable() != null && outputObject.getVariable().getType().equals(getVariable().getType()))){
                if(outputObject.getBluePrintInputObject() == null) {
                    if(outputObject.getVariable() == null) outputObject.setBluePrintInputObject(this);
                    super.setOutputObject(bluePrintOutputObject);
                }
            }
        }

        public String getName(){
            return nameInput;
        }
        public void setName(String name){
            this.nameInput = name;
        }
        public BluePrintOutputObject getOutputObjectBluePrint(){
            return (BluePrintOutputObject)getOutputObject();
        }
    }
    public static class BluePrintOutputObject extends OutputObject {

        private String nameOutput = "output";

        private Variable variable;
        private BluePrintInputObject bluePrintInputObject;

        public BluePrintOutputObject(EditorObject editorObject, Variable.TYPE type) {
            super(editorObject);
            setVariableType(type);
        }

        public String getName(){
            return nameOutput;
        }
        public void setName(String name){
            this.nameOutput = name;
        }

        public void setVariable(Variable variable){
            this.variable = variable;
            setOutputColor(variable != null ? variable.getType().getColor() : Color.GRAY);
        }
        public void setVariableType(Variable.TYPE type){
            variable = type != null ? new Variable(type) : null;
            setOutputColor(type != null ? type.getColor() : Color.GRAY);
        }
        public Variable getVariable(){
            return variable;
        }

        public void setBluePrintInputObject(BluePrintInputObject bluePrintInputObject){
            this.bluePrintInputObject = bluePrintInputObject;
        }
        public BluePrintInputObject getBluePrintInputObject(){
            return bluePrintInputObject;
        }

    }

    public AlertDialog crtAlertDialog(Context c){
        ScrollView scrollView = new ScrollView(c);
        LinearLayout main = new LinearLayout(c);
        final List<EditText> editTexts = new ArrayList<>();

        for(InputObject inputObject : getInputs()){
            BluePrintInputObject bluePrintInputObject = (BluePrintInputObject)inputObject;
            if(bluePrintInputObject == null) continue;

            final EditText editText = new EditText(c);
            editText.setText(String.valueOf(bluePrintInputObject.getVariable().getValue()));
            if(bluePrintInputObject.getOutputObjectBluePrint() != null) editText.setEnabled(false);

            main.addView(editText);
            editTexts.add(editText);
        }

        scrollView.addView(main);

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Get Value");
        builder.setView(scrollView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < editTexts.size(); i++){
                    EditText editText = editTexts.get(i);
                    getInputBluePrint(i).getVariable().setValue(editText.getText().toString());
                }
            }
        });
        return builder.create();
    }
    public void showAlertDialog(){
        alertDialog = crtAlertDialog(EditorActivity.context);
        alertDialog.show();
    }
}
