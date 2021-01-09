package com.github.phoenix_dev38.osk;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile {

    public static final String FILEBASEDIRPATH = "plugins/OneShotKill/";

    public static final String LOCATIONFILEPATH = FILEBASEDIRPATH + "location.yml";
    public static final String SETTINGFILEPATH = FILEBASEDIRPATH + "setting.yml";

    public static final YamlConfiguration LOCATIONYAML = new YamlConfiguration();
    public static final YamlConfiguration SETTINGYAML = new YamlConfiguration();

    public static void loadLocation() {
        File directory = new File(FILEBASEDIRPATH);
        if (!directory.exists()) new File(FILEBASEDIRPATH).mkdir();

        File file = new File(LOCATIONFILEPATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            LOCATIONYAML.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveLocation() {
        File directory = new File(FILEBASEDIRPATH);
        if (!directory.exists()) new File(FILEBASEDIRPATH).mkdir();

        File file = new File(LOCATIONFILEPATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            LOCATIONYAML.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSetting() {
        File directory = new File(FILEBASEDIRPATH);
        if (!directory.exists()) new File(FILEBASEDIRPATH).mkdir();

        File file = new File(SETTINGFILEPATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            SETTINGYAML.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveSetting() {
        File directory = new File(FILEBASEDIRPATH);
        if (!directory.exists()) new File(FILEBASEDIRPATH).mkdir();

        File file = new File(SETTINGFILEPATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            SETTINGYAML.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
