package com.example.dictionary;

import java.util.ArrayList;

public class Language {
    private int id;
    private String code;
    private String name;

    private static ArrayList<Language> languageArrayList;

    public Language(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static void initLanguage() {
        languageArrayList = new ArrayList<Language>();

        Language english = new Language(0, "en", "English");
        languageArrayList.add(english);

        Language vietnam = new Language(1, "vi", "Vietnamese");
        languageArrayList.add(vietnam);

        Language thailand = new Language(2, "th", "Thailand");
        languageArrayList.add(thailand);

    }

    public static ArrayList<Language> getLanguageArrayList() {
        return languageArrayList;
    }

    public static String[] languageNames() {
        String[] names = new String[languageArrayList.size()];
        for (int i = 0; i < languageArrayList.size(); ++i) {
            names[i] = languageArrayList.get(i).name;
        }
        return names;
    }

    public static String[] languageCode() {
        String[] codes = new String[languageArrayList.size()];
        for (int i = 0; i < languageArrayList.size(); ++i) {
            codes[i] = languageArrayList.get(i).code;
        }
        return codes;
    }

    public static String getCodeFromName(String name) {
        for (int i = 0; i < languageArrayList.size(); ++i) {
            if (languageArrayList.get(i).name.equals(name)) {
                return languageArrayList.get(i).code;
            }
        }
        return "error";
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}

