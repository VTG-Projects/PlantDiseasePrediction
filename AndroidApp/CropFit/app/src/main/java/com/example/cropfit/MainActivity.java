package com.example.cropfit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cropfit.ml.Model;
import com.example.cropfit.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button select, predict;
    private TextView tv;
    private Bitmap img;
    private Bitmap img1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView);
        select = (Button) findViewById(R.id.button);
        predict = (Button) findViewById(R.id.button2);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = Bitmap.createScaledBitmap(img, 224, 224, true);
                try {
                    ModelUnquant model_unquant = ModelUnquant.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature1 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                    TensorImage tensorImage1 = new TensorImage(DataType.FLOAT32);
                    tensorImage1.load(img);
                    ByteBuffer byteBuffer1 = tensorImage1.getBuffer();
                    inputFeature1.loadBuffer(byteBuffer1);
                    // Runs model inference and gets result.
                    ModelUnquant.Outputs outputs1 = model_unquant.process(inputFeature1);
                    TensorBuffer outputFeature1 = outputs1.getOutputFeature0AsTensorBuffer();
                    // Releases model resources if no longer used.
                    model_unquant.close();
                    // Fetching results in res from model
                    float[] res1 = new float[2];
                    for(int i = 0; i <= 1; i++) {
                        res1[i] = (outputFeature1.getFloatArray()[i]);
                    }
                    // Getting index of maximum value in results array
                    int maxAt1 = 0;
                    for (int i = 0; i < res1.length; i++) {
                        maxAt1 = res1[i] > res1[maxAt1] ? i : maxAt1;
                    }
                    // Printing className of Detected Disease
                    String classNames1[] = {"Valid", "Invalid"};
                    String str1_1 = classNames1[maxAt1];

                    if(str1_1 == "Valid") {
                        img = Bitmap.createScaledBitmap(img, 200, 200, true);
                        try {
                            Model model = Model.newInstance(getApplicationContext());
                            // Creates inputs for reference.
                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 200, 200, 3}, DataType.FLOAT32);

                            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                            tensorImage.load(img);
                            ByteBuffer byteBuffer = tensorImage.getBuffer();

                            inputFeature0.loadBuffer(byteBuffer);

                            // Runs model inference and gets result.
                            Model.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                            // Releases model resources if no longer used.
                            model.close();
                            // Fetching results in res from model
                            float[] res = new float[41];
                            for (int i = 0; i <= 40; i++) {
                                res[i] = (outputFeature0.getFloatArray()[i]);
                            }
                            // Getting index of maximum value in results array
                            int maxAt = 0;
                            for (int i = 0; i < res.length; i++) {
                                maxAt = res[i] > res[maxAt] ? i : maxAt;
                            }
                            // Printing className of Detected Disease
                            String classNames[] = {"Apple___Apple_scab", "Apple___Black_rot", "Apple___Cedar_apple_rust", "Apple___healthy", "Cherry_(including_sour)___Powdery_mildew", "Cherry_(including_sour)___healthy", "Chili__healthy", "Chili__leaf curl", "Chili__leaf spot", "Chili__whitefly", "Chili__yellowish", "Coffee__Rust", "Coffee__healthy", "Coffee__red spider mite", "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot", "Corn_(maize)___Common_rust_", "Corn_(maize)___Northern_Leaf_Blight", "Corn_(maize)___healthy", "Grape___Black_rot", "Grape___Esca_(Black_Measles)", "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)", "Grape___healthy", "Peach___Bacterial_spot", "Peach___healthy", "Pepper,_bell___Bacterial_spot", "Pepper,_bell___healthy", "Potato___Early_blight", "Potato___Late_blight", "Potato___healthy", "Strawberry___Leaf_scorch", "Strawberry___healthy", "Tomato___Bacterial_spot", "Tomato___Early_blight", "Tomato___Late_blight", "Tomato___Leaf_Mold", "Tomato___Septoria_leaf_spot", "Tomato___Spider_mites Two-spotted_spider_mite", "Tomato___Target_Spot", "Tomato___Tomato_Yellow_Leaf_Curl_Virus", "Tomato___Tomato_mosaic_virus", "Tomato___healthy"};
                            String str1 = classNames[maxAt];
                            String str2 = Float.toString(res[maxAt]);
                            tv.setText("The predicted disease is " + str1 + " with a strength of " + str2 + ".");
                        }catch (IOException e1) {
                            // TODO Handle the exception
                        }
                    }else{
                        tv.setText("Invalid Input");
                    }
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            imgView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}