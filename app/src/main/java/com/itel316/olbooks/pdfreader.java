package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class pdfreader extends AppCompatActivity {

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    private PDFView reader;
    private String pdf;

    private boolean nightMode = false;
    private ImageButton themtoggler, exit;

    private TextView pageIndicator;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        getSupportActionBar().hide();

        pageIndicator = findViewById(R.id.pageIndicator);
        themtoggler = findViewById(R.id.themtoggler);
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(e -> finish());
        themtoggler.setOnClickListener(e -> {
            nightMode = !nightMode;
            themtoggler.setImageResource(nightMode ? R.drawable.ic_dark : R.drawable.ic_light);
            Toast.makeText(this, nightMode ? "🌒 Dark Mode" : "☀ Light Mode", Toast.LENGTH_LONG).show();
            configureReader();
        });

        reader = findViewById(R.id.pdfView);
        pdf = getIntent().getStringExtra("pdf");

        reader.fromAsset(pdf)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(nightMode) // toggle night mode
                .onPageChange((p, i) -> {
                    pageIndicator.setText(String.format("%d / %d", p + 1, i));
                    totalPage = i + 1;
                })
                .load();

        pageIndicator.setOnClickListener(e -> {
            Dialog tagDialog = new Dialog(this);
            tagDialog.setContentView(R.layout.jumpage);

            TextView pagesindicator = tagDialog.findViewById(R.id.pagesindicator);
            EditText pagejumpinpt = tagDialog.findViewById(R.id.pagejumpinpt);
            Button jumpage = tagDialog.findViewById(R.id.jumpage);

            pagesindicator.setText("1 - "+totalPage);
            jumpage.setOnClickListener(er -> {
                int tojump = Integer.parseInt(pagejumpinpt.getText().toString());
                if(tojump - 1 > totalPage - 1){
                    Toast.makeText(this, "This book only have 1 - "+totalPage + " 😕", Toast.LENGTH_LONG).show();
                    return;
                }
                reader.fromAsset(pdf)
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(tojump - 1)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        .spacing(0)
                        .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                        .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                        .pageSnap(true) // snap pages to screen boundaries
                        .pageFling(false) // make a fling change only a single page like ViewPager
                        .nightMode(nightMode) // toggle night mode
                        .onPageChange((p, i) -> {
                            pageIndicator.setText(String.format("%d / %d", p + 1, i ));
                            totalPage = i;
                        })
                        .load();
                tagDialog.dismiss();
            });
            tagDialog.setOnDismissListener(dism -> {
                hideSystemUI();
            });
            tagDialog.show();
            Window window = tagDialog.getWindow();
        });
    }

    public void configureReader () {
        reader.setNightMode(nightMode);
        reader.loadPages();
    }
}