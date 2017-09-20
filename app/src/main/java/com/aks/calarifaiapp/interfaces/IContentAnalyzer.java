package com.aks.calarifaiapp.interfaces;

/**
 * Created by syedshah on 10/27/16.
 */

public interface IContentAnalyzer {
    public void analyze(byte[] imagebyte,IContentAnalyzerResultHandler resultHandler);
}
