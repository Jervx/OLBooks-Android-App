package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.itel316.olbooks.databinding.ActivityHomeBinding;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.User;

import java.nio.BufferUnderflowException;

public class Activity_Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    private User currentUser;
    private Fragment curFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        dbHelper.updateUserLoginState(currentUser.getUserId(), 1);

        Fragment_Home initial_frag = new Fragment_Home();

        Bundle initialBundle = new Bundle();
        initialBundle.putSerializable("curUser", currentUser);

        initial_frag.setArguments(initialBundle);
        switchFragment(initial_frag);

        binding.bottomNavigationView.setOnItemSelectedListener( nav_item -> {

            int id = nav_item.getItemId();
            Bundle bund = new Bundle();
            currentUser.fetchSelf(dbHelper);
            bund.putSerializable("curUser", currentUser);
            System.out.println("FETCHED -> "+ currentUser.getEmail());

            if(id == R.id.navigation_home){
                Fragment_Home fragHome = new Fragment_Home();
                fragHome.setArguments(bund);
                switchFragment(fragHome);
            }

            if(id == R.id.navigation_search){
                Fragment_Search fragSearch = new Fragment_Search();
                fragSearch.setArguments(bund);
                switchFragment(fragSearch);
            }

            if(id == R.id.navigation_bookmarks){
                Fragment_Bookmarks fragBookmarks = new Fragment_Bookmarks();
                fragBookmarks.setArguments(bund);
                switchFragment(fragBookmarks);
            }

            if(id == R.id.navigation_profiles){
                Fragment_Profile fragProfile = new Fragment_Profile();
                fragProfile.setArguments(bund);
                switchFragment(fragProfile);
            }

            return true;
        } );
    }
    
    private void switchFragment(Fragment frag){
        if(curFrag != null && frag.getClass() == curFrag.getClass()) {
            System.out.println("Current Fragment is the same thus not changed fragment");
            return;
        }
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        curFrag = frag;

        fragTrans.replace(R.id.frame_fragment, frag);
        fragTrans.commit();
    }
}