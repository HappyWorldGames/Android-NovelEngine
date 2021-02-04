package ru.HappyWorldGames.NovelEngine;

import android.annotation.SuppressLint;

import java.io.File;

import ru.HappyWorldGames.NovelEngine.Editor.EditorObject;
import ru.HappyWorldGames.NovelEngine.Editor.EditorView;

/*
    Project Folder Structure
    Project
        .build //содержит временные файлы сборки
        assets //ресурсы проекта: картинки, тексты и тд.
            images
            text
            audio
        scripts //содержит визуальные скрипты
        scenes //содержит сцены
        scenes_tree.xml //содержит дерево сцен
        manifest.xml

    Manifest.xml
    version_engine = number version engine
    project_name = Name Project
    package_name = packageName
    version_code = number version
    version_name = name version
    init_scene = Name Scene
 */
public class Files {
    @SuppressLint("SdCardPath")
    final public static File projectsDir = new File("/sdcard/NovelEngine/");
    public File projectDir;

    public File assetsDir;

    public File imagesDir;
    public File textDir;
    public File audioDir;

    public File scriptsDir;
    public File scenesDir;

    public Files(String projectName){
        projectDir = new File(projectsDir, projectName);
        init();
    }

    public static Files createProject(String projectName, String packageName){
        if(projectName == null || packageName == null || projectName.equals("") || packageName.equals("")) return null;

        Files files = new Files(projectName);

        ManifestSetting manifestSetting = new ManifestSetting(projectName, packageName);
        manifestSetting.saveManifest();

        files.createScenesTree();

        return files;
    }

    public void createScenesTree(){
        File scenesTree = new File(projectDir, "scenes_tree.xml");
        try {
            if(!scenesTree.exists()) scenesTree.createNewFile();
        }catch (Exception e){ throw new RuntimeException(e); }
    }

    private void init(){
        if(!projectDir.exists()) projectDir.mkdirs();

        assetsDir = new File(projectDir, "assets");
        if(!assetsDir.exists()) assetsDir.mkdirs();

        imagesDir = new File(assetsDir, "images");
        if(!imagesDir.exists()) imagesDir.mkdirs();
        textDir = new File(assetsDir, "text");
        if(!textDir.exists()) textDir.mkdirs();
        audioDir = new File(assetsDir, "audio");
        if(!audioDir.exists()) audioDir.mkdirs();

        scriptsDir = new File(projectDir, "scripts");
        if(!scriptsDir.exists()) scriptsDir.mkdirs();

        scenesDir = new File(projectDir, "scenes");
        if(!scenesDir.exists()) scenesDir.mkdirs();
    }

    public static void saveEditorView(EditorView editorView, File file){
        for(EditorObject editorObject : editorView.getEditorObjects()){

        }
    }
}
