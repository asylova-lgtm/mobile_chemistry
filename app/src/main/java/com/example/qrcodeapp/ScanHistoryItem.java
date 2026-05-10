package com.example.qrcodeapp;

import android.graphics.Bitmap;
import android.renderscript.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanHistoryItem {
    private String rawData;        // исходные данные из QR-кода
    private String dataType;       // тип данных (URL, текст, изображение и т.д.)
    private String displayText;    // текст для отображения
    private Bitmap imageBitmap;    // изображение (если есть)
    private String timestamp;      // время сканирования
    private String imageUrl;       // URL изображения (если есть)
    private int localImageResId;  // ID ресурса из drawable

    // Конструктор
    public ScanHistoryItem(String rawData, String dataType, String displayText,
                           Bitmap imageBitmap, String imageUrl, int localImageResId) {
        this.rawData = rawData;
        this.dataType = dataType;
        this.displayText = displayText;
        this.imageBitmap = imageBitmap;
        this.imageUrl = imageUrl;
        this.localImageResId = localImageResId;

        // Автоматически добавляем временную метку
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        this.timestamp = sdf.format(new Date());
    }

    // Геттеры (методы для получения значений)
    public int getLocalImageResId() { return localImageResId; }

    public String getRawData() {
        return rawData;
    }

    public String getDisplayText() {
        return displayText;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Проверка, есть ли изображение
    public boolean hasImage() {
        return imageBitmap != null ||
                (imageUrl != null && !imageUrl.isEmpty()) ||
                localImageResId != 0;
    }
    public String getDataType() {return dataType;}
}