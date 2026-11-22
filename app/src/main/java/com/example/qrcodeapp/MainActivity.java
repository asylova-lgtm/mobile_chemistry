package com.example.qrcodeapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // UI элементы
    private Button btnScan, btnClear, btnClearHistory, btnShowDescription, btn_combine_elements;
    private TextView TvResultTitle, TvResultType, TvResultContent, TvResultLink;
    private ImageView IvResultImage;
    private LinearLayout historyContainer;
    private ScrollView scrollView;
    private  TextView tvHistoryToggle;
    private Element currentDisplayedElement = null;

    // История сканирований
    private List<ScanHistoryItem> historyList = new ArrayList<>();
    private static final int MAX_HISTORY_ITEMS = 50;

    // Интерфейс для колбэка загрузки изображения
    interface ImageLoadCallback {
        void onImageLoaded(int position);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupClickListeners();
        // TEST BD
        DatabasesInitializer.populateDatabase(this);
        updateHistoryDisplay();
    }

    private void displayElementFromDb(String englishName) {
        AppDatabase db = AppDatabase.getInstance(this);
        Element element = db.elementDao().getElementByEnglishName(englishName);

        if (element != null) {
            currentDisplayedElement = element;
            // 1. Показываем русское название на главном экране
            TvResultTitle.setText("Результат сканирования:");
            TvResultContent.setText(element.nameRu);
            TvResultContent.setVisibility(View.VISIBLE);

            // 2. Устанавливаем тип (категорию)
            if (element.categoryId != null) {
                Categories cat = db.categoryDao().getCategoryById(element.categoryId);
                /*if (cat != null) {
                    TvResultType.setText("Тип: Химический элемент (" + cat.nameCategories + ")");
                }*/
            }

            // 3. Показываем картинку
            List<Media> media = db.mediaDao().getMediaForElement(element.id);
            if (!media.isEmpty()) {
                int resId = getResources().getIdentifier(media.get(0).filePath, "drawable", getPackageName());
                if (resId != 0) {
                    IvResultImage.setImageResource(resId);
                    IvResultImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void initViews() {
        btnScan = findViewById(R.id.btn_scan);
        btnClear = findViewById(R.id.btn_clear);
        btnClearHistory = findViewById(R.id.btn_clear_history);
        TvResultTitle = findViewById(R.id.tv_result_title);
        TvResultType = findViewById(R.id.tv_result_type);
        TvResultContent = findViewById(R.id.tv_result_content);
        TvResultLink = findViewById(R.id.tv_result_link);
        IvResultImage = findViewById(R.id.iv_result_image);
        historyContainer = findViewById(R.id.history_container);
        scrollView = findViewById(R.id.scroll_view);
        tvHistoryToggle = findViewById(R.id.tv_history_toggle);
        btnShowDescription = findViewById(R.id.btn_show_description);
        btn_combine_elements = findViewById(R.id.btn_combine_elements);
    }

    private void setupClickListeners() {
        btnScan.setOnClickListener(v -> startQRScanner());
        btnClear.setOnClickListener(v -> clearCurrentResult());
        btnClearHistory.setOnClickListener(v -> clearHistory());
        if (btnShowDescription != null) {
            btnShowDescription.setOnClickListener(v -> showLastScanDescription());
        }
        btn_combine_elements.setOnClickListener(v -> showCombineElementDialog());
        tvHistoryToggle.setOnClickListener(v -> {
            if (historyContainer.getVisibility() == View.VISIBLE) {
                historyContainer.setVisibility(View.GONE);
                tvHistoryToggle.setText("История сканирования ▼");
            } else {
                historyContainer.setVisibility(View.VISIBLE);
                tvHistoryToggle.setText("История сканирования ▲");
            }
        });
    }

    private void startQRScanner() {
        Intent intent = new Intent(this, CustomScannerActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
                String scannedText = data.getStringExtra("SCAN_RESULT");
                if (scannedText != null) {
                    // Передаем отсканированный текст в наш метод обработки (база данных)
                    processScannedData(scannedText);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void processScannedData(String data) {
        String scannedText = data.trim().toLowerCase();

        // Скрываем старые результаты
        hideAllResultViews();
        TvResultTitle.setVisibility(View.VISIBLE);
        TvResultType.setVisibility(View.VISIBLE);

        // 1. Ищем элемент в БД
        AppDatabase db = AppDatabase.getInstance(this);
        Element element = db.elementDao().getElementByEnglishName(scannedText);

        if (element != null) {
            // === ЕСЛИ ЭТО ХИМИЧЕСКИЙ ЭЛЕМЕНТ ===
            // Выводим данные на главный экран (картинка + название)
            displayElementFromDb(scannedText);

            // Открываем всплывающее окно с полным описанием
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                showElementDialog(element);
            }, 500);

            // Сохраняем в историю в БД
            ScanHistory history = new ScanHistory();
            history.elementId = element.id;
            history.displayText = element.nameRu;
            history.dataType = "Химический элемент";
            history.scannedDate = System.currentTimeMillis();
            db.scanHistoryDao().insert(history);

        } else {
            // === ЕСЛИ ЭТО НЕ ЭЛЕМЕНТ (обычный текст) ===
            TvResultType.setText("Тип: Текст");
            TvResultContent.setVisibility(View.VISIBLE);
            TvResultContent.setText(scannedText);

            // Пишем в историю
            ScanHistory history = new ScanHistory();
            history.elementId = null;
            history.displayText = scannedText;
            history.dataType = "Текст";
            history.scannedDate = System.currentTimeMillis();
            db.scanHistoryDao().insert(history);
        }
        updateHistoryDisplay();
    }

    private void showElementDialog(Element element) {
        try {
            AppDatabase db = AppDatabase.getInstance(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_discription, null);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);

            // 1. Находим элементы интерфейса
            TextView titleView = dialogView.findViewById(R.id.dialog_title);
            TextView typeView = dialogView.findViewById(R.id.dialog_type);
            TextView timeView = dialogView.findViewById(R.id.dialog_time);
            TextView descriptionView = dialogView.findViewById(R.id.dialog_description);
            Button closeButton = dialogView.findViewById(R.id.dialog_close);
            WebView webView = dialogView.findViewById(R.id.webViewModel);

            // 2. Заполняем текстовые поля
            String categoryName = "Неизвестно";
            if (element.categoryId != null) {
                Categories cat = db.categoryDao().getCategoryById(element.categoryId);
                if (cat != null) categoryName = cat.nameCategories;
            }

            titleView.setText("Химический элемент:\n" + element.nameRu);
            typeView.setText("Символ: " + element.symbol + " | Атомный номер: " + element.atomicNumber);
            timeView.setText("Группа: " + categoryName);

            String fullDescription = "ОПИСАНИЕ:\n" + element.description + "\n\n═════════════════\n" +
                    "ПРИМЕНЕНИЕ:\n" + element.uses;
            descriptionView.setText(fullDescription);

            // 3. Ищем 3D-модель в БД
            String modelFileName = null;
            List<Media> allMedia = db.mediaDao().getMediaForElement(element.id);
            for (Media m : allMedia) {
                if ("model3d".equals(m.type)) {
                    modelFileName = m.filePath + ".glb";
                    break;
                }
            }

            // 4. Настраиваем WebView и загружаем модель
            if (modelFileName != null && !modelFileName.isEmpty()) {
                webView.setVisibility(View.VISIBLE);
                webView.getSettings().setJavaScriptEnabled(true);

                // Логи для проверки
                webView.setWebChromeClient(new android.webkit.WebChromeClient() {
                    @Override
                    public boolean onConsoleMessage(android.webkit.ConsoleMessage consoleMessage) {
                        android.util.Log.d("WEBVIEW_3D", consoleMessage.message());
                        return true;
                    }
                });

                // Создаем загрузчик для assets
                final androidx.webkit.WebViewAssetLoader assetLoader = new androidx.webkit.WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new androidx.webkit.WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new android.webkit.WebViewClient() {
                    @Override
                    public android.webkit.WebResourceResponse shouldInterceptRequest(android.webkit.WebView view, android.webkit.WebResourceRequest request) {
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                // Формируем HTML
                String htmlData = "<html><head>" +
                        "<script type='module' src='https://appassets.androidplatform.net/assets/model-viewer.min.js'></script>" +
                        "</head><body style='margin:0; padding:0;'>" +
                        "<model-viewer id='mv' src='https://appassets.androidplatform.net/assets/" + modelFileName + "' " +
                        "auto-rotate camera-controls style='width:100%; height:100%; background-color: #ffffff;'></model-viewer>" +
                        "<script>" +
                        "  const mv = document.getElementById('mv');" +
                        "  mv.addEventListener('load', () => console.log('MODEL_LOADED_SUCCESS'));" +
                        "  mv.addEventListener('error', (e) => console.log('MODEL_LOAD_ERROR: ' + e.detail));" +
                        "</script>" +
                        "</body></html>";

                // ГЛАВНОЕ ИЗМЕНЕНИЕ ЗДЕСЬ: передаем базовый URL вместо null
                webView.loadDataWithBaseURL(
                        "https://appassets.androidplatform.net/assets/",
                        htmlData,
                        "text/html",
                        "UTF-8",
                        null
                );

            } else {
                webView.setVisibility(View.GONE);
            }

            // 5. Кнопка закрытия
            closeButton.setOnClickListener(v -> {
                if (webView != null) {
                    webView.destroy();
                }
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при открытии диалога: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromUrl(String imageUrl, ImageLoadCallback callback) {
        if (IvResultImage == null) return;

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        IvResultImage.setImageDrawable(resource);
                        IvResultImage.setVisibility(View.VISIBLE);

                        if (callback != null) {
                            callback.onImageLoaded(historyList.size());
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        IvResultImage.setImageDrawable(errorDrawable);
                        IvResultImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    // ================ РАБОТА С ИСТОРИЕЙ ИЗ БД ================

    private List<Element> getScannedElementsFromDb(){
        List<Element> uniqueElements = new ArrayList<>();
        AppDatabase db = AppDatabase.getInstance(this);
        List<ScanHistory> history = db.scanHistoryDao().getAllHistory();

        for (ScanHistory h : history) {
            if (h.elementId != null) {
                Element el = db.elementDao().getElementById(h.elementId);
                if (el != null) {
                    //нет ли такого элемента в списке
                    boolean exists = false;
                    for (Element e : uniqueElements) {
                        if (e.id == el.id) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) uniqueElements.add(el);
                }
            }
        }
        return  uniqueElements;
    }

    private void showCombineElementDialog() {
        List<Element> elements = getScannedElementsFromDb();

        if (elements.size() < 2) {
            Toast.makeText(this, "Для реакции необходимо минимум 2 химических элемента", Toast.LENGTH_LONG).show();
            return;
        }

        String[] names = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            names[i] = elements.get(i).nameRu;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите первый элемент");
        builder.setItems(names, ((dialog, which) -> {
            Element first = elements.get(which);
            showSecondElementDialog(first, elements, names);
        }));
        builder.show();
    }

    private void showSecondElementDialog(Element first, List<Element> elements, String[] names) {
        Log.d("REACTION_TEST", "Выбран первый элемент: " + first.nameRu);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите второй элемент для реакции с " + first.symbol);
        builder.setItems(names, ((dialog, which) -> {
            Element second = elements.get(which);
            if(first.id == second.id) {
                Toast.makeText(this, "Выберите другой элемент!", Toast.LENGTH_LONG).show();
                return;
            }
            checkReactionInDb(first, second);
        }));
        builder.show();
    }

    private void checkReactionInDb(Element first, Element second) {
        AppDatabase db = AppDatabase.getInstance(this);
        Reaction reaction = null;

        reaction = db.reactionDao().getReaction(first.id, second.nameRu);
        if (reaction == null) {
            reaction = db.reactionDao().getReaction(second.id, first.nameRu);
        }
        showReactionResultDialog(first, second, reaction);
    }

    private void showReactionResultDialog(Element first, Element second, Reaction reaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_discription, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);

        TextView titleView = dialogView.findViewById(R.id.dialog_title);
        TextView typeView = dialogView.findViewById(R.id.dialog_type);
        TextView timeView = dialogView.findViewById(R.id.dialog_time);
        TextView descriptionView = dialogView.findViewById(R.id.dialog_description);
        Button closeButton = dialogView.findViewById(R.id.dialog_close);

        // Скрываем WebView, так как для реакций 3D-модель не нужна
        WebView webView = dialogView.findViewById(R.id.webViewModel);
        if (webView != null) webView.setVisibility(View.GONE);

        titleView.setText("⚗️ Химическая реакция ⚗️");

        if (reaction != null) {
            typeView.setText(first.nameRu + " + " + second.nameRu);
            timeView.setText("Условия: " + reaction.conditions);

            String fullInfo = "УРАВНЕНИЕ:\n" + reaction.equation +
                    "\n\n═══════════════════════════\n" +
                    "ПРОДУКТЫ:\n" + reaction.products +
                    "\n\n═══════════════════════════\n" +
                    "ОПИСАНИЕ:\n" + reaction.descriptionReaction;
            descriptionView.setText(fullInfo);
        } else {
            typeView.setText(first.nameRu + " + " + second.nameRu);
            timeView.setText("Реакция не зарегистрирована");

            String fullInfo = "В базе данных нет информации о реакции между\n" +
                    first.nameRu + " и " + second.nameRu + ".\n\n" +
                    "Возможно, эти элементы не реагируют друг с другом.";
            descriptionView.setText(fullInfo);
        }

        closeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void updateHistoryDisplay() {
        historyContainer.removeAllViews();
        AppDatabase db = AppDatabase.getInstance(this);

        // Получаем историю из БД (свежие сканирования сверху)
        List<ScanHistory> historyList = db.scanHistoryDao().getAllHistory();

        if (historyList.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("История пуста");
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setPadding(0, 20, 0, 20);
            emptyView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            historyContainer.addView(emptyView);
            return;
        }

        if (historyList.isEmpty()) {
            tvHistoryToggle.setVisibility(View.GONE);
            historyContainer.setVisibility(View.GONE);
            return;
        } else {
            tvHistoryToggle.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < historyList.size(); i++) {
            ScanHistory item = historyList.get(i);
            View historyItemView = getLayoutInflater().inflate(R.layout.history_item, historyContainer, false);

            TextView typeView = historyItemView.findViewById(R.id.history_item_type);
            TextView timeView = historyItemView.findViewById(R.id.history_item_time);
            TextView textView = historyItemView.findViewById(R.id.history_item_text);
            ImageView imageView = historyItemView.findViewById(R.id.history_item_image);

            // Форматируем время
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            timeView.setText(sdf.format(new java.util.Date(item.scannedDate)));
            typeView.setText(item.dataType);

            String displayText = item.displayText;
            if (displayText != null && displayText.length() > 50) {
                displayText = displayText.substring(0, 47) + "...";
            }
            textView.setText(displayText);

            // Если есть ID элемента, показываем картинку
            if (item.elementId != null) {
                List<Media> media = db.mediaDao().getMediaForElement(item.elementId);
                if (!media.isEmpty()) {
                    int resId = getResources().getIdentifier(media.get(0).filePath, "drawable", getPackageName());
                    if (resId != 0) {
                        imageView.setImageResource(resId);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            final int position = i;
            // Обработчик короткого нажатия
            historyItemView.setOnClickListener(v -> restoreFromHistory(historyList.get(position)));

            // Обработчик долгого нажатия (пока оставим так)
            historyItemView.setOnLongClickListener(v -> {
                Toast.makeText(this, "Долгое нажатие пока в разработке", Toast.LENGTH_SHORT).show();
                return true;
            });

            historyContainer.addView(historyItemView);
        }
    }

    // Восстановление из истории по клику
    private void restoreFromHistory(ScanHistory item) {
        clearCurrentResult();
        TvResultTitle.setVisibility(View.VISIBLE);
        TvResultType.setVisibility(View.VISIBLE);

        TvResultType.setText("Тип: " + item.dataType);

        if (item.elementId != null) {
            AppDatabase db = AppDatabase.getInstance(this);
            // Ищем элемент по его ID из базы данных
            Element element = db.elementDao().getElementById(item.elementId);

            if (element != null) {
                // Вызываем наш метод, который показывает картинку и описание
                displayElementFromDb(element.nameEn);
            } else {
                TvResultContent.setVisibility(View.VISIBLE);
                TvResultContent.setText(item.displayText);
            }
        } else {
            TvResultContent.setVisibility(View.VISIBLE);
            TvResultContent.setText(item.displayText);
        }

        Toast.makeText(this, "Восстановлено из истории", Toast.LENGTH_SHORT).show();
    }
    // Очистка истории
    private void clearHistory() {
        AppDatabase db = AppDatabase.getInstance(this);
        // Очищаем таблицу (нужно добавить метод в DAO)
        db.scanHistoryDao().deleteAll();

        updateHistoryDisplay();
        Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show();
    }

    private void showLastScanDescription() {
        if (currentDisplayedElement != null) {
            // Если сейчас на экране что-то есть, показываем окно для него
            showElementDialog(currentDisplayedElement);
        } else {
            // Если экран пуст, ищем последний элемент в истории
            AppDatabase db = AppDatabase.getInstance(this);
            List<ScanHistory> history = db.scanHistoryDao().getAllHistory();

            if (history.isEmpty()) {
                Toast.makeText(this, "Нет отсканированных данных", Toast.LENGTH_SHORT).show();
                return;
            }

            ScanHistory lastItem = history.get(0);
            if (lastItem.elementId != null) {
                Element element = db.elementDao().getElementById(lastItem.elementId);
                if (element != null) {
                    showElementDialog(element);
                }
            } else {
                Toast.makeText(this, "Последнее сканирование не является химическим элементом", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isChemicalElement(String name) {
        if (name == null) return false;
        String s = name.toLowerCase();
        return s.equals("hydrogen") || s.equals("h") ||
                s.equals("nitrogen") || s.equals("n") ||
                s.equals("oxygen") || s.equals("o") ||
                s.equals("iron") || s.equals("fe") ||
                s.equals("sodium") || s.equals("na") ||
                s.equals("sulfur") || s.equals("s");
    }

    // Метод для получения символа элемента из названия
    private String getChemicalSymbolFromName(String name) {
        if (name == null) return null;
        switch (name.toLowerCase()) {
            case "hydrogen": return "H";
            case "nitrogen": return "N";
            case "oxygen": return "O";
            case "iron": return "Fe";
            case "sodium": return "Na";
            case "sulfur": return "S";
            default: return null;
        }
    }

    // ================ МЕТОДЫ ДЛЯ ОПРЕДЕЛЕНИЯ ТИПОВ ДАННЫХ ================

    private String determineDataType(String data) {
        if (data == null || data.isEmpty()) return "Пусто";

        // Проверяем на формат "URL|Описание"
        if (data.contains("|")) {
            String[] parts = data.split("\\|", 2);
            if (parts.length == 2) {
                String possibleUrl = parts[0].trim();
                String description = parts[1].trim().toLowerCase();

                if (Patterns.WEB_URL.matcher(possibleUrl).matches()) {
                    // ⭐ Проверяем, не химический ли это элемент по описанию
                    if (isChemicalElement(description)) {
                        return "Химический элемент с изображением";
                    }

                    if (isImageUrl(possibleUrl)) {
                        return "Ссылка на изображение с описанием";
                    }
                    return "URL с описанием";
                }
            }
        }

        // Проверка на URL
        if (Patterns.WEB_URL.matcher(data).matches()) {
            if (isImageUrl(data)) {
                return "Ссылка на изображение";
            }
            return "URL";
        }

        // Проверка на Email
        if (Patterns.EMAIL_ADDRESS.matcher(data).matches()) {
            return "Email";
        }

        // Проверка на телефон
        if (data.matches("^[+]?[0-9\\s-]{7,}$")) {
            return "Телефон";
        }

        // Проверка на WiFi
        if (data.startsWith("WIFI:")) {
            return "WiFi";
        }

        // Проверка на Base64 изображение
        if (isBase64Image(data)) {
            return "Изображение (Base64)";
        }

        return "Текст";
    }

    private boolean isImageUrl(String url) {
        if (url == null || url.isEmpty()) return false;
        String lowerUrl = url.toLowerCase();
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".svg"};

        for (String ext : imageExtensions) {
            if (lowerUrl.contains(ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBase64Image(String data) {
        if (data == null || data.length() < 100) return false;

        try {
            String base64Data = data;
            if (data.contains(",")) {
                base64Data = data.split(",")[1];
            }

            if (!base64Data.matches("^[A-Za-z0-9+/=]+$")) {
                return false;
            }

            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);

            return options.outWidth > 0 && options.outHeight > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private Bitmap decodeBase64ToBitmap(String base64Data) {
        try {
            String base64Image = base64Data;
            if (base64Data.contains(",")) {
                base64Image = base64Data.split(",")[1];
            }
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void hideAllResultViews() {
        if (TvResultContent != null) TvResultContent.setVisibility(View.GONE);
        if (TvResultLink != null) TvResultLink.setVisibility(View.GONE);
        if (IvResultImage != null) IvResultImage.setVisibility(View.GONE);
        if (TvResultType != null) TvResultType.setVisibility(View.GONE);
    }

    private void clearCurrentResult() {
        hideAllResultViews();
        TvResultTitle.setVisibility(View.GONE);
        currentDisplayedElement = null;
    }
    private void showSimpleDescriptionDialog(String text, String type, String time) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_discription, null);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);

            TextView titleView = dialogView.findViewById(R.id.dialog_title);
            TextView typeView = dialogView.findViewById(R.id.dialog_type);
            TextView timeView = dialogView.findViewById(R.id.dialog_time);
            TextView descriptionView = dialogView.findViewById(R.id.dialog_description);
            Button closeButton = dialogView.findViewById(R.id.dialog_close);

            titleView.setText("Описание");
            typeView.setText("Тип: " + type);
            timeView.setText("Время: " + time);
            descriptionView.setText(text);

            closeButton.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

            Log.d("QR_DEBUG", "Обычный диалог показан");

        } catch (Exception e) {
            Log.e("QR_DEBUG", "Ошибка: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при открытии диалога", Toast.LENGTH_SHORT).show();
        }
    }
}