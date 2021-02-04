package ru.HappyWorldGames.NovelEngine.Scenes;

import android.content.Context;

import androidx.annotation.NonNull;

import ru.HappyWorldGames.NovelEngine.Editor.EditorObject;
import ru.HappyWorldGames.NovelEngine.Editor.EditorView;

public class ScenesTreeEditor extends EditorView {

    public ScenesTreeEditor(Context c){
        super(c);
    }

    @NonNull
    public static EditorObject crtEditorObject(@NonNull SceneInfo sceneInfo){
        EditorObject editorObject = new EditorObject();

        editorObject.addInput(new EditorObject.InputObject(editorObject));

        for(int i = 0; i < sceneInfo.getExitBlocksCount(); i++)
            editorObject.addOutput(new EditorObject.OutputObject(editorObject));

        return editorObject;
    }
}
