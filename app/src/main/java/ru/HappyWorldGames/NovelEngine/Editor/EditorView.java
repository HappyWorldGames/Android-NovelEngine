package ru.HappyWorldGames.NovelEngine.Editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.HappyWorldGames.NovelEngine.EditorActivity;

public class EditorView extends SurfaceView implements SurfaceHolder.Callback {
    final private Camera camera;
    private EditorThread editorThread;

    final private HashMap<Point, List<EditorObject>> editorObjectList = new HashMap<>();
    final private int globalScale = 1000;

    private EditorObject selectedObject;
    private long tempTime;

    final private ScaleGestureDetector scaleGestureDetector;

    private float lastX, lastY;

    final private Paint removeZonePaint;

    public EditorView(Context c){
        super(c);
        getHolder().addCallback(this);
        scaleGestureDetector = new ScaleGestureDetector(c, new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                camera.setScale(camera.getScale() * scaleGestureDetector.getScaleFactor());
                camera.setScale(Math.max(0.1f, Math.min(camera.getScale(), 10)));
                return true;
            }
        });
        camera = new Camera();

        removeZonePaint = new Paint();
        removeZonePaint.setColor(Color.RED);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        canvas.translate(camera.getX(), camera.getY());
        canvas.scale(camera.getScale(), camera.getScale());
        canvas.drawColor(Color.GRAY);

        if(selectedObject != null){
            float normX = camera.getNormalX(0);
            float normY = camera.getNormalY(0);
            canvas.drawRect(normX, normY, normX + EditorActivity.screenWidth / camera.getScale(), normY + EditorActivity.screenHeight * 0.05f / camera.getScale(), removeZonePaint);
        }

        EditorObject[] editorObjects = getViewEditorObjects();
        for(EditorObject editorObject : editorObjects)
            editorObject.draw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float normX = camera.getNormalX(event.getX());
        float normY = camera.getNormalY(event.getY());

        OnReSize(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return OnDown(event, normX, normY);
            case MotionEvent.ACTION_MOVE:
                return OnMove(event, normX, normY);
            case MotionEvent.ACTION_UP:
                return OnUp(event, normX, normY);
            default:
                return false;
        }
    }

    private boolean OnDown(@NonNull MotionEvent event, float normX, float normY){
        lastX = event.getX();
        lastY = event.getY();

        EditorObject[] editorObjects = getViewEditorObjects();
        for(int i = editorObjects.length - 1; i >= 0; i--) {
            EditorObject editorObject = editorObjects[i];
            if (isTouch(normX, normY, editorObject.getX(), editorObject.getY(), editorObject.getWidth(), editorObject.getHeight())) {
                selectedObject = editorObject;
                tempTime = System.currentTimeMillis();
                return editorObject.touch(event, normX, normY);
            }
        }

        selectedObject = null;
        return true;
    }
    private boolean OnMove(MotionEvent event, float normX, float normY){
        if(selectedObject != null) {
            updateEditorObject(selectedObject);
            return selectedObject.touch(event, normX, normY);
        }else {
            camera.setX(camera.getX() + camera.getDifferentX(event.getX(), lastX));
            camera.setY(camera.getY() + camera.getDifferentY(event.getY(), lastY));

            lastX = event.getX();
            lastY = event.getY();
            return true;
        }
    }
    private boolean OnUp(@NonNull MotionEvent event, float normX, float normY){
        lastX = event.getX();
        lastY = event.getY();

        if(selectedObject != null){
            if(selectedObject.isDrawLine){
                EditorObject[] editorObjects = getViewEditorObjects();
                for(int i = editorObjects.length - 1; i >= 0; i--) {
                    EditorObject editorObject = editorObjects[i];
                    if (isTouch(normX, normY, editorObject.getX(), editorObject.getY(), editorObject.getWidth(), editorObject.getHeight())) {
                        return selectedObject.connectLine(editorObject, normX, normY);
                    }
                }
                selectedObject.isDrawLine = false;
            }else{
                float zeroNormX = camera.getNormalX(0);
                float zeroNormY = camera.getNormalY(0);
                if(isTouch(normX, normY, zeroNormX, zeroNormY, EditorActivity.screenWidth / camera.getScale(), EditorActivity.screenHeight * 0.05f / camera.getScale())){
                    removeEditorObject(selectedObject);
                    selectedObject = null;
                    return true;
                }
            }
            updateEditorObject(selectedObject);
            return System.currentTimeMillis() - tempTime < 300 ? selectedObject.click() : selectedObject.touch(event, normX, normY);
        }else return false;
    }
    private void OnReSize(MotionEvent event){
        scaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        editorThread = new EditorThread(getHolder(), this);
        editorThread.setRunning(true);
        editorThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        editorThread.setRunning(false);
        while (retry) {
            try {
                editorThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }

    public Camera getCamera(){ return camera; }
    public void setSelectedObject(EditorObject editorObject){
        this.selectedObject = editorObject;
    }

    public void addEditorObject(@NonNull EditorObject editorObject){
        Point point = new Point((int)Math.floor(editorObject.getX() / globalScale), (int)Math.floor(editorObject.getY() / globalScale));
        List<EditorObject> list = editorObjectList.get(point);
        if(list == null) list = new ArrayList<>();
        editorObject.setMyPoint(point);
        list.add(editorObject);
        editorObjectList.put(point, list);
    }
    public EditorObject[] getEditorObjects(){
        List<EditorObject> all = new ArrayList<>();
        for(List<EditorObject> editorObjects : editorObjectList.values())
            all.addAll(editorObjects);
        return all.toArray(new EditorObject[0]);
    }
    public void removeEditorObject(@NonNull EditorObject editorObject){
        Point point = editorObject.getMyPoint();
        List<EditorObject> list = editorObjectList.get(point);
        if(list == null) return;
        list.remove(editorObject);
        editorObject.setMyPoint(null);
        if(list.size() == 0) editorObjectList.remove(point);
        else editorObjectList.put(point, list);
    }
    private void updateEditorObject(@NonNull EditorObject editorObject){
        if(editorObject.getMyPoint().equals(new Point((int) Math.floor(editorObject.getX() / globalScale), (int) Math.floor(editorObject.getY() / globalScale)))) return;
        removeEditorObject(editorObject);
        addEditorObject(editorObject);
    }

    public Point[] getViewPoints(){
        int minPointX = (int)Math.floor(camera.getNormalX(0) / globalScale);
        int maxPointX = (int)Math.floor((camera.getNormalX(0) + camera.getWidth()) / globalScale);
        int minPointY = (int)Math.floor(camera.getNormalY(0) / globalScale);
        int maxPointY = (int)Math.floor((camera.getNormalY(0) + camera.getHeight()) / globalScale);

        List<Point> points = new ArrayList<>();
        for(int i = minPointX - 1; i < maxPointX + 2; i++)
            for (int j = minPointY - 1; j < maxPointY + 2; j++)
                points.add(new Point(i, j));

        return points.toArray(new Point[0]);
    }
    public EditorObject[] getViewEditorObjects(){
        Point[] points = getViewPoints();
        List<EditorObject> editorObjects = new ArrayList<>();

        for(Point point : points) {
            List<EditorObject> list = editorObjectList.get(point);
            if(list != null) editorObjects.addAll(list);
        }

        return editorObjects.toArray(new EditorObject[0]);
    }

    public static boolean isTouch(float xMouse, float yMouse, float xObj, float yObj, float width, float height){
        return xMouse > xObj && xMouse < xObj + width && yMouse > yObj && yMouse < yObj + height;
    }

    public static class Camera {
        private float x, y, scale;

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
        public float getScale() {
            return scale;
        }
        public void setScale(float scale) {
            this.scale = scale;
        }

        public int getWidth(){
            return (int)(EditorActivity.screenWidth / getScale());
        }
        public int getHeight(){
            return (int)(EditorActivity.screenHeight / getScale());
        }

        public Camera(){
            setX(0);
            setY(0);
            setScale(1);
        }

        public float getNormalX(float mouseX){
            return (mouseX - getX()) / getScale();
        }
        public float getNormalY(float mouseY){
            return (mouseY - getY()) / getScale();
        }

        public float getDifferentX(float mouseX, float lastX){ return mouseX - lastX; }
        public float getDifferentY(float mouseY, float lastY){ return mouseY - lastY; }
    }

}
