package ducere.lechal.pod;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.odml.MapLoader;
import com.here.android.mpa.odml.MapPackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DownloadMap extends AppCompatActivity {

    MapLoader mapLoader;
    List<Integer> pacakageId = new ArrayList<Integer>();
    MapPackage mappacakages;
    ListView lvContinents, lvCountries, lvStates, lvCities;
    ArrayList<String> continets = new ArrayList<String>();
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> states = new ArrayList<String>();
    ArrayList<String> cities = new ArrayList<String>();

    ArrayList<String> sizeContinents = new ArrayList<String>();
    ArrayList<String> sizeCountries = new ArrayList<String>();
    ArrayList<String> sizeStates = new ArrayList<String>();
    ArrayList<String> sizeCities = new ArrayList<String>();

    ArrayList<Integer> downlaodimageContinents = new ArrayList<Integer>(); // 0 - null 1 - not installed 2 - installed
    ArrayList<Integer> downlaodimageCountries = new ArrayList<Integer>(); // 0 - null 1 - not installed 2 - installed
    ArrayList<Integer> downlaodimageStates = new ArrayList<Integer>(); // 0 - null 1 - not installed 2 - installed
    ArrayList<Integer> downlaodimageCities = new ArrayList<Integer>(); // 0 - null 1 - not installed 2 - installed
    int images[] = {R.mipmap.battery_80, R.mipmap.battery_80, R.mipmap.battery_80, R.mipmap.battery_80};

    int children = 0; // 0 - Continent 1 - country 2 - state 3 - city
    int continent = 0, state = 0, city = 0, country = 0;

    ProgressBar progressBar;
    private int progress = 10;
    Notification notification;
    NotificationManager notificationManager;
    ProgressDialog progressDialog;
    View viewMap;
    boolean isDownloading = false;
    ProgressBar progressBarMap;
    LinearLayout llProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_download_map);
        lvContinents = (ListView) findViewById(R.id.lv_continents);
        lvCountries = (ListView) findViewById(R.id.lv_countries);
        lvStates = (ListView) findViewById(R.id.lv_states);
        lvCities = (ListView) findViewById(R.id.lv_cities);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(DownloadMap.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        if (!isFinishing())
            progressDialog.show();


        initMapEngine();

        lvContinents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                continent = position;
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                if (!isFinishing())
                    progressDialog.show();


                if (!isDownloading) {
                    mapLoader.getMapPackages();
                } else {
                    progressDialog.cancel();
                    lvContinents.setVisibility(View.GONE);
                    lvCountries.setVisibility(View.VISIBLE);
                    lvStates.setVisibility(View.GONE);
                    lvCities.setVisibility(View.GONE);
                }


            }
        });

        lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                if (!isFinishing())
                    progressDialog.show();
                country = position;
                viewMap = view;
                if (!isDownloading) {
                    mapLoader.getMapPackages();
                } else {
                    progressDialog.cancel();
                    lvContinents.setVisibility(View.GONE);
                    lvContinents.setVisibility(View.GONE);
                    lvStates.setVisibility(View.VISIBLE);
                    lvCities.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Already one package is downloading wait...", Toast.LENGTH_SHORT).show();
                }


            }
        });

        lvStates.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                if (!isFinishing())
                    progressDialog.show();

                state = position;
                viewMap = view;
                if (!isDownloading) {
                    mapLoader.getMapPackages();
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Already one package is downloading wait...", Toast.LENGTH_SHORT).show();
                }


            }
        });
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                viewMap = view;
                // TODO Auto-generated method stub
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                if (!isFinishing())
                    progressDialog.show();
                city = position;
                mapLoader.getMapPackages();


            }
        });


        initNotification();
        //initActionBar();

    }


    void initNotification() {
        // configure the intent
        Intent intent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // configure the notification
        notification = new Notification(R.mipmap.icon_map, "simulating a download", System
                .currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.downlaod_progress);
        notification.contentIntent = pendingIntent;
        notification.contentView.setImageViewResource(R.id.status_icon, R.mipmap.icon_map);
        notification.contentView.setTextViewText(R.id.status_text, "Downloading map");
        notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);
        Intent switchIntent = new Intent(this, switchButtonListener.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
                switchIntent, 0);

        notification.contentView.setOnClickPendingIntent(R.id.llCancel,
                pendingSwitchIntent);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(
                getApplicationContext().NOTIFICATION_SERVICE);

    }

    public class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("cancel", "cancel notification");
            notificationManager.cancel(42);
            mapLoader.removeListener(mapLoaderListener);
        }
    }

    MapLoader.Listener mapLoaderListener = new MapLoader.Listener() {
        public void onUninstallMapPackagesComplete(MapPackage rootMapPackage,
                                                   MapLoader.ResultCode mapLoaderResultCode) {
            //mapLoader.removeListener(mapLoaderListener);
            //Log.d("on uninstall map package complete", mapLoaderResultCode.toString() + "");
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), "Uninstall Map : " + mapLoaderResultCode.toString(), Toast.LENGTH_SHORT).show();
            isDownloading = false;
        }

        public void onProgress(int progressPercentage) {
            Log.d("progress", progressPercentage + "");
            isDownloading = true;
            notification.contentView.setProgressBar(R.id.status_progress, 100, progressPercentage, false);

            if (progressBarMap != null) {
                progressBarMap.setProgress(progressPercentage);
                progressBarMap.setIndeterminate(false);
            }
            // inform the progress bar of updates in progress
            notificationManager.notify(42, notification);
            if (progressPercentage == 100) {
                notificationManager.cancel(42);
                if (llProgress != null)
                    llProgress.setVisibility(View.GONE);
            }
            //mapLoader.removeListener(mapLoaderListener);
        }

        public void onPerformMapDataUpdateComplete(MapPackage rootMapPackage,
                                                   MapLoader.ResultCode mapLoaderResultCode) {
            //mapLoader.removeListener(mapLoaderListener);
            // Log.d("on perform map update complete", mapLoaderResultCode.toString() + "");
            Toast.makeText(getApplicationContext(), "map update : " + mapLoaderResultCode.toString(), Toast.LENGTH_SHORT).show();
            isDownloading = false;
        }

        public void onInstallationSize(long diskSize, long networkSize) {
            Log.d("Size", diskSize + "," + networkSize);
            progressDialog.cancel();
        }

        public void onInstallMapPackagesComplete(MapPackage rootMapPackage,
                                                 MapLoader.ResultCode mapLoaderResultCode) {
            // Log.d("on install map package complete", mapLoaderResultCode.toString() + "");
            Toast.makeText(getApplicationContext(), "map install : " + mapLoaderResultCode.toString(), Toast.LENGTH_SHORT).show();

            isDownloading = false;
            //mapLoader.removeListener(mapLoaderListener);
            progressDialog.cancel();
        }

        public void onGetMapPackagesComplete(MapPackage rootMapPackage,
                                             MapLoader.ResultCode mapLoaderResultCode) {
            progressDialog.cancel();
            //Log.d("on map packages complete", mapLoaderResultCode.toString());
            if (mapLoaderResultCode == MapLoader.ResultCode.OPERATION_SUCCESSFUL) {
                if (children == 0) {
                    //Continents
                    downlaodimageContinents.clear();
                    continets.clear();
                    sizeContinents.clear();
                    for (int i = 0; i < rootMapPackage.getChildren().size(); i++) {
                        continets.add(rootMapPackage.getChildren().get(i).getTitle());
                        sizeContinents.add(fileSize(rootMapPackage.getChildren().get(i).getSize()) + "");
                        if (rootMapPackage.getChildren().get(i).isInstalled()) {
                            downlaodimageContinents.add(2);
                        } else if (rootMapPackage.getChildren().get(i).getChildren() != null &&
                                rootMapPackage.getChildren().get(i).getChildren().size() > 0) {
                            downlaodimageContinents.add(0);
                        } else {
                            downlaodimageContinents.add(1);
                        }

                    }

                    // Create ArrayAdapter using the Countries list.
                    DownloadMapAdapter adapter = new DownloadMapAdapter(DownloadMap.this, continets, images, sizeContinents, downlaodimageContinents, 0);
                    // Assign adapter to ListView
                    lvContinents.setAdapter(adapter);
                    children = 1;
                } else if (children == 1) {
                    //Countries
                    if (rootMapPackage.getChildren().get(continent).getChildren() != null && rootMapPackage.getChildren().get(continent).getChildren().size() > 0) {

                        downlaodimageCountries.clear();
                        countries.clear();
                        sizeCountries.clear();

                        lvContinents.setVisibility(View.GONE);
                        lvCountries.setVisibility(View.VISIBLE);
                        lvStates.setVisibility(View.GONE);
                        lvCities.setVisibility(View.GONE);

                        for (int i = 0; i < rootMapPackage.getChildren().get(continent).getChildren().size(); i++) {
                            countries.add(rootMapPackage.getChildren().get(continent).getChildren().get(i).getTitle());
                            sizeCountries.add(fileSize(rootMapPackage.getChildren().get(continent).getChildren().get(i).getSize()) + "");
                            Log.d("test", rootMapPackage.getChildren().get(continent).getChildren().get(i).isInstalled() + "");
                            if (rootMapPackage.getChildren().get(continent).getChildren().get(i).isInstalled()) {
                                downlaodimageCountries.add(2);
                            } else if (rootMapPackage.getChildren().get(continent).getChildren().get(i) != null &&
                                    rootMapPackage.getChildren().get(continent).getChildren().get(i).getChildren().size() > 0) {
                                downlaodimageCountries.add(0);
                            } else {
                                downlaodimageCountries.add(1);
                            }
                        }


                        // Create ArrayAdapter using the Countries list.
                        DownloadMapAdapter adapter = new DownloadMapAdapter(
                                DownloadMap.this, countries, images,
                                sizeCountries, downlaodimageCountries, 1);

                        // Assign adapter to ListView
                        lvCountries.setAdapter(adapter);
                        children = 2;
                    } else {
                        pacakageId.add(rootMapPackage.getChildren().get(continent).getId());

                        Log.d("install title 1", rootMapPackage.getChildren().get(continent).getTitle());
                        pacakageId.clear();
                    }
                } else if (children == 2) {

                    //States

                    if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren() != null &&
                            rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().size() > 0) {
                        downlaodimageStates.clear();
                        states.clear();
                        sizeStates.clear();

                        lvContinents.setVisibility(View.GONE);
                        lvCountries.setVisibility(View.GONE);
                        lvStates.setVisibility(View.VISIBLE);
                        lvCities.setVisibility(View.GONE);

                        for (int i = 0; i < rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().size(); i++) {
                            states.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(i).getTitle());
                            sizeStates.add(fileSize(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(i).getSize()) + "");

                            if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(i).isInstalled()) {
                                downlaodimageStates.add(2);
                            } else if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(i) != null &&
                                    rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(i).getChildren().size() > 0) {
                                downlaodimageStates.add(0);
                            } else {
                                downlaodimageStates.add(1);
                            }
                        }

                        // Create ArrayAdapter using the Countries list.
                        DownloadMapAdapter adapter = new DownloadMapAdapter(DownloadMap.this, states, images, sizeStates, downlaodimageStates, 1);

                        // Assign adapter to ListView
                        lvStates.setAdapter(adapter);
                        children = 3;
                    } else {
                        pacakageId.clear();
                        pacakageId.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getId());
                        if (!rootMapPackage.getChildren().get(continent).getChildren().get(country).isInstalled()) {

                            ArrayList<Integer> downloadList = new ArrayList<>(1);
                            downloadList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getId());
                            mapLoader.installMapPackages(downloadList);

                            notification.contentView.setTextViewText(R.id.status_text, rootMapPackage.getChildren().get(continent).getChildren().get(country).getTitle());
                            if (viewMap == null) {
                                return;
                            }
                            llProgress = (LinearLayout) viewMap.findViewById(R.id.llProgress);
                            progressBarMap = (ProgressBar) viewMap.findViewById(R.id.status_progress);
                            progressBarMap.setMax(100);
                            progressBarMap.setIndeterminate(true);
                            viewMap.setBackgroundColor(0x55333333);
                            llProgress.setVisibility(View.VISIBLE);
                            final ImageView ivDownload = (ImageView) viewMap.findViewById(R.id.iv_download);
                            ivDownload.setVisibility(View.INVISIBLE);
                            ImageView ivCancel = (ImageView) viewMap.findViewById(R.id.ivCancel);
                            ivCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mapLoader.cancelCurrentOperation();
                                    notificationManager.cancel(42);
                                    llProgress.setVisibility(View.GONE);
                                    ivDownload.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            ArrayList<Integer> removalList = new ArrayList<>(1);
                            removalList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getId());
                            mapLoader.uninstallMapPackages(removalList);
                            progressDialog.cancel();
                            progressDialog.setMessage("Uninstalling...");
                            progressDialog.setCancelable(true);
                            if (!isFinishing())
                                progressDialog.show();
                        }
                        Log.d("install title 2", rootMapPackage.getChildren().get(continent).getChildren().get(country).getTitle() + "nn");

                    }

                } else if (children == 3) {

                    //Cities
                    if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren() != null &&
                            rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().size() > 0) {

                        downlaodimageCities.clear();
                        cities.clear();
                        sizeCities.clear();

                        lvContinents.setVisibility(View.GONE);
                        lvCountries.setVisibility(View.GONE);
                        lvStates.setVisibility(View.GONE);
                        lvCities.setVisibility(View.VISIBLE);

                        for (int i = 0; i < rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().size(); i++) {
                            cities.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().get(i).getTitle());
                            sizeCities.add(fileSize(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().get(i).getSize()) + "");

                            if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().get(i).isInstalled()) {
                                downlaodimageCities.add(2);
                            } else if (rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().get(i) != null &&
                                    rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getChildren().get(i).getChildren().size() > 0) {
                                downlaodimageCities.add(0);
                            } else {
                                downlaodimageCities.add(1);
                            }
                        }

                        // Create ArrayAdapter using the Countries list.
                        DownloadMapAdapter adapter = new DownloadMapAdapter(
                                DownloadMap.this, cities, images,
                                sizeCities, downlaodimageCities, 1);

                        // Assign adapter to ListView
                        lvCities.setAdapter(adapter);
                        children = 4;
                    } else {
                        pacakageId.clear();
                        pacakageId.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                        if (!rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).isInstalled()) {

                            ArrayList<Integer> downloadList = new ArrayList<>(1);
                            downloadList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                            mapLoader.installMapPackages(downloadList);

                            notification.contentView.setTextViewText(R.id.status_text, rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getTitle());

                            if (viewMap == null) {
                                return;
                            }
                            final ImageView ivDownload = (ImageView) viewMap.findViewById(R.id.iv_download);
                            ivDownload.setVisibility(View.INVISIBLE);
                            llProgress = (LinearLayout) viewMap.findViewById(R.id.llProgress);
                            progressBarMap = (ProgressBar) viewMap.findViewById(R.id.status_progress);
                            progressBarMap.setMax(100);
                            llProgress.setVisibility(View.VISIBLE);
                            progressBarMap.setIndeterminate(true);
                            ImageView ivCancel = (ImageView) viewMap.findViewById(R.id.ivCancel);
                            ivCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llProgress.setVisibility(View.GONE);
                                    mapLoader.cancelCurrentOperation();
                                    notificationManager.cancel(42);
                                    ivDownload.setVisibility(View.VISIBLE);
                                }
                            });

                        } else {

                            ArrayList<Integer> removalList = new ArrayList<>(1);
                            removalList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                            mapLoader.uninstallMapPackages(removalList);
                            Log.d("uninstall", pacakageId.size() + "--");
                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                            progressDialog.setMessage("Uninstalling...");
                            progressDialog.setCancelable(false);
                            if (!isFinishing())
                                progressDialog.show();
                        }
                        Log.d("install title 3", rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getTitle());
                        pacakageId.clear();
                        lvCities.setVisibility(View.VISIBLE);
                    }

                } else {
                    pacakageId.clear();
                    pacakageId.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                    if (!rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).isInstalled()) {
                        ArrayList<Integer> downloadList = new ArrayList<>(1);
                        downloadList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                        mapLoader.installMapPackages(downloadList);

                        notification.contentView.setTextViewText(R.id.status_text, rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getTitle());

                        if (viewMap == null) {
                            return;
                        }
                        final ImageView ivDownload = (ImageView) viewMap.findViewById(R.id.iv_download);
                        ivDownload.setVisibility(View.INVISIBLE);
                        llProgress = (LinearLayout) viewMap.findViewById(R.id.llProgress);
                        progressBarMap = (ProgressBar) viewMap.findViewById(R.id.status_progress);
                        progressBarMap.setMax(100);
                        progressBarMap.setIndeterminate(true);
                        llProgress.setVisibility(View.VISIBLE);
                        ImageView ivCancel = (ImageView) viewMap.findViewById(R.id.ivCancel);
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mapLoader.cancelCurrentOperation();
                                notificationManager.cancel(42);
                                llProgress.setVisibility(View.GONE);
                                ivDownload.setVisibility(View.VISIBLE);
                            }
                        });

                    } else {
                        ArrayList<Integer> removalList = new ArrayList<>(1);
                        removalList.add(rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getId());
                        mapLoader.uninstallMapPackages(removalList);
                        progressDialog.cancel();
                        progressDialog.setMessage("Uninstalling...");
                        progressDialog.setCancelable(false);
                        if (!isFinishing())
                            progressDialog.show();
                    }
                    Log.d("install title 4", rootMapPackage.getChildren().get(continent).getChildren().get(country).getChildren().get(state).getTitle());
                    pacakageId.clear();
                    lvCities.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getApplicationContext(), mapLoaderResultCode.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        public void onCheckForUpdateComplete(boolean updateAvailable,
                                             String currentMapVersion, String newestMapVersion,
                                             MapLoader.ResultCode mapLoaderResultCode) {
            //Log.d("on check for update complete", updateAvailable + "");
            Toast.makeText(getApplicationContext(), "on check for update complete : " + mapLoaderResultCode.toString(), Toast.LENGTH_SHORT).show();

            isDownloading = false;
        }
    };

    public void initMapEngine() {

        MapEngine mapEngine = MapEngine.getInstance(getApplicationContext());
        mapEngine.init(getApplicationContext(), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    mapLoader = MapLoader.getInstance();
                    mapLoader.addListener(mapLoaderListener);
                    mapLoader.getMapPackages();
                    // mapLoader.performMapDataUpdate();
                } else {
                    // handle factory initialization failure
                    //showToastMessage("Map engine Initilize Failure...");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

		/*	getMenuInflater().inflate(R.menu.search_menu, menu);
         */
        return super.onCreateOptionsMenu(menu);
    }


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (children == 0) {
                    mapLoader.removeListener(mapLoaderListener);
                    finish();

                } else if (children == 1) {
                    mapLoader.removeListener(mapLoaderListener);
                    finish();

                } else if (children == 2) {
                    children = 1;
                    lvContinents.setVisibility(View.VISIBLE);
                    lvCountries.setVisibility(View.GONE);
                    lvStates.setVisibility(View.GONE);
                    lvCities.setVisibility(View.GONE);

                } else if (children == 3) {
                    children = 2;
                    lvContinents.setVisibility(View.GONE);
                    lvCountries.setVisibility(View.VISIBLE);
                    lvStates.setVisibility(View.GONE);
                    lvCities.setVisibility(View.GONE);
                } else if (children == 4) {
                    children = 3;
                    lvContinents.setVisibility(View.GONE);
                    lvCountries.setVisibility(View.GONE);
                    lvStates.setVisibility(View.VISIBLE);
                    lvCities.setVisibility(View.GONE);
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (children == 0) {
            mapLoader.removeListener(mapLoaderListener);
            finish();

        } else if (children == 1) {
            mapLoader.removeListener(mapLoaderListener);
            finish();

        } else if (children == 2) {
            children = 1;
            lvContinents.setVisibility(View.VISIBLE);
            lvCountries.setVisibility(View.GONE);
            lvStates.setVisibility(View.GONE);
            lvCities.setVisibility(View.GONE);

        } else if (children == 3) {
            children = 2;
            lvContinents.setVisibility(View.GONE);
            lvCountries.setVisibility(View.VISIBLE);
            lvStates.setVisibility(View.GONE);
            lvCities.setVisibility(View.GONE);
        } else if (children == 4) {
            children = 3;
            lvContinents.setVisibility(View.GONE);
            lvCountries.setVisibility(View.GONE);
            lvStates.setVisibility(View.VISIBLE);
            lvCities.setVisibility(View.GONE);
        }
    }

    public static String fileSize(long size) {
        Log.d("size", size + "");
        if (size <= 0) return "0";
        final String[] units = new String[]{"kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
