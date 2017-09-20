package com.aks.calarifaiapp;

/**
 * Created by Awais on 11/25/2016.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.aks.calarifaiapp.adapter.MyAdapter;
import com.aks.calarifaiapp.interfaces.IContentAnalyzer;
import com.aks.calarifaiapp.interfaces.IContentAnalyzerResultHandler;
import com.aks.calarifaiapp.util.ClarifaiUtil;

import java.util.List;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static com.aks.calarifaiapp.R.id.imageView;

public class Main extends AppCompatActivity implements IContentAnalyzer,IContentAnalyzerResultHandler {

    private ListView taglist;
    private ImageView image;
    private ProgressBar pbar;
    private Context context;
    private FloatingActionButton f_btn;
    private static final int PICK_IMAGE = 100;
    private byte[] imageBytes;
    private IContentAnalyzer contentAnalyzer;
    private  IContentAnalyzerResultHandler resultcontentAnalyzer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        askForPermission();
        taglist=(ListView)findViewById(R.id.listView);
        image=(ImageView)findViewById(imageView);
        pbar=(ProgressBar)findViewById(R.id.progressBar);

        f_btn=(FloatingActionButton)findViewById(R.id.fab);

        f_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);

            }
        });

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch(requestCode) {
            case PICK_IMAGE:
                final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
                if (imageBytes != null) {
                    pbar.setVisibility(View.VISIBLE);

                    contentAnalyzer=this;
                    resultcontentAnalyzer=this;

                    contentAnalyzer.analyze(imageBytes,resultcontentAnalyzer);
                 }
                break;
        }
    }

    @Override
    public void analyze(byte[] imagebyte,IContentAnalyzerResultHandler resultHandler) {

        onImagePicked(imagebyte,resultHandler);
    }

    @Override
    public void onResult( List<Concept> result) {
        MyAdapter adp=new MyAdapter(result,context);
        taglist.setAdapter(adp);

        image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

    }


    private void onImagePicked(@NonNull final byte[] imageBytes,IContentAnalyzerResultHandler resultHandler) {

        pbar.setVisibility(View.VISIBLE);
        this.imageBytes=imageBytes;
        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images

                final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();

                // Use this model to predict, with the image that the user just selected as the input
                return generalModel.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                        .executeSync();
            }

            @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                pbar.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    return;
                }
                resultcontentAnalyzer.onResult(predictions.get(0).data());
                  }

        }.execute();
    }
    private void askForPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }
}

