package ru.HappyWorldGames.NovelEngine.Editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.HappyWorldGames.NovelEngine.BluePrint.Standard.StandardList;
import ru.HappyWorldGames.NovelEngine.EditorActivity;

public class AddBlockAlertView {
    final private Context c;
    final private AlertDialog alertDialog;
    final private List<String> names = new ArrayList<>();

    public AddBlockAlertView(Context c){
        this.c = c;
        alertDialog = crtAlertDialog();
        names.addAll(Arrays.asList(StandardList.names));
    }

    public void show(){
        alertDialog.show();
    }

    private AlertDialog crtAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setItems(StandardList.names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    EditorObject editorObject = (EditorObject) StandardList.getClassInit(which);
                    EditorView.Camera camera = EditorActivity.editorView.getCamera();

                    editorObject.setX(camera.getNormalX(0));
                    editorObject.setY(camera.getNormalY(0));

                    EditorActivity.editorView.addEditorObject(editorObject);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return builder.create();
    }
}
