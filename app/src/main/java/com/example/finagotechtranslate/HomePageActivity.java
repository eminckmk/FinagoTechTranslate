package com.example.finagotechtranslate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;

public class HomePageActivity extends AppCompatActivity {

    // İncelediğiniz için çok teşekkürlerr :))

    private EditText editTextInput, editTextOutput;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;
    private AutoCompleteTextView fromLAN,toLAN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Spinner üzerinde dil seçeneklerini kullanmak için /top
        String[] type = new String[]{"English", "Turkish", "French", "Afrikaans", "Arabic", "Catalan", "Czech", "Hindi", "Spanish", "Urdu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, type);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.toLAN);
        AutoCompleteTextView editTextFilledExposedDropdown2 = findViewById(R.id.fromLAN);

        editTextFilledExposedDropdown.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.filter_spinner_dropdown_bg, null));
        editTextFilledExposedDropdown.setAdapter(adapter);

        editTextFilledExposedDropdown2.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.filter_spinner_dropdown_bg, null));
        editTextFilledExposedDropdown2.setAdapter(adapter);
        //Spinner üzerinde dil seçeneklerini kullanmak için /down


        toLAN = findViewById(R.id.toLAN);
        fromLAN = findViewById(R.id.fromLAN);
        editTextInput = findViewById(R.id.editTextInput);
        editTextOutput = findViewById(R.id.editTextOutput);
        Button buttonTranslate = findViewById(R.id.buttonTranslate);
        ImageButton buttonChangeLAN = findViewById(R.id.buttonChangeLAN);

        //Dilleri ve çevirlecek-çevirilmiş metinleri paralel değiştirmek için /top
        buttonChangeLAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String hold = " ";
                    hold = editTextInput.getText().toString();
                    editTextInput.setText(editTextOutput.getText());
                    editTextOutput.setText(hold);

                    int pos = -1;
                    int pos2 = -1;
                    for (int i = 0; i < 9; i++) {
                        if (type[i].equals(toLAN.getText().toString())) {
                            pos = i;
                        }
                        if (type[i].equals(fromLAN.getText().toString())) {
                            pos2 = i;
                        }
                    }

                    fromLAN.setText(fromLAN.getAdapter().getItem(pos).toString(), false);
                    toLAN.setText(toLAN.getAdapter().getItem(pos2).toString(), false);
                }catch (Exception e){
                    e.printStackTrace();
                    //Dillerin seçilmediği durumda uygulamanın patlamaması için (Toast Mesaj ile uyarı sağlanabilir)
                }
            }
        });
        //Dilleri ve çevirlecek-çevirilmiş metinleri paralel değiştirmek için /down

        //Buttona basıldığı zaman çeviri işlemlerini yapması için /top
        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (checkInternetConnection()) {

                        getTranslateService();
                        translate();

                    } else { Toast.makeText(getApplicationContext(),"An unknown error occurred",Toast.LENGTH_LONG).show(); }
                }catch (Exception e){
                    e.printStackTrace();
                    //Çeviriyi başlattığımızda boş metin uygulamayı patlatmaması için (Toast Mesaj ile uyarı sağlanabilir)
                }

            }
        });
        //Buttona basıldığı zaman çeviri işlemlerini yapması için /down
    }

    //Google cloud üzerindeki projemize erişim için gerekli (Json haline çevirdiğimiz) keyi bağladığımız bölüm /top
    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.keyapi)) {

            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) { ioe.printStackTrace();}
    }
    //Google cloud üzerindeki projemize erişim için gerekli (Json haline çevirdiğimiz) keyi bağladığımız bölüm /down

    //çeviri işlemleri /top
    public void translate() {

        String getFromLAN = language(fromLAN.getText().toString());
        String getToLAN = language(toLAN.getText().toString());

        originalText = editTextInput.getText().toString();
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage(getFromLAN), Translate.TranslateOption.sourceLanguage(getToLAN));
        translatedText = translation.getTranslatedText();

        editTextOutput.setText(translatedText);
    }
    //çeviri işlemleri /down

    //Internet kontrolünü sağlayan fonksiyon /top
    public boolean checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
    //Internet kontrolünü sağlayan fonksiyon /down


    //Dil kontrolünü sağlayan fonksiyonum //top
    public String language(String fromLanguage) {

        String test;
        switch (fromLanguage) {
            case "Turkish":
                test = "tr";
                break;
            case "English":
                test = "en";
                break;
            case "Afrikaans":
                test = "af";
                break;
            case "Arabic":
                test = "ar";
                break;
            case "Catalan":
                test = "ca";
                break;
            case "Czech":
                test = "cs";
                break;
            case "French":
                test = "fr";
                break;
            case "Hindi":
                test = "hi";
                break;
            case "Spanish":
                test = "es";
                break;
            case "Urdu":
                test = "ur";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + fromLanguage);
        }return test;
    }
    //Dil kontrolünü sağlayan fonksiyonum //down
}




