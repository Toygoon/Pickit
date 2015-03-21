package kr.prizm.pickit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class CardActivity extends ActionBarActivity {

    private ViewGroup rowContainer;
    private static final int SCALE_DELAY = 200;
    private View view;
    private View view2;
    private Toolbar toolbar;
    private DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton share_btn;
    private ImageButton delete_btn;
    private String tmp2;

    String TITLES[] = {"Text","Photo","Favorite"};
    int ICONS[] = {R.drawable.m1,R.drawable.m2,R.drawable.m3};

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    String NAME = "Name";
    String EMAIL = "Email";
    int PROFILE = R.drawable.owner;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);


        // 툴바를 액션바로 지정

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#c847c0"));

        // 드로어 레이아웃 추가

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        Drawer.setDrawerListener(mDrawerToggle);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;

                        }
                    }
                })
        );


        // 카드 애니메이션
        rowContainer = (LinearLayout) findViewById(R.id.row_container);
        for (int i = 0; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate().setStartDelay(100 + i * SCALE_DELAY).scaleX(1).scaleY(1);
        }

        // 버튼


        Intent i = getIntent();
        String tmp = i.getStringExtra("imgpath");
        tmp2 = tmp;
        // 로우값 채우기

        view = rowContainer.findViewById(R.id.row1);
        view2 = rowContainer.findViewById(R.id.row2);

        int day = 0;

        fillRow(view, "TODAY", "");



        Bitmap bitmap = null;
        if(tmp != null) {


            Uri uriFromPath = Uri.fromFile(new File(tmp));
            try {

                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            }catch (FileNotFoundException e) {

            }
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            Bitmap resized = null;

            while (height > 2000) {
                resized = Bitmap.createScaledBitmap(bitmap, (width * 2000) / height, 2000, true);
                height = resized.getHeight();
                width = resized.getWidth();

            }




            ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(resized); //R.drawable.ic_cast_connected_grey600_24dp


        }

        view = rowContainer.findViewById(R.id.row2);
        fillRow(view, String.valueOf(++day) + " DAYS AGO", "");
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.pi_2);



        view = rowContainer.findViewById(R.id.row3);
        fillRow(view, String.valueOf(++day) + " DAYS AGO", "");
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.pi_3);

        view = rowContainer.findViewById(R.id.row4);
        fillRow(view, String.valueOf(++day) + " DAYS AGO", "");
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.pi_4);

        view = rowContainer.findViewById(R.id.row5);
        fillRow(view, String.valueOf(++day) + " DAYS AGO", "");
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.pi_5);

        view = rowContainer.findViewById(R.id.row6);
        fillRow(view, String.valueOf(++day) + " DAYS AGO", "");
        try {
            FileInputStream fisMemo = CardActivity.this.openFileInput("Memo.txt");
            byte[] memoData = new byte[fisMemo.available()];
            while(fisMemo.read(memoData) != -1) {}
            fisMemo.close();
            ((TextView) view.findViewById(R.id.textView)).setText(new String(memoData));
        } catch (Exception e) { e.printStackTrace(); }

    }

    // 토글아이콘 상태 동기화

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void fillRow(View view, final String title, final String description) {

        // 제목 아이디값을 찾습니다

        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

        // 서브 제목 아이디값을 찾습니다.

        //TextView descriptionView = (TextView) view.findViewById(R.id.description);
        //descriptionView.setText(description);

        // 로우값을 찾고 그에대한 클릭이벤트를 부여합니다.

        findViewById(R.id.row1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageViewByUri((ImageView) view.findViewById(R.id.imageView));

            }
        });

        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, "Hi! Galaxy!");
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + tmp2));
                i.setType("image/*");
                startActivity(i);
               // Log.d("sdfsdfdfsdfsdf: ", String.valueOf(tmp));
            }
         });



        findViewById(R.id.row2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageView((ImageView) view.findViewById(R.id.imageView));

            }
        });

        findViewById(R.id.row3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageView((ImageView) view.findViewById(R.id.imageView));

            }
        });

        findViewById(R.id.row4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageView((ImageView) view.findViewById(R.id.imageView));

            }
        });

        findViewById(R.id.row5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageView((ImageView) view.findViewById(R.id.imageView));

            }
        });


    }

    public void loadImageViewByUri(ImageView imageView) {
        Dialog dialog = new Dialog(CardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_imageview);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView fb = (ImageView) dialog.findViewById(R.id.imageView);

        String tmp  = getIntent().getStringExtra("imgpath");



        Uri uriFromPath = Uri.fromFile(new File(tmp));

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap resized = null;

        while (height > 2000) {
            resized = Bitmap.createScaledBitmap(bitmap, (width * 2000) / height, 2000, true);
            height = resized.getHeight();
            width = resized.getWidth();

        }
        fb.setImageBitmap(resized);
        dialog.show();


        //Intent intent = new Intent(CardActivity.this, ImageViewActivity.class);
        /*
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] food = stream.toByteArray();
        Bundle extras = new Bundle();
        intent.putExtras(extras);*/
        //intent.putExtra("picture", tmp);
        //intent.putExtra("int", 1);
        //startActivity(intent);

    }


    public void loadImageView(ImageView imageView) {
        Dialog dialog = new Dialog(CardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_imageview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView fb = (ImageView) dialog.findViewById(R.id.imageView);
        Intent intent = new Intent(CardActivity.this, ImageViewActivity.class);

        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        fb.setImageBitmap(bitmap);
        dialog.show();/*
        byte[] food = stream.toByteArray();
        Bundle extras = new Bundle();
        intent.putExtras(extras);
        intent.putExtra("picture", food);
        startActivity(intent);*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 토글 클릭시 열리는 기능 추가

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
