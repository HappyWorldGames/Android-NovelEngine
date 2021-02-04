package ru.HappyWorldGames.NovelEngine.BluePrint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import ru.HappyWorldGames.NovelEngine.Editor.EditorView;

public class BluePrintEditorView extends EditorView {

    final private Paint paint = new Paint();

    public BluePrintEditorView(Context c) {
        super(c);

        paint.setColor(Color.CYAN);
        paint.setTextSize(24);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        paint.setTextSize(24 / getCamera().getScale());
        canvas.drawText("BLUEPRINT X=" + (int)getCamera().getNormalX(0) + " Y=" + (int)-getCamera().getNormalY(0), getCamera().getNormalX(0), getCamera().getNormalY(0) + paint.getTextSize(), paint);
    }
}
