package ru.HappyWorldGames.NovelEngine;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.HappyWorldGames.NovelEngine.BluePrint.BluePrintEditorView;
import ru.HappyWorldGames.NovelEngine.Editor.AddBlockAlertView;
import ru.HappyWorldGames.NovelEngine.Editor.EditorView;

public class EditorActivity extends AppCompatActivity {
    public static float screenWidth, screenHeight;
    public static Context context;

    public String projectName;
    public Files files;
    public ManifestSetting manifestSetting;

    public static EditorView editorView;
    public AddBlockAlertView addBlockAlertView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Display display1 = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display1.getRealSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        projectName = getIntent().getStringExtra("projectName");
        files = new Files(projectName);
        manifestSetting = ManifestSetting.loadManifest(projectName);

        editorView = new BluePrintEditorView(this);
        setContentView(editorView);

        addBlockAlertView = new AddBlockAlertView(this);

        editorView.post(new Runnable() {
            @Override
            public void run() {
                screenWidth = editorView.getWidth();
                screenHeight = editorView.getHeight();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Blocks").setShowAsAction(2);
        if(editorView.getClass().equals(BluePrintEditorView.class)) menu.add(0, 2, 0, "Var").setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                addBlockAlertView.show();
                return true;
            case 2:
                return true;
        }
        return false;
    }
}
