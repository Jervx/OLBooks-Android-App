package com.itel316.olbooks;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private User curUser;
    private DatabaseHelper dbHelper;
    private ArrayList<Chip> category_tags;

    private Book books[];

    private ChipGroup chipContainer;
    private ImageButton btn_addCategoryTag;
    private ViewGroup container;
    private TextView textview_tags, greets;
    private ViewPager2 swipe_view;
    private EditText searchField;
    private View parentView;
    private ImageView nofound;

    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    public Fragment_Home() {

    }

    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
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

        View view = inflater.inflate(R.layout.fragment__home, container, false);
        category_tags = new ArrayList<>();
        this.container = container;

        searchField = (EditText) view.findViewById(R.id.search_field);
        chipContainer = (ChipGroup) view.findViewById(R.id.chip_container);
        btn_addCategoryTag = (ImageButton) view.findViewById(R.id.btn_send_new_pass);
        textview_tags = (TextView) view.findViewById(R.id.textview_tags);
        swipe_view = (ViewPager2) view.findViewById(R.id.viewpager_swipe);
        greets = (TextView) view.findViewById((R.id.textView_greet));
        viewPagerItemArrayList = new ArrayList<>();

        parentView = getActivity().findViewById(R.id.frame_fragment);
        nofound = view.findViewById(R.id.nofound);

        btn_addCategoryTag.setOnClickListener(e -> {
            openDialog();
        });

        searchField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks();
            }
        });

        dbHelper = new DatabaseHelper(getContext());

        curUser = (User) getArguments().getSerializable("curUser");
        loadBooks();
        return view;
    }

    public void openDialog() {
        Dialog tagDialog = new Dialog(getContext());
        tagDialog.setContentView(R.layout.tagfilter);
        ImageView close = (ImageView) tagDialog.findViewById(R.id.close);
        Button btn_add_filter = (Button) tagDialog.findViewById(R.id.btn_send_new_pass);

        close.setOnClickListener(e -> { tagDialog.dismiss(); });

        btn_add_filter.setOnClickListener(e -> {
            Chip tag_chip = new Chip(getActivity());
            tag_chip.setText(((EditText)tagDialog.findViewById(R.id.email_input)).getText().toString());
            tag_chip.setCloseIconVisible(true);
            tag_chip.setTextColor(getResources().getColor(R.color.Bg));
            tag_chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.CoffeeBlack_700)));

            tag_chip.setOnCloseIconClickListener(ev -> {
                category_tags.remove(tag_chip);
                updateChipContainer();
            });

            category_tags.add(tag_chip);
            updateChipContainer();
            tagDialog.dismiss();
        });

        tagDialog.show();
    }


    public void updateChipContainer() {
        chipContainer.removeAllViews();
        for (Chip chip : category_tags) {
            chipContainer.addView(chip);
        }
        loadBooks();
    }

    public void loadBooks() {
        int additional = searchField.getText().toString().length() > 0 ? 1 : 0;
        String chosenTags[] = new String[category_tags.size() + additional];

        if(additional == 1)  chosenTags[chosenTags.length - 1] = searchField.getText().toString();

        int counter = 0;
        for (Chip chp : category_tags) {
            chosenTags[counter] = (String) chp.getText();
            counter++;
        }

        books = dbHelper.getBooksByTag(chosenTags);
        viewPagerItemArrayList = new ArrayList<>();
        for (Book book : books) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(book);
            viewPagerItemArrayList.add(viewPagerItem);
        }


        textview_tags.setText(String.format("%d book found", books.length));
        greets.setText("Hi " + curUser.getFname());
        if(books.length == 0) nofound.setVisibility(View.VISIBLE);
        else nofound.setVisibility(View.GONE);
        renderViewPager();
    }

    public void renderViewPager() {
        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList, (Activity_Home) getActivity(), getActivity().findViewById(R.id.frame_fragment), curUser, dbHelper);

        swipe_view.setAdapter(vpAdapter);
        swipe_view.setClipToPadding(false);
        swipe_view.setClipChildren(false);
        swipe_view.setOffscreenPageLimit(2);
        swipe_view.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer comptrans = new CompositePageTransformer();
        comptrans.addTransformer(new MarginPageTransformer(40));
        comptrans.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        swipe_view.setPageTransformer(comptrans);
    }

}