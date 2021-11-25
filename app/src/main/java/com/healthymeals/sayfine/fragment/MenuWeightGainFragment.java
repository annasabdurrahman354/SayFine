package com.healthymeals.sayfine.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.CrudMenuActivity;
import com.healthymeals.sayfine.adapter.crud.MenuListAdapter;
import com.healthymeals.sayfine.model.Menu;

import java.util.ArrayList;

public class MenuWeightGainFragment extends Fragment {
    private RecyclerView recyclerView;
    private MenuListAdapter adapter;
    private ArrayList<Menu> list;
    private FirebaseFirestore firebaseFirestore;

    public MenuWeightGainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_type, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getMenus();
        adapter = new MenuListAdapter(getContext(), list);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getMenus() {
        firebaseFirestore.collection("Menus").whereEqualTo("type", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(getParentFragment().getContext(), "Gagal mendapatkan data menu!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Menu menu;
                            menu = new Menu(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("goFoodUrl"), dc.getDocument().getString("grabFoodUrl"), dc.getDocument().getString("shopeeFoodUrl"), dc.getDocument().getString("thumbUrl"), ((Number) dc.getDocument().getDouble("price")).intValue(), ((Number) dc.getDocument().getDouble("calorie")).intValue(), dc.getDocument().getBoolean("type"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(menu);
                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
