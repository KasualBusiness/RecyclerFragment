package biz.kasual.recyclerfragmentsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import biz.kasual.recyclerfragmentsample.fragments.SectionRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.DefaultRecyclerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        displaySimpleRecyclerFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.simple_action:
                displaySimpleRecyclerFragment();
                break;

            case R.id.section_action:
                displaySectionRecyclerFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySimpleRecyclerFragment() {
        setTitle(getString(R.string.simple_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new DefaultRecyclerFragment()).commit();
    }

    private void displaySectionRecyclerFragment() {
        setTitle(getString(R.string.section_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SectionRecyclerFragment()).commit();
    }

}
