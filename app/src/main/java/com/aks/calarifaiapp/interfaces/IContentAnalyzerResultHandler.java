package com.aks.calarifaiapp.interfaces;

import java.util.List;

import clarifai2.dto.prediction.Concept;

/**
 * Created by syedshah on 10/27/16.
 */

public interface IContentAnalyzerResultHandler {
    public void onResult(List<Concept> result);

}
