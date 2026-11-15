package com.example.qrcodeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collections;

public class CustomScannerActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private boolean isScanningStopped = false;
    private static final int CAMERA_PERMISSION_CODE = 201;

    // Переменные для отслеживания времени наведения
    private String lastScannedText = null;
    private long scanStartTime = 0;
    private static final long REQUIRED_STABLE_TIME_MS = 2500; // 2.5 секунды

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeView = findViewById(R.id.barcode_scanner);
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> finish());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void startScanning() {
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(
                Collections.singletonList(com.google.zxing.BarcodeFormat.QR_CODE)));
        barcodeView.setStatusText("Наведите камеру на QR-код и держите 2-3 секунды");
        barcodeView.decodeContinuous(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Без разрешения на камеру сканирование невозможно", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (isScanningStopped) return;

            String currentText = result.getText();
            if (currentText == null) return;

            long currentTime = System.currentTimeMillis();

            // Если это тот же код, что был в прошлом кадре
            if (currentText.equals(lastScannedText)) {
                // Проверяем, сколько времени прошло с момента первого обнаружения
                if (currentTime - scanStartTime >= REQUIRED_STABLE_TIME_MS) {
                    // Время вышло! Код стабильно в кадре нужное время. Принимаем результат.
                    isScanningStopped = true;
                    barcodeView.pause();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SCAN_RESULT", currentText);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
                // Если время еще не вышло, ничего не делаем. Ждем следующий кадр.
            } else {
                // Это новый код (или первый). Запоминаем его и время начала.
                lastScannedText = currentText;
                scanStartTime = currentTime;
                // Можно добавить визуальную подсказку, например, изменить текст статуса
                barcodeView.setStatusText("Распознавание... Держите камеру");
            }
        }

        @Override
        public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!isScanningStopped) barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}