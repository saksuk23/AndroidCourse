package com.example.androidcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private ImageView imageView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find relevant components
        input = findViewById(R.id.txt_input);
        imageView = findViewById(R.id.DownloadedImage);

        //using the 3rd party module
        client = new OkHttpClient();

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the text in the field and put it in request
                final String imageUrl = input.getText().toString();
                final Request request = new Request.Builder()
                        .url(imageUrl)
                        .build();
                //try to response on different thread then the main UI thread
                //a better solution is to use Handlers
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Get response from url
                            Response response = client.newCall(request).execute();

                            if (response.body() != null) {
                                //convert body of the response to bitmap for imageView
                                InputStream inputStream = response.body().byteStream();
                                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //put the bitmap to the ImageView
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
