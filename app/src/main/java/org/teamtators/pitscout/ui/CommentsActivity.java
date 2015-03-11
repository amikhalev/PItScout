package org.teamtators.pitscout.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.teamtators.pitscout.DataManager;
import org.teamtators.pitscout.PitScoutBaseActivity;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentsActivity extends PitScoutBaseActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @InjectView(R.id.comments)
    EditText comments;
    @Inject
    DataManager dataManager;
    @Inject
    Context context;
    private ScoutingData scoutingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.inject(this);
        scoutingData = dataManager.getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_picture:
                takePicture();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_done:
                done();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                return;
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(context, getString(R.string.error_pictures_unsupported), Toast.LENGTH_LONG).show();
        }
    }

    protected File getImageFile() throws IOException {
        String competition = context
                .getSharedPreferences(SignInActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(SignInActivity.KEY_COMPETITION, "");
        Integer teamNumber = scoutingData.getTeamNumber();
        String timeStamp = new SimpleDateFormat("HH.mm.ss", Locale.US).format(new Date());
        String imageFileName = competition + "_" + teamNumber + "_" + timeStamp;
        File directory = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        return File.createTempFile(imageFileName, ".jpg", directory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void done() {
        scoutingData.setComments(comments.getText().toString());
        try {
            scoutingData.appendToFile(this);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_write_failed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        Toast.makeText(context, getString(R.string.message_data_written), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(context, ScoutingActivity.class));
    }
}
