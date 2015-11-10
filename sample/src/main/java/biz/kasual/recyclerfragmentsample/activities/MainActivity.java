package biz.kasual.recyclerfragmentsample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.fragments.CustomSectionTitleRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.CustomSectionViewRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.GestureRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.MultipleChoiceRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.SingleChoiceRecyclerFragment;
import biz.kasual.recyclerfragmentsample.fragments.SectionRecyclerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        displaySingleChoiceRecyclerFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.single_choice_action:
                displaySingleChoiceRecyclerFragment();
                break;

            case R.id.multiple_choice_action:
                displayMultipleChoiceRecyclerFragment();
                break;

            case R.id.gesture_action:
                displayGestureRecyclerFragment();
                break;

            case R.id.section_action:
                displaySectionRecyclerFragment();
                break;

            case R.id.custom_section_title_action:
                displayCustomSectionTitleRecyclerFragment();
                break;

            case R.id.custom_section_view_action:
                displayCustomSectionViewRecyclerFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySingleChoiceRecyclerFragment() {
        setTitle(getString(R.string.single_choice_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SingleChoiceRecyclerFragment()).commit();
    }

    private void displayMultipleChoiceRecyclerFragment() {
        setTitle(getString(R.string.multiple_choice_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MultipleChoiceRecyclerFragment()).commit();
    }

    private void displayGestureRecyclerFragment() {
        setTitle(getString(R.string.gesture_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new GestureRecyclerFragment()).commit();
    }

    private void displaySectionRecyclerFragment() {
        setTitle(getString(R.string.section_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SectionRecyclerFragment()).commit();
    }

    private void displayCustomSectionTitleRecyclerFragment() {
        setTitle(getString(R.string.custom_section_title_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CustomSectionTitleRecyclerFragment()).commit();
    }

    private void displayCustomSectionViewRecyclerFragment() {
        setTitle(getString(R.string.custom_section_view_recycler_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CustomSectionViewRecyclerFragment()).commit();
    }

}
