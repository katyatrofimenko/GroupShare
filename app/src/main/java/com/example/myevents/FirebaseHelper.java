package com.example.myevents;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    public interface DataCallback<T> {
        void onSuccess(List<T> data);

        void onFailure(Exception e);
    }

    public interface DataCallbackSpecific<T> {
        void onSuccess(T data);

        void onFailure(Exception e);
    }

    // Fetch a list of documents from a collection
    public static <T> void fetchData(String collectionPath, Class<T> modelClass, DataCallback<T> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<T> dataList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            try {
                                T item = document.toObject(modelClass);
                                if (item != null) {
                                    dataList.add(item);
                                }
                            } catch (Exception e) {
                                callback.onFailure(e);
                                return;
                            }
                        }
                        callback.onSuccess(dataList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Fetch a specific document from a collection
    public static <T> void fetchData(String collectionPath, String documentId, Class<T> modelClass, DataCallbackSpecific<T> callback) {
        // Check if documentId is valid
        if (documentId == null || documentId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Document ID cannot be null or empty"));
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath).document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                T item = document.toObject(modelClass);
                                if (item != null) {
                                    callback.onSuccess(item);
                                } else {
                                    callback.onFailure(new NullPointerException("Failed to convert document to model class"));
                                }
                            } catch (Exception e) {
                                callback.onFailure(e);
                            }
                        } else {
                            callback.onFailure(new NullPointerException("No such document"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }






    public void setDocument(String collectionName, String documentId, Map<String, Object> data) {
        FirebaseFirestore    db = FirebaseFirestore.getInstance();

        // Set the document with the specified ID
        db.collection(collectionName)
                .document(documentId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("info", "DocumentSnapshot successfully written with ID: " + documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("info", "Error writing document", e);
                    }
                });
    }
}
