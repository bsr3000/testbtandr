package ishchenko.a.testbt;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import ishchenko.a.testbt.Adapters.DocsAdapter;
import ishchenko.a.testbt.SupportClasses.Docs;
import ishchenko.a.testbt.SupportClasses.Profile;
import ishchenko.a.testbt.SupportClasses.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocsActivity extends AppCompatActivity {

    private SharedPreferences sP;
    private GridView gridView;
    private DocsAdapter docsAdapter;
    private ArrayList<Docs> docsList;
    private User curUser;
    private boolean perm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs);
        init_view();
    }

    private void init_view() {
        sP = getSharedPreferences("prefs", 0);
        gridView = (GridView) findViewById(R.id.grid_view);
        getDocs();
    }

    private void checkParcel() {
        Intent intent = getIntent();
        if (intent.getParcelableExtra("user") != null) {
            curUser = intent.getParcelableExtra("user");
        } else {
            curUser = Profile.getInstance(sP);
        }
    }

    private void getDocs() {
        checkParcel();
        RestClient.getInstance(sP).getApiService().getDocs(curUser.getPk()).enqueue(new Callback<ArrayList<Docs>>() {
            @Override
            public void onResponse(Call<ArrayList<Docs>> call, Response<ArrayList<Docs>> response) {
                if (response.code() == 200) {
                    docsList = response.body();
                    docsAdapter = new DocsAdapter(getBaseContext(), docsList);
                    gridView.setAdapter(docsAdapter);
                    setGridViewListener();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Docs>> call, Throwable t) {
                Snackbar.make(gridView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM", "Granted");
                return true;
            } else {
                Log.v("PERM", "Asked");
                ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("PERM", "Granted");
            return true;
        }
    }

    public void setGridViewListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Docs doc = (Docs) docsAdapter.getItem(i);
                perm = checkPerm();
                if (perm) {
                    String path = RestClient.getInstance(sP).getBaseUrl();
                    String url = path.substring(0, path.length() - 1) + doc.getDoc();
                    String[] filename = url.split("/");
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename[filename.length - 1]);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uriString);
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                        if (Build.VERSION.SDK_INT > 23) {
                            if (uriString.substring(0, 7).matches("file://")) {
                                uriString = uriString.substring(7);
                            }
                        }
                        File file = new File(uriString);
                        Uri contentUri = FileProvider.getUriForFile(getBaseContext(), ".provider", file);
                        if (mimeType != null) {
                            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                            openFileIntent.setDataAndTypeAndNormalize(contentUri, mimeType);
                            openFileIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            try {
                                startActivity(openFileIntent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    };
}
