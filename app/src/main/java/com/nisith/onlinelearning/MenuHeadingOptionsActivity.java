package com.nisith.onlinelearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nisith.onlinelearning.Adapters.MenuOptionsRecyclerAdapter;
import com.nisith.onlinelearning.Model.MenuItem;

public class MenuHeadingOptionsActivity extends AppCompatActivity  implements MenuOptionsRecyclerAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MenuOptionsRecyclerAdapter adapter;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_heading_options);
        initializeViews();
        collectionReference = FirebaseFirestore.getInstance().collection(Constant.TOPICS);
        setupRecyclerViewWithAdapter();
    }


    private void initializeViews(){
        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);
        setTitle("");
        TextView toolbarTextView = findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("Edit");
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setupRecyclerViewWithAdapter(){
        Query query = collectionReference.orderBy(Constant.TITLE);
        FirestoreRecyclerOptions<MenuItem> recyclerOptions = new FirestoreRecyclerOptions.Builder<MenuItem>()
                .setQuery(query, MenuItem.class)
                .build();
        adapter = new MenuOptionsRecyclerAdapter(recyclerOptions, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }
    }


    @Override
    public void onItemClick(String title, DocumentReference documentReference) {
        Intent intent = new Intent(this, MenuItemsActivity.class);
        intent.putExtra(Constant.DOCUMENT_ID, documentReference.getId());
        intent.putExtra(Constant.TITLE, title);
        startActivity(intent);
    }
}