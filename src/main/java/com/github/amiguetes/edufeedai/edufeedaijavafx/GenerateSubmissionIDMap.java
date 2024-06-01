package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.Digest;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;
import com.google.gson.Gson;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.stream;

public class GenerateSubmissionIDMap {

    String assessmentFolder;
    Digest digest;

    public GenerateSubmissionIDMap(String assessmentFolder, Digest digest){
        this.assessmentFolder = assessmentFolder;
        this.digest = digest;
    }

    public SubmissionIdMap[] generateSubmissionIDMaps(){

        File folder = new File(assessmentFolder);

        File[] files = folder.listFiles(File::isDirectory);
        SubmissionIdMap[] submissionIdMaps = new SubmissionIdMap[files.length];

        for (int i = 0; i < files.length; i++) {
            SubmissionIdMap submissionIdMap = new SubmissionIdMap();
            submissionIdMap.setCustom_id(files[i].getName());

            try {
                submissionIdMap.setSubmission_id(digest.digest(files[i].getName()));
            } catch (NoSuchAlgorithmException e) {
                submissionIdMap.setSubmission_id("errorgeneratingname");
            }

            submissionIdMaps[i] = submissionIdMap;
        }

        List<SubmissionIdMap> lista = stream(submissionIdMaps).sorted(Comparator.comparing(SubmissionIdMap::getCustom_id)).toList();

        return lista.toArray(submissionIdMaps);
    }

    public String saveSubmissionIDMaps(SubmissionIdMap[] submissionIdMaps){
        Gson gson = new Gson();

        String json = gson.toJson(submissionIdMaps);

        File file = new File(assessmentFolder + File.separator + "id_map.json");

        try {
            java.nio.file.Files.writeString(file.toPath(), json);
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }

}