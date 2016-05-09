package ducere.lechal.pod;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ducere.lechal.pod.customViews.CustomViewPager;
import ducere.lechal.pod.interfaces.OnBackPressed;
import ducere.lechal.pod.interfaces.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CustomViewPager.OnPageChangeListener,OnFragmentInteractionListener{

    public static TabLayout tabLayout;
    CustomViewPager viewPager;
    public static Toolbar toolbar;
    MenuItem item_contacts,item_notifications,item_add_pods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons0();
        viewPager.setOnPageChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(toolbar.getVisibility()==View.GONE){
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null) {
                //TODO: Perform your logic to pass back press here
                for(Fragment fragment : fragmentList){
                    if(fragment instanceof OnBackPressed){
                        ((OnBackPressed)fragment).onBackPressed();
                    }
                }
            }
        } else{
            super.onBackPressed();
        }
    }
    /**
     * Adding fragments to ViewPager
     * @param viewPager
     */
    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "Home");
        adapter.addFrag(new ActivityFragment(), "Activity");
        adapter.addFrag(new DeviceFragment(), "Device");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        item_contacts = menu.findItem(R.id.action_contacts);
        item_notifications = menu.findItem(R.id.action_notifications);
        item_add_pods = menu.findItem(R.id.action_add_pods);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contacts) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position){
            case 0:
                setupTabIcons0();
                break;
            case 1:
                setupTabIcons1();
                break;
            case 2:
                setupTabIcons2();
                break;



        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
             mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {


            return mFragmentTitleList.get(position);
        }
    }
    private void setupTabIcons0() {


        int[] tabIcons = {
                R.drawable.home_tab_white,
                R.drawable.activity_tab_white,
                R.drawable.lechal_tab_white
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(null);
        tabLayout.getTabAt(2).setIcon(null);

        tabLayout.getTabAt(0).setText("");
        tabLayout.getTabAt(1).setText("Activity");
        tabLayout.getTabAt(2).setText("Device");
        tabLayout.getTabAt(0).setContentDescription("Navigation Home page selected");
        tabLayout.getTabAt(1).setContentDescription("FitnessFragment Home page ");
        tabLayout.getTabAt(2).setContentDescription("Profile Home page");

        if(item_add_pods!=null){
            item_contacts.setVisible(true);
            item_notifications.setVisible(false);
            item_add_pods.setVisible(false);
        }


    }
    private void setupTabIcons1() {


        int[] tabIcons = {
                R.drawable.home_tab_white,
                R.drawable.activity_tab_white,
                R.drawable.lechal_tab_white
        };

        tabLayout.getTabAt(0).setIcon(null);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(null);
        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setText("");
        tabLayout.getTabAt(2).setText("Device");
        tabLayout.getTabAt(0).setContentDescription("Navigation Home page ");
        tabLayout.getTabAt(1).setContentDescription("FitnessFragment Home page selected");
        tabLayout.getTabAt(2).setContentDescription("Profile Home page");
        if(item_add_pods!=null){
            item_contacts.setVisible(false);
            item_notifications.setVisible(true);
            item_add_pods.setVisible(false);
        }

    }
    private void setupTabIcons2() {


        int[] tabIcons = {
                R.drawable.home_tab_white,
                R.drawable.activity_tab_white,
                R.drawable.lechal_tab_white
        };

        tabLayout.getTabAt(0).setIcon(null);
        tabLayout.getTabAt(1).setIcon(null);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setText("Activity");
        tabLayout.getTabAt(2).setText("");
        tabLayout.getTabAt(0).setContentDescription("Navigation Home page ");
        tabLayout.getTabAt(1).setContentDescription("FitnessFragment Home page ");
        tabLayout.getTabAt(2).setContentDescription("Profile Home page selected");
        if(item_add_pods!=null){
            item_contacts.setVisible(false);
            item_notifications.setVisible(false);
            item_add_pods.setVisible(true);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
