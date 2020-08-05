package com.snow.stonehedge.fair.database.functions;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import com.snow.stonehedge.fair.database.DatabaseCollections;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
public class DatabaseFunctions {

    private Firestore firestore;

    public DatabaseFunctions(Firestore firestore) {
        this.firestore = firestore;
    }

    public String getDocument(DatabaseCollections collection, String documentID) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection(collection.getDatabaseName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return "Found the document ["  + documentID + "]. The document contains symbol: \n " + document.getString("symbol");
        } else {
            return "Could not find the document ["  + documentID + "]";
        }
    }

    public void addDocument(DatabaseCollections collection, HashMap<String, Object> values) throws ExecutionException, InterruptedException {
        String documentID = values.get("id").toString();
        DocumentReference documentReference = firestore.collection(collection.getDatabaseName()).document(documentID);
        ApiFuture<WriteResult> future = documentReference.set(values);
        log.info("Successfully added [" + documentID + "] into the database collection [" + collection + "] at -> "  + future.get().getUpdateTime());
    }

}
