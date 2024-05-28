package com.github.amiguetes.edufeedai.edufeedaijavafx.model;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.exceptions.AssessmentErrorException;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CheckResults {

    private static final String API_URL = "http://localhost:3000/grade";

    private static CheckResults _instance;
    private static int AssesmentId = 0;

    public static synchronized CheckResults getInstance(){
        if (null == _instance){
            _instance = new CheckResults();
        }
        return _instance;
    }

    private CheckResults(){

    }

    public Assessment createNewAssessment(String gradingCriteria,String taskSubmitted) throws AssessmentErrorException {

        Gson gson = new Gson();

        AssessmentBase assessmentBase = new AssessmentBase(gradingCriteria, taskSubmitted);

        String json = gson.toJson(assessmentBase);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URL(API_URL).toURI())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();

        } catch (Exception e) {
            throw new AssessmentErrorException(e);
        }



       return new Assessment(String.valueOf(++CheckResults.AssesmentId),gradingCriteria,taskSubmitted);

    }

    public void gradeAssessment(Assessment assessment) throws AssessmentErrorException {

        throw new UnsupportedOperationException("Not supported yet.");

    }

}
