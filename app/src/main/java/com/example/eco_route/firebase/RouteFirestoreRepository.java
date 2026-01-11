package com.example.eco_route.firebase;

import com.example.eco_route.RouteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.List;

public class RouteFirestoreRepository {

    private static final FirebaseFirestore db =
            FirebaseUtil.getDb();

    public static void addRoute(RouteModel route) {
        db.collection("routes")
                .add(route);
    }

    public static void updateRoute(String routeId, RouteModel route) {
        db.collection("routes")
                .document(routeId)
                .set(route);
    }

    public static void deleteRoute(String routeId) {
        db.collection("routes")
                .document(routeId)
                .delete();
    }

    public static void getAllRoutes(OnSuccessListener<QuerySnapshot> success) {
        db.collection("routes")
                .get()
                .addOnSuccessListener(success);
    }
}
