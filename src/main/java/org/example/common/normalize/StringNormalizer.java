package org.example.common.normalize;


public class StringNormalizer {


    public static String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }


    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

}