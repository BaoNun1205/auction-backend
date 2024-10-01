package com.example.auction_web.utils;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;

public class CreateSlug {
    // Phương thức tạo slug
    public static String createSlug(String input) {
        // Chuyển sang chữ thường
        String slug = input.toLowerCase();

        // Loại bỏ dấu
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{M}", "");

        // Thay thế khoảng trắng và các ký tự đặc biệt
        slug = slug.replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");

        return slug;
    }
}
