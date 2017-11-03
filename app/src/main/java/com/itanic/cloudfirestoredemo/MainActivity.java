package com.itanic.cloudfirestoredemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.itanic.cloudfirestoredemo.adapter.UserAdapter;
import com.itanic.cloudfirestoredemo.databinding.ActivityMainBinding;
import com.itanic.cloudfirestoredemo.model.Hobbies;
import com.itanic.cloudfirestoredemo.model.User;

public class MainActivity extends AppCompatActivity {

    /**
     * Instance for Firestore Database
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Used to with DataBinding
     */
    ActivityMainBinding activityMainBinding;

    /**
     * Adapter to Binding Data
     */
    private UserAdapter mAdapter;

    /**
     * Listening onStart if Any Changes
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    /**
     * Stopping listen
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);

        /**
         * Querying for Getting Data
         */
        Query mQuery = db.collection("users");
        mAdapter = new UserAdapter(mQuery) {

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                // Show/hide content if the query returns empty.
                Log.d("TAG", "onDataChanged: " + getItemCount());
                if (getItemCount() == 0) {
                    activityMainBinding.contentMain.recyclerView.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Nothing to show..", Toast.LENGTH_SHORT).show();
                } else {
                    activityMainBinding.contentMain.recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content), "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        /**
         * Binding Data / Setting Adapter
         */
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        activityMainBinding.contentMain.recyclerView.setLayoutManager(manager);
        activityMainBinding.contentMain.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        activityMainBinding.contentMain.recyclerView.setAdapter(mAdapter);

        /***
         * On Click of FAB Button...
         * Inserting new record on Firestore Database
         */
        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = activityMainBinding.contentMain.editTextName.getText().toString();
                String lang = activityMainBinding.contentMain.editTextLanguage.getText().toString();


                /**
                 * Defining Model
                 */
                Hobbies hobbies = new Hobbies();
                hobbies.setHobby1("Playing Cricket");
                hobbies.setHobby2("Listening  Cricket");

                User user = new User();
                user.setName(name);
                user.setLanguage(lang);
                user.setHobbies(hobbies);

                // Add a new document with a generated ID
                db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        activityMainBinding.contentMain.editTextName.setText("");
                        activityMainBinding.contentMain.editTextLanguage.setText("");
                        activityMainBinding.contentMain.editTextName.requestFocus();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
