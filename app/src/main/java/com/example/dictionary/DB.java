package com.example.dictionary;

public class DB {
    public static String[] getData(int id) {
        if (id == R.id.action_eng_vn) {
            return getEngVn();
        } else if (id == R.id.action_eng_eng) {
            return getEngEng();
        } else if (id == R.id.action_vn_eng) {
            return getVnEng();
        }
        return new String[0];
    }

    public static String[] getEngVn() {
        String[] source = new String[] {
                "a",
                "abadon",
                "ability",
                "able",
                "about",
                "above",
                "aboard",
                "absense",
                "absent",
                "absolute",
                "absolutely",
                "absorb",
                "abuse",
                "academic",
                "accent",
                "accept",
                "acceptable",
                "access",
                "accident",
                "accidental",
                "accidentally",
                "accommodation",
                "accompany"
        };
        return source;
    }

    public static String[] getVnEng() {
        String[] source = new String[] {
                "Ai",
                "Ba",
                "Cai gi",
                "Do",
                "Em",
                "Gom",
                "Ha",
                "Han",
                "Huyen",
                "I",
                "In",
                "Inh",
                "Kien",
                "Lay",
                "Minh",
                "May",
                "Mai",
                "Suy",
                "Sung",
                "Suong",
                "Truong",
                "Yen"
        };
        return source;
    }

    public static String[] getEngEng() {
        String[] source = new String[] {
                "i don't know",
                "find it yourselg"
        };
        return source;
    }
}
