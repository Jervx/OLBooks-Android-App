package com.itel316.olbooks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.User;

public class Fragment_Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private User curUser;

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
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        Button btn_signout = (Button) view.findViewById(R.id.btn_sign_out);

        btn_signout.setOnClickListener(e->{
            dbHelper.updateUserLoginState(curUser.getUserId(), 0);
            getActivity().finishAffinity();
        });

        return view;
    }
}