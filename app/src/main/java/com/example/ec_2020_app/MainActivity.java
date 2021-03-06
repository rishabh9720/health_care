package com.example.ec_2020_app;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ec_2020_app.adapter.EventsAdapter;
import com.example.ec_2020_app.adapter.EventsAdapter2;
import com.example.ec_2020_app.adapter.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button log_out;
    TextView show_name,show_mobile,show_college,show_email;
    DatabaseReference reff;
    Timer timer;

    int currentPosition=0;
    ViewPager viewPager;
    LinearLayout linearLayout;

    DrawerLayout mdrawerLayout;
    ActionBarDrawerToggle mtoggle;
    NavigationView nag;

   /* private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
    }*/
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if (mtoggle.onOptionsItemSelected(item)) {
           return true;
       }
       return super.onOptionsItemSelected(item);
   }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_one:
                Toast.makeText(this, "ab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_two: Intent i=new Intent(MainActivity.this,about.class);
                startActivity(i);
                break;
            case R.id.nav_item_three:
                Toast.makeText(this, "ab", Toast.LENGTH_SHORT).show();
                break;
            default:  Toast.makeText(this,"ahsb",Toast.LENGTH_SHORT).show();
        }

        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
 /*   public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navicon, menu);
        return true;
    }*/

    public void onBackPressed() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdrawerLayout = findViewById(R.id.drawer_layout);
        mtoggle = new ActionBarDrawerToggle(this, mdrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mdrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setHomeButtonEnabled(true);
        nag=findViewById(R.id.nav_view);
        nag.setNavigationItemSelectedListener(this);

      //  log_out = findViewById(R.id.log_out);
          //  getSupportActionBar().hide();

     //   show_name = findViewById(R.id.fetch_name);
     //   show_college = findViewById(R.id.fetch_college);
    //    show_email = findViewById(R.id.fetch_email);
     //   show_mobile = findViewById(R.id.fetch_mobile);

        galleryViewPager();

        RecyclerView recyclerView = findViewById(R.id.recycler1);
        RecyclerView recyclerView2 = findViewById(R.id.recyclyer2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new EventsAdapter());

        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setAdapter(new EventsAdapter2());


        final String mmobile = getIntent().getStringExtra("mmobile");

        String datta = "rtr";

            if (mmobile.isEmpty())
            {
                Log.e("TAG","alpha");
                Toast.makeText(MainActivity.this, "You should first sign up and then come", Toast.LENGTH_LONG).show();
                Intent goto_signup = new Intent(MainActivity.this, sign_up.class);
                startActivity(goto_signup);
            }

            if(!mmobile.equals(datta))
        {

            Log.e("TAG","betA");
            Toast.makeText(this, mmobile, Toast.LENGTH_LONG).show();

          reff = FirebaseDatabase.getInstance().getReference().child("users").child(mmobile);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String temp_name = dataSnapshot.child("full_name").getValue().toString();
                        String temp_college = dataSnapshot.child("college").getValue().toString();
                        String temp_email = dataSnapshot.child("email").getValue().toString();
                        String temp_mobile = dataSnapshot.child("mobile_no").getValue().toString();

                        // show_name.setText(temp_name);
                        //  show_college.setText(temp_college);
                        //  show_email.setText(temp_email);
                        //  show_mobile.setText(temp_mobile);
                    } else {
                        // show_email.setText("You should first sigu up and then come");
                        Toast.makeText(MainActivity.this, "You should first sign up and then come", Toast.LENGTH_LONG).show();
                        Intent goto_signup = new Intent(MainActivity.this, sign_up.class);
                        startActivity(goto_signup);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Sorry try again", Toast.LENGTH_SHORT).show();
                }

            });

        }


    /*    log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "Logging Out !!", Toast.LENGTH_SHORT).show();
                    Intent go_to_login = new Intent(MainActivity.this, login.class);
                    startActivity(go_to_login);
                }
            }
        });*/


    }

   // void chekker(View v)
 //   {
   //     Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    //}



    private void galleryViewPager() {

        linearLayout = findViewById(R.id.dotsIndicator);
        viewPager = findViewById(R.id.gallery);

        viewPager.setAdapter(new ViewPagerAdapter(this));


        createSlideShow();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition=position;
            }

            @Override
            public void onPageSelected(int position) {
                prepareDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void createSlideShow() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(currentPosition==5)
                    currentPosition=0;
                viewPager.setCurrentItem(currentPosition++,true);


            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },0,1000);

    }

    private void prepareDots(int currentSlidePosition) {
        if(linearLayout.getChildCount()>0)
            linearLayout.removeAllViews();

        ImageView dots[] = new ImageView[5];

        for(int i=0;i<5;i++)
        {
            dots[i] = new ImageView(this);
            if(i==currentSlidePosition)
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.activedots));
            else
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactivedots));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);
            linearLayout.addView(dots[i],layoutParams);

        }
    }

}
