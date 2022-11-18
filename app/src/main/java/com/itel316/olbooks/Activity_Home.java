package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;

import com.itel316.olbooks.databinding.ActivityHomeBinding;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.User;

import java.nio.BufferUnderflowException;
import java.util.List;

public class Activity_Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    private User currentUser;
    private Fragment curFrag;

    Intent dataProvided;

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

        binding.bottomNavigationView.setOnItemSelectedListener(nav_item -> {

            int id = nav_item.getItemId();
            Bundle bund = new Bundle();
            currentUser.fetchSelf(dbHelper);
            bund.putSerializable("curUser", currentUser);

            if (id == R.id.navigation_home) {
                Fragment_Home fragHome = new Fragment_Home();
                fragHome.setArguments(bund);
                switchFragment(fragHome);
            }

            if (id == R.id.navigation_bookmarks) {
                Fragment_Bookmarks fragBookmarks = new Fragment_Bookmarks();
                fragBookmarks.setArguments(bund);
                switchFragment(fragBookmarks);
            }

            if (id == R.id.navigation_profiles) {
                Fragment_Profile fragProfile = new Fragment_Profile();
                fragProfile.setArguments(bund);
                switchFragment(fragProfile);
            }

            return true;
        });
    }

    public void switchFragment(Fragment frag) {
        if (curFrag != null && frag.getClass() == curFrag.getClass())
            return;

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        curFrag = frag;

        if (fragMan.getBackStackEntryCount() > 8) fragMan.popBackStack();

        fragTrans.replace(R.id.frame_fragment, frag).addToBackStack("root_frag").commit();
    }

    @Override
    public void onBackPressed() {
        int cnt = getSupportFragmentManager().getBackStackEntryCount();
        if (cnt > 1) {
            getSupportFragmentManager().popBackStackImmediate();
            List<Fragment> frags = getSupportFragmentManager().getFragments();

            int foc = 0;

            if (frags.size() == 0) {
                foc = 0;
            } else {
                Fragment frag = getSupportFragmentManager().getFragments().get(frags.size() > 0 ? frags.size() - 1 : 0);
                curFrag = frag;

                if (frag.getClass() == Fragment_Home.class) foc = 0;
                if (frag.getClass() == Fragment_Bookmarks.class) foc = 1;
                if (frag.getClass() == Fragment_Profile.class) foc = 2;
            }

            if(foc != cnt){
                binding.bottomNavigationView.getMenu().getItem(foc).setChecked(true);
            }
        } else finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 1000) {
                dataProvided = data;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("CANCELLED ");
            }
        } catch (Exception e) {
            System.out.println("Fire ERR " + e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}