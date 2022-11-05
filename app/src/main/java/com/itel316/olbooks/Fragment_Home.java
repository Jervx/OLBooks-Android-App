package com.itel316.olbooks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

import java.util.ArrayList;

public class Fragment_Home extends Fragment implements Dialog_Fragment_One_Input.DialogListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private User curUser;
    private DatabaseHelper dbHelper;
    private ArrayList<Chip> category_tags;

    private Book books[];

    private ChipGroup chipContainer;
    private Button btn_addCategoryTag;
    private ViewGroup container;
    private TextView textview_tags;

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

        chipContainer = (ChipGroup) view.findViewById(R.id.chip_container);
        btn_addCategoryTag = (Button) view.findViewById(R.id.btn_add_filter);
        textview_tags = (TextView) view.findViewById(R.id.textview_tags);

        btn_addCategoryTag.setOnClickListener(e -> {
            openDialog();
        });

        dbHelper = new DatabaseHelper(getContext());

        curUser = (User) getArguments().getSerializable("curUser");
        loadBooks();

        return view;
    }

    public void openDialog(){
        Dialog_Fragment_One_Input dialog_Fragment_one_input = new Dialog_Fragment_One_Input();
        Bundle bund = new Bundle();
        bund.putString("title", "Add Tag");
        dialog_Fragment_one_input.setArguments(bund);
        dialog_Fragment_one_input.setTargetFragment(Fragment_Home.this, 1);
        dialog_Fragment_one_input.show(getParentFragmentManager(), "Add Tag");
    }


    @Override
    public void applyTexts(String fieldOne) {
        Chip tag_chip = new Chip(getActivity());
        tag_chip.setText(fieldOne);
        tag_chip.setCloseIconVisible(true);
        tag_chip.setTextColor(getResources().getColor(R.color.riverbed));

        tag_chip.setOnCloseIconClickListener(e->{
            category_tags.remove(tag_chip);
            updateChipContainer();
        });

        category_tags.add(tag_chip);
        updateChipContainer();
    }

    public void updateChipContainer(){
        chipContainer.removeAllViews();
        for(Chip chip : category_tags){
            chipContainer.addView(chip);
        }
        loadBooks();
    }

    public void loadBooks(){
        String chosenTags[] = new String[category_tags.size()];
        int counter = 0;
        for(Chip chp : category_tags){
            chosenTags[counter] = (String) category_tags.get(counter).getText();
            counter++;
        }

        books = dbHelper.getBooksByTag(chosenTags);
        for (Book book : books) System.out.println(book.toString());

        textview_tags.setText(String.format("Tags (%d Book Matched)", books.length));
    }
}