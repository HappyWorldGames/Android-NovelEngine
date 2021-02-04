package ru.HappyWorldGames.NovelEngine.Editor;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class EditorThread extends Thread {
    private boolean running = false;
    private long lastTime;
    final public long fps = 60; //need public setting

    final private SurfaceHolder surfaceHolder;
    final private EditorView editorView;

    public EditorThread(SurfaceHolder surfaceHolder, EditorView editorView){
        this.surfaceHolder = surfaceHolder;
        this.editorView = editorView;

        lastTime = System.currentTimeMillis();
    }

    public boolean isRunning(){ return running; }
    public void setRunning(boolean run){ running = run; }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while (isRunning()){
            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    editorView.onDraw(c);
                }
                long tempTime = System.currentTimeMillis() - lastTime;
                long sleepTime = 1000 / fps - tempTime;
                if(sleepTime > 0){
                    synchronized (this) {
                        wait(sleepTime);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    lastTime = System.currentTimeMillis();
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

}
