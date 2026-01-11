package com.example.eco_route.firebase;

import com.example.eco_route.HistoryModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

public class HistoryFirestoreRepository {

    private static final FirebaseFirestore db =
            FirebaseUtil.getDb();

    public static void addHistory(String userId, HistoryModel history) {
        db.collection("users")
                .document(userId)
                .collection("history")
                .add(history);
    }

    public static void deleteHistory(String userId, String historyId) {
        db.collection("users")
                .document(userId)
                .collection("history")
                .document(historyId)
                .delete();
    }

    public static void deleteAllHistory(String userId) {
        db.collection("users")
                .document(userId)
                .collection("history")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot) {
                        doc.getReference().delete();
                    }
                });
    }

    public static void getHistory(String userId,
                                  OnSuccessListener<QuerySnapshot> success) {

        db.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(success);
    }
}
