package com.polinc.movieappex.test2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.R;
import com.polinc.movieappex.databinding.ActivityLoginBinding;
import com.polinc.movieappex.databinding.ActivityUsersBinding;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;
    ActivityUsersBinding binding;
    RecyclerView rvContacts;
    ContactsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);

        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lookup the recyclerview in activity layout
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
          adapter = new ContactsAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!

        binding.fabMoreUsers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Hello",2000 ).show();
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

                //Add new elements
                int curSize = adapter.getItemCount();
                ArrayList<Contact> newItems = Contact.createContactsList(20);
                contacts.addAll(newItems);
                adapter.notifyItemRangeInserted(curSize, newItems.size());
            }
        });
    }
}