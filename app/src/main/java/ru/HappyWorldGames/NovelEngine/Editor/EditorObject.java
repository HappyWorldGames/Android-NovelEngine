package ru.HappyWorldGames.NovelEngine.Editor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.HappyWorldGames.NovelEngine.EditorActivity;

public class EditorObject {
    private Point myPoint;

    final private List<InputObject> inputs = new ArrayList<>();
    final private List<OutputObject> outputs = new ArrayList<>();

    public boolean isDrawLine = false;
    private OutputObject selectedOutputObject;
    final public float[] drawLinePoints = new float[4];

    final private Paint borderPaint, linePaint, textPaint;
    private String name, description;

    private float x, y;
    private int width, height; // 160x80 px
    private float lastX, lastY;

    public List<InputObject> getInputs(){
        return inputs;
    }
    public List<OutputObject> getOutputs(){
        return outputs;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public EditorObject(){
        setName("Name");
        setDescription("Description");

        setWidth(160);
        setHeight(80);

        borderPaint = new Paint();
        borderPaint.setColor(Color.LTGRAY);
        linePaint = new Paint();
        linePaint.setColor(Color.YELLOW);
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
    }

    public void draw(Canvas canvas) {
        for (InputObject inputObject : inputs)
            inputObject.drawLine(canvas);

        if(isDrawLine) canvas.drawLine(drawLinePoints[0], drawLinePoints[1], drawLinePoints[2], drawLinePoints[3], linePaint);

        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), borderPaint);
        canvas.drawText(getName(), getX() + (float)getWidth() / 2 - textPaint.measureText(name) / 2, getY() + 20 + textPaint.getTextSize() / 2, textPaint);
        canvas.drawText(getDescription(), getX() + (float)getWidth() / 2 - textPaint.measureText(description) / 2, getY() + (float)getHeight() / 2 + textPaint.getTextSize() / 2 + 20, textPaint);

        for(int i = 0; i < inputs.size(); i++)
            inputs.get(i).draw(canvas);

        for(int i = 0; i < outputs.size(); i++)
            outputs.get(i).draw(canvas);
    }

    public boolean touch(@NonNull MotionEvent event, float normX, float normY){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = normX;
                lastY = normY;

                for(int i = 0; i < Math.max(inputs.size(), outputs.size()); i++) {
                    if(i < inputs.size() && EditorView.isTouch(normX, normY, getX(), getY() + ((i + i) * InputObject.size) + InputObject.size, InputObject.size, InputObject.size)){
                        if(inputs.get(i).outputObject != null){
                            EditorObject editorObject = inputs.get(i).getOutputObject().getEditorObject();
                            editorObject.selectedOutputObject = inputs.get(i).getOutputObject();

                            editorObject.drawLinePoints[0] = editorObject.getX() + editorObject.getWidth();
                            editorObject.drawLinePoints[1] = editorObject.getY() + ((inputs.get(i).getOutputObject().pos + inputs.get(i).getOutputObject().pos) * OutputObject.size) + OutputObject.size * 1.5f;
                            editorObject.drawLinePoints[2] = normX;
                            editorObject.drawLinePoints[3] = normY;

                            editorObject.isDrawLine = true;
                            inputs.get(i).setOutputObject(null);
                            EditorActivity.editorView.setSelectedObject(editorObject);
                        }
                        break;
                    }else if(i < outputs.size() && EditorView.isTouch(normX, normY, getX() + getWidth() - OutputObject.size, getY() + ((i + i) * OutputObject.size) + OutputObject.size, OutputObject.size, OutputObject.size)){
                        selectedOutputObject = outputs.get(i);

                        drawLinePoints[0] = getX() + getWidth();
                        drawLinePoints[1] = getY() + ((i + i) * OutputObject.size) + OutputObject.size * 1.5f;
                        drawLinePoints[2] = normX;
                        drawLinePoints[3] = normY;

                        isDrawLine = true;
                        break;
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(isDrawLine){
                    drawLinePoints[2] = normX;
                    drawLinePoints[3] = normY;
                }else {
                    setX(getX() + (normX - lastX));
                    setY(getY() + (normY - lastY));

                    lastX = normX;
                    lastY = normY;
                }
                return true;
            case MotionEvent.ACTION_UP:
                EditorActivity.editorView.setSelectedObject(null);
                return true;
            default:
                return false;
        }
    }

    public boolean click(){
        EditorActivity.editorView.setSelectedObject(null);
        return true;
    }

    public boolean connectLine(EditorObject editorObject, float normX, float normY){
        isDrawLine = false;
        OutputObject outputObject = selectedOutputObject;
        selectedOutputObject = null;
        InputObject inputObject = getTouchInput(normX, normY, editorObject);
        if(inputObject != null) {
            inputObject.setOutputObject(outputObject);
            return true;
        }else return false;
    }

    public InputObject getInput(int pos){
        return inputs.get(pos);
    }
    public OutputObject getOutput(int pos){
        return outputs.get(pos);
    }

    public void addInput(@NonNull InputObject inputObject){
        inputObject.pos = inputs.size();
        inputs.add(inputObject);
        normalHeight();
    }
    /*public void removeInput(InputObject inputObject){
        inputs.remove(inputObject);
        normalHeight();
    }*/
    public void addOutput(@NonNull OutputObject outputObject){
        outputObject.pos = outputs.size();
        outputs.add(outputObject);
        normalHeight();
    }
    /*public void removeOutput(OutputObject outputObject){
        outputs.remove(outputObject);
        normalHeight();
    }*/
    private void normalHeight(){
        int max = Math.max(inputs.size(), outputs.size());
        if(max < 3) return;
        setHeight(80 + ((max - 2) * 12));
    }

    public void setMyPoint(Point point){
        myPoint = point;
    }
    public Point getMyPoint(){
        return myPoint;
    }

    public static InputObject getTouchInput(float normX, float normY, @NonNull EditorObject editorObject){
        for(int i = 0; i < editorObject.inputs.size(); i++) {
            if(EditorView.isTouch(normX, normY, editorObject.getX(), editorObject.getY() + ((i + i) * InputObject.size) + InputObject.size, InputObject.size, InputObject.size)){
                return editorObject.inputs.get(i);
            }
        }
        return null;
    }
    /*public static OutputObject getTouchOutput(float normX, float normY, EditorObject editorObject){
        for(int i = 0; i < editorObject.outputs.size(); i++) {
            if(EditorView.isTouch(normX, normY, editorObject.getX() + editorObject.getWidth() - OutputObject.size, editorObject.getY() + ((i + i) * OutputObject.size) + OutputObject.size, OutputObject.size, OutputObject.size)){
                return editorObject.outputs.get(i);
            }
        }
        return null;
    }*/

    public static class InputObject {
        final private Paint linePaint, inputPaint;
        final public static int size = 10;
        final private EditorObject editorObject;
        private OutputObject outputObject = null;
        private int pos = 0;

        public InputObject(EditorObject editorObject){
            this.editorObject = editorObject;

            linePaint = new Paint();
            linePaint.setColor(Color.YELLOW);

            inputPaint = new Paint();
            inputPaint.setTextSize(InputObject.size);
            setInputColor(Color.GRAY);
        }

        public void drawLine(Canvas canvas){
            if(outputObject == null) return;

            EditorObject outputEditorObject = outputObject.getEditorObject();
            canvas.drawLine(editorObject.getX(), editorObject.getY() + ((pos + pos) * size) + size * 1.5f, outputEditorObject.getX() + outputEditorObject.getWidth(), outputEditorObject.getY() + ((outputObject.pos + outputObject.pos) * size) + size * 1.5f, linePaint);
        }

        public void draw(@NonNull Canvas canvas){
            canvas.drawRect(editorObject.getX(), editorObject.getY() + ((pos + pos) * InputObject.size) + InputObject.size, editorObject.getX() + InputObject.size, editorObject.getY() + ((pos + pos) * InputObject.size) + InputObject.size + InputObject.size, inputPaint);
        }

        public void setInputColor(int color){
            inputPaint.setColor(color);
        }

        public void setOutputObject(OutputObject outputObject){
            this.outputObject = outputObject;
        }
        public OutputObject getOutputObject(){
            return outputObject;
        }
    }
    public static class OutputObject {
        final private Paint outputPaint;
        final public static int size = 10;
        final private EditorObject editorObject;
        private int pos = 0;

        public OutputObject(EditorObject editorObject){
            this.editorObject = editorObject;

            outputPaint = new Paint();
            outputPaint.setTextSize(InputObject.size);
            setOutputColor(Color.GRAY);
        }

        public void draw(@NonNull Canvas canvas){
            canvas.drawRect(editorObject.getX() + editorObject.getWidth() - size, editorObject.getY() + ((pos + pos) * OutputObject.size) + OutputObject.size, editorObject.getX() + editorObject.getWidth(), editorObject.getY() + ((pos + pos) * OutputObject.size) + OutputObject.size + OutputObject.size, outputPaint);
        }

        public void setOutputColor(int color){
            outputPaint.setColor(color);
        }

        public EditorObject getEditorObject(){
            return editorObject;
        }
    }
}
