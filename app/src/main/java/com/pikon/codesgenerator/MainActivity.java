package com.pikon.codesgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout dlRoot;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolBar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowTitleEnabled( false );

        dlRoot = findViewById( R.id.dlRoot );
        actionBarDrawerToggle = new ActionBarDrawerToggle( this, dlRoot, R.string.nav_open, R.string.nav_close );
        dlRoot.addDrawerListener( actionBarDrawerToggle );
        actionBarDrawerToggle.syncState(); //

        setNavigationDrawer();
    }

    private void setNavigationDrawer(){
        NavigationView nav = (NavigationView) findViewById( R.id.nvNavigation );
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                int itemId = item.getItemId();
                if( itemId == R.id.navGeneratorBar )
                    frag = new BarGeneratorFrag();
                else if( itemId == R.id.navGeneratorQR )
                    frag = new QrGeneratorFrag();
                else if( itemId == R.id.navReaderBar )
                    frag = new CodeReaderFrag();

                if( frag != null ){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace( R.id.flFrame, frag );
                    transaction.commit();
                    dlRoot.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( actionBarDrawerToggle.onOptionsItemSelected( item ) )
            return true;
        return super.onOptionsItemSelected(item);
    }
}