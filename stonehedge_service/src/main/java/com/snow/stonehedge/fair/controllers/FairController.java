package com.snow.stonehedge.fair.controllers;

import com.snow.stonehedge.fair.database.functions.DatabaseFunctions;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Slf4j
@RestController("/fair")
public class FairController {

    private DatabaseFunctions databaseFunctions;

    public FairController(DatabaseFunctions databaseFunctions) {
        this.databaseFunctions = databaseFunctions;
    }

    @GetMapping("/v1/worked")
    public String worked() {
        return "It worked!";
    }

//    @GetMapping("/v1/candidate/get/{documentName}")
//    public String getCandidateInformation(@PathVariable String documentName) throws ExecutionException, InterruptedException {
//        return databaseFunctions.getDocument(documentName);
//    }

    @GetMapping("/v1/candidate/add/{documentName}")
    public String addCandidateInformation(@PathVariable String documentName) throws ExecutionException, InterruptedException {
        HashMap<String, Object> inputValues = new HashMap<>();

        inputValues.put("degreeName", "CS");
        inputValues.put("degreeYears", 5);
        inputValues.put("hasCompletedCollege", false);
        inputValues.put("minimumSalary", 75250);

        return "";
    }

}

