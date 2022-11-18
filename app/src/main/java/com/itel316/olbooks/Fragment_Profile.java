package com.itel316.olbooks;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private User curUser;
    private Button save, discard, logout, change_profile;
    private EditText name, lastname, password;
    private boolean hasNewImage = false;

    private String newName, newLastName, newPassword;
    private DatabaseHelper dbHelper;
    private CircleImageView profile_image;
    private Bitmap bitmap;

    public Fragment_Profile() {
    }

    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__profile, container, false);
        dbHelper = new DatabaseHelper(getContext());
        curUser = (User) getArguments().getSerializable("curUser");
        curUser.fetchSelf(dbHelper);


        logout = view.findViewById(R.id.logout);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save);
        change_profile = view.findViewById(R.id.change_profile);
        ((Button) view.findViewById(R.id.exitapp)).setOnClickListener(e -> {
            getActivity().finish();
        });

        discard.setOnClickListener(e -> reInit());
        save.setOnClickListener(e -> saveChange());

        name = view.findViewById(R.id.name);
        lastname = view.findViewById(R.id.lastname);
        password = view.findViewById(R.id.password);

        profile_image = view.findViewById(R.id.profile_image);

        name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleType();
            }
        });

        lastname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleType();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleType();
            }
        });

        logout.setOnClickListener(e -> {
            dbHelper.updateUserLoginState(curUser.getUserId(), 0);
            getActivity().finishAffinity();
        });

        change_profile.setOnClickListener(e -> {
            Intent pickGal = new Intent(Intent.ACTION_PICK);
            pickGal.setType("image/*");
            pickGal.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickGal, 1000);
        });

        reInit();
        handleType();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity_Home activity_home = (Activity_Home) getActivity();
        try {
            if (resultCode == activity_home.RESULT_OK && requestCode == 1000) {
                Uri targetUri = data.getData();
                bitmap = BitmapFactory.decodeStream(activity_home.getContentResolver().openInputStream(targetUri));

                hasNewImage = true;
                profile_image.setImageBitmap(bitmap);
                handleType();
            } else if (resultCode == Activity.RESULT_CANCELED) { System.out.println("CANCELLED "); }
        } catch (Exception e) { System.out.println("Fire ERR " + e); }
    }

    void handleType() {

        newName = name.getText().toString();
        newLastName = lastname.getText().toString();
        newPassword = password.getText().toString();

        if (newName.length() == 0 || newLastName.length() == 0) return;
        boolean hasChanges = false;

        hasChanges = !newName.equals(curUser.getFname());
        hasChanges = hasChanges || !newLastName.equals(curUser.getLname());
        hasChanges = hasChanges || newPassword.length() > 0;

        if (hasChanges || hasNewImage) {
            discard.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
        } else {
            discard.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
        }
    }

    void reInit() {
        newName = "";
        newLastName = "";
        newPassword = "";
        name.setText(curUser.getFname());
        lastname.setText(curUser.getLname());
        hasNewImage = false;
        password.setText("");
        if (curUser.getImg() != null) {
            try {
                File imgFile = new File(curUser.getImg());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    profile_image.setImageBitmap(myBitmap);
                }
            } catch (Exception e) {
                System.out.println("ERR IMAGE CUZ "+e);
            }
        }
    }

    void saveChange() {
        curUser.setFname(newName);
        curUser.setLname(newLastName);

        if (password.length() > 0) {
            curUser.setPassword(AuthHelper.hashPassword(newPassword));
            System.out.println("Has New Password " + newPassword);
        }

        try{

            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir("profiles", Context.MODE_PRIVATE);
            File file = new File(directory, OlbookUtils.toISODateString(new Date()) + "_PROFILE_"
                    + OlbookUtils.randomKey(8) + ".jpg");
            if (!file.exists()) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            String abspath = file.toString();
            String prevImg = curUser.getImg();
            File toDelete = new File(prevImg);
            toDelete.delete();
            curUser.setImg(abspath);
        }catch (Exception e){}

        curUser.saveState(dbHelper);
        curUser.fetchSelf(dbHelper);

        reInit();

        save.setVisibility(View.GONE);
        discard.setVisibility(View.GONE);

        Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_LONG).show();
    }

}