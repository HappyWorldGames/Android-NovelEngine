package ru.HappyWorldGames.NovelEngine;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ManifestSetting {

    public String projectName;
    public String packageName;
    public int versionCode = 1;
    public String versionName = "1.0";
    public String initScene = "null";

    public ManifestSetting(String projectName, String packageName){
        this.projectName = projectName;
        this.packageName = packageName;
    }

    public static ManifestSetting loadManifest(String projectName){
        File projectDir = new File(Files.projectsDir, projectName);
        File manifestFile = new File(projectDir, "manifest.xml");

        ManifestSetting manifestSetting = new ManifestSetting(projectName, null);
        XmlPullParser parser = Xml.newPullParser();
        try {
            FileInputStream fis = new FileInputStream(manifestFile);

            parser.setInput(fis, "UTF-8");
            parser.nextTag();

            manifestSetting.projectName = parser.getAttributeValue("", "project_name");
            manifestSetting.packageName = parser.getAttributeValue("", "package_name");
            manifestSetting.versionCode = Integer.parseInt(parser.getAttributeValue("", "version_code"));
            manifestSetting.versionName = parser.getAttributeValue("", "version_name");
            manifestSetting.initScene = parser.getAttributeValue("", "init_scene");

            return manifestSetting;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void saveManifest(){
        File projectDir = new File(Files.projectsDir, projectName);
        File manifestFile = new File(projectDir, "manifest.xml");

        XmlSerializer serializer = Xml.newSerializer();
        try {
            FileOutputStream fos = new FileOutputStream(manifestFile);

            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "manifest");

            serializer.attribute("", "version_engine", String.valueOf(BuildConfig.VERSION_CODE));
            serializer.attribute("", "project_name", projectName);
            serializer.attribute("", "package_name", packageName);
            serializer.attribute("", "version_code", String.valueOf(versionCode));
            serializer.attribute("", "version_name", versionName);
            serializer.attribute("", "init_scene", initScene);

            serializer.endTag("", "manifest");
            serializer.endDocument();

            fos.flush();
            fos.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
