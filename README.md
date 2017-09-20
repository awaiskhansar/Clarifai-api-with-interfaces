# Clarifai-api-with-interfaces

SetUP
You can integrate that project in your own project

1) Permissions
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

2) Add name in Manifest (Application Tag)

<application
        android:name=".App"//App is te class in main default package for INSTANCE initilization

3)Interfaces

public interface IContentAnalyzer {
    public void analyze(byte[] imagebyte,IContentAnalyzerResultHandler resultHandler);
}

1) Clarifai api accept the file/image as byte[]
2) IContentAnalyzer interface with byte[] and the result interface object




public interface IContentAnalyzerResultHandler {
    public void onResult(List<Concept> result);

}

1)Clarifai api resturn the result in (List<Concept>) list of CONCEPT(calrifai api class)
2) you can use that list to poulate to listview or gridview


