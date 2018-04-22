package com.studio.mpak.newsby;

import static java.lang.String.format;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.studio.mpak.newsby.loader.BackgroundService;
import com.studio.mpak.newsby.loader.DownloadResultReceiver;
import com.studio.mpak.newsby.util.AppUtil;

/**
 * @author Andrei Kuzniatsou
 */
public class DownloadActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver, AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = DownloadActivity.class.getSimpleName();

    private static final int DEFAULT_SELECTION = 0;
    private static final int DEFAULT_PERIOD = 1;
    private DownloadResultReceiver mReceiver;
    private Button downloadButton;
    private ProgressBar progressBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

//        Spinner spinner = findViewById(R.id.peroid_spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.update_period, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
//        spinner.setSelection(DEFAULT_SELECTION);

        intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("period", DEFAULT_PERIOD);

        downloadButton = findViewById(R.id.start_downloading);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.isMyServiceRunning(DownloadActivity.this, BackgroundService.class)) {
                    stopService(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                    downloadButton.setText("Начать загрузку");
                } else {
                    startService(intent);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    downloadButton.setText("Отменить");
                }
            }
        });

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (AppUtil.isMyServiceRunning(DownloadActivity.this, BackgroundService.class)) {
            new AlertDialog.Builder(this)
                    .setTitle("Выход")
                    .setMessage("Прервать загрузку?")
                    .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopService(intent);
                            NavUtils.navigateUpFromSameTask(DownloadActivity.this);
                        }
                    })
                    .setNegativeButton("НЕТ", null)
                    .show();
        } else {
            NavUtils.navigateUpFromSameTask(DownloadActivity.this);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case BackgroundService.STATUS_RUNNING:
                long progress = resultData.getLong(Intent.EXTRA_TEXT);
                progressBar.setProgress((int) progress);
                break;
            case BackgroundService.STATUS_FINISHED:
                progressBar.setVisibility(View.INVISIBLE);
                downloadButton.setText("Готово");
//                Toast.makeText(this, "Complete", Toast.LENGTH_LONG).show();
                break;
            case BackgroundService.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Log.i(LOG_TAG, "Selected 1 item");
                intent.putExtra("period", 1);
                break;
            case 1:
                Log.i(LOG_TAG, "Selected 2 item");
                intent.putExtra("period", 2);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
