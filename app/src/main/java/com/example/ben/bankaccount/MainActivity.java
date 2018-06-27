package com.example.ben.bankaccount;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    Context context;
    SqliteDatabase sqliteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // başlangıçta listview fragmenti ayarlanıyor.
        ListViewFragment listViewFragment = new ListViewFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, listViewFragment);
        ft.commit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.bank);

        sqliteDatabase = new SqliteDatabase(this);
        context = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addCustomer) {
            // menuden artı iconuna tıklandığında add customer fragmenti açılıyor
            AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, addCustomerFragment);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // geri butonuna tıklandığında main activitye dönüyor, fakat uygulamadan çıkışı ayarlayamadım.
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
