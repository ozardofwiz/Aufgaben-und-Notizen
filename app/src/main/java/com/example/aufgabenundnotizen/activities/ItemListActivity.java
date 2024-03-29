package com.example.aufgabenundnotizen.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.aufgabenundnotizen.R;
import com.example.aufgabenundnotizen.adapters.ViewPagerAdapter;
import com.example.aufgabenundnotizen.fragments.ItemListFragment;
import com.example.aufgabenundnotizen.other.FilterType;

/**
 * Diese Activity stellt eine Liste von Items dar und hat
 * verschiedene Darstellungen für Smartphone und Tablet-große Geräte.
 * Auf Smartphones, stellt die Activity eine List von Items dar, welche bei
 * Berührung zu einer {@Link ItemDetailActivity} führen. Auf Tablets, stellt die
 * Activity die Liste von Items und die Itemdetails seite an seite unter Verwendung
 * von zwei vertikalen Panes dar.
 */
public class ItemListActivity extends AppCompatActivity implements OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private boolean mIsFabOpen;
    private FloatingActionButton mFabAdd, mFabAddTodo, mFabAddNote;
    private Animation mFabOpen, mFabClose, mRotateForward, mRotateBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Tabs initialisieren
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

        // Floating Action Buttons initialisieren
        mFabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        mFabAddTodo = (FloatingActionButton) findViewById(R.id.fabAddTodo);
        mFabAddNote = (FloatingActionButton) findViewById(R.id.fabAddNote);
        mFabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        mRotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        mFabAdd.setOnClickListener(this);
        mFabAddTodo.setOnClickListener(this);
        mFabAddNote.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ItemListFragment.newInstance(FilterType.ALL), getString(R.string.tab_title_all));
        adapter.addFragment(ItemListFragment.newInstance(FilterType.TODOS), getString(R.string.tab_title_todos));
        adapter.addFragment(ItemListFragment.newInstance(FilterType.NOTES), getString(R.string.tab_title_notes));

        // Alle 3 Fragmente sollen in Speichern gehalten werden,
        // um im EventBus die CallBacks zu empfangen.
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_all);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_todo);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_note);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fabAdd:
                animateFab();
                break;
            case R.id.fabAddTodo:
                ItemDetailActivity.start(v.getContext(), null, FilterType.TODOS);
                animateFab();
                break;
            case R.id.fabAddNote:
                ItemDetailActivity.start(v.getContext(), null, FilterType.NOTES);
                animateFab();
                break;
        }
    }

    private void animateFab() {
        if (mIsFabOpen) {
            mFabAdd.startAnimation(mRotateBackward);
            mFabAddTodo.startAnimation(mFabClose);
            mFabAddNote.startAnimation(mFabClose);
            mFabAddTodo.setClickable(false);
            mFabAddNote.setClickable(false);
            mIsFabOpen = false;
        } else {
            mFabAdd.startAnimation(mRotateForward);
            mFabAddTodo.startAnimation(mFabOpen);
            mFabAddNote.startAnimation(mFabOpen);
            mFabAddTodo.setClickable(true);
            mFabAddNote.setClickable(true);
            mIsFabOpen = true;
        }
    }

}
