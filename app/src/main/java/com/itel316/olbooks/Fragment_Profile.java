package com.itel316.olbooks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.itel316.olbooks.models.User;

public class Fragment_Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private User curUser;
    private Button save, discard, logout;
    private EditText name, lastname, password;

    private String newName, newLastName, newPassword;
    private DatabaseHelper dbHelper;

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
        curUser = (User) getArguments().getSerializable("curUser");
        dbHelper = new DatabaseHelper(getContext());

        logout = view.findViewById(R.id.logout);
        discard = view.findViewById(R.id.discard);
        save = view.findViewById(R.id.save);

        discard.setOnClickListener(e -> reInit());
        save.setOnClickListener(e -> saveChange());

        name = view.findViewById(R.id.name);
        lastname = view.findViewById(R.id.lastname);
        password = view.findViewById(R.id.password);

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

        reInit();
        handleType();
        return view;
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

        if (hasChanges) {
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
        password.setText("");
    }

    void saveChange() {
        curUser.setFname(newName);
        curUser.setLname(newLastName);
        if (password.length() > 0) {
            curUser.setPassword(AuthHelper.hashPassword(newPassword));
            System.out.println("Has New Password "+newPassword);
        }

        curUser.saveState(dbHelper);
        curUser.fetchSelf(dbHelper);

        reInit();

        save.setVisibility(8);
        discard.setVisibility(8);

        Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_LONG).show();
    }

}