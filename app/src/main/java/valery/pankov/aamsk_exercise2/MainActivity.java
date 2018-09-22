package valery.pankov.aamsk_exercise2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import static android.view.WindowManager.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fab;
    private FloatingActionMenu actionMenu;
    private int currentPage;
    private TextView textView;
    private ImageAdapter adapter;
    private boolean appBarExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareView();
        prepareColors();
        setActionMenu();
    }

    public void setStatusBarColor(View statusBar, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            w.setStatusBarColor(color);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS, LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //status bar height
                statusBar.getLayoutParams().height = getStatusBarHeight();
                statusBar.setBackgroundColor(color);
            }
        }


    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (appBarExpanded) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_insta:
                openInstagram();
                return true;
            case R.id.action_send_email:
                mailTo();
                return true;
            case R.id.action_twitter:
                openTwitter();
                return true;
            case R.id.action_calling:
                call();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            //Log.i(TAG, "page selected " + position);
            currentPage = position;
        }
    }


    private void prepareView(){
        final Toolbar toolbar = findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = findViewById(R.id.appbar);
        fab = findViewById(R.id.fab);
        textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getString(R.string.biography)));
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.title));


        ViewPager viewPager = findViewById(R.id.view_pager);
        adapter = new ImageAdapter(this);

        viewPager.setAdapter(adapter);

        PageListener pageListener = new PageListener();
        viewPager.setOnPageChangeListener(pageListener);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(MainActivity.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);
                if (Math.abs(verticalOffset) > 1) {
                    actionMenu.updateItemPositions();
                    if (Math.abs(verticalOffset) > appBarLayout.getHeight() / 2) {
                        actionMenu.close(true);
                        appBarExpanded = false;
                        invalidateOptionsMenu();
                    }
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }

            }
        });
    }

    private void setActionMenu(){
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIcon1 = new ImageView(this);
        ImageView itemIcon2 = new ImageView(this);
        ImageView itemIcon3 = new ImageView(this);
        ImageView itemIcon4 = new ImageView(this);

        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_twitter_logo));
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_instagram));
        itemIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_answer));
        itemIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_emai));

        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();

        actionMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(180)
                .setEndAngle(270)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.subfab_radius_large))
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(fab)
                .build();

        actionMenu.getSubActionItems().get(0).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTwitter();
            }
        });
        actionMenu.getSubActionItems().get(1).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstagram();
            }
        });
        actionMenu.getSubActionItems().get(2).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
        actionMenu.getSubActionItems().get(3).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailTo();
            }
        });
    }

    private void openInstagram() {

        Intent intent = null;
        Uri uri = Uri.parse("https://www.instagram.com/explore/tags/sirjackiestewart");
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.instagram.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        this.startActivity(intent);
    }

    private void openTwitter() {

        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/grumpysirjackie"));
        }
        this.startActivity(intent);

    }

    private void mailTo() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:sharron@mn2s.com"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            warningToast();
        }
        startActivity(intent);
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+4402072349455"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void warningToast() {
        Toast.makeText(MainActivity.this, R.string.warning_msg_email, Toast.LENGTH_LONG).show();
    }

    private void prepareColors(){
        Bitmap b = BitmapFactory.decodeResource(getResources(),
                adapter.Images[currentPage]);
        Palette.from(b).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                setStatusBarColor(collapsingToolbar, palette.getLightVibrantColor(R.color.colorPrimaryDark));
                collapsingToolbar.setContentScrimColor(palette.getDarkVibrantColor(R.color.colorPrimary));
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

    }
}
