package ducere.lechal.pod;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import ducere.lechal.pod.beans.Home;

/**
 * Created by VR Naresh on 10-05-2016.
 */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imvCamera;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView txtDob;
    //  private TextView dateView;
    private int year, month, day;
    private static final String shareStr[] = {"Camera", "Gallery", "Remove photo"};
    private static int RESULT_LOAD_IMAGE = 1;
    protected static final int CAMERA_PIC_REQUEST = 2;
    public static String selectedImagePath;
    protected static Uri outputFileUri = null;
    String msg;
    public static float Orientation;
    private boolean mImageCaptureUri;
    int imageheight, imagwidth;
    static Bitmap original = null;
    static Bitmap resizeimage = null;
    Bitmap bmp;
    ImageView imvProfile;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        setup();
    }

    private void setup() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

    }

    private void initViews() {
        imvCamera = (ImageView) findViewById(R.id.imvCamera);

        Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfileActivity.this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(adapter);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Collapsing");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

        txtDob = (TextView) findViewById(R.id.txtDob);
        txtDob.setOnClickListener(this);

        ImageView imvCamera = (ImageView) findViewById(R.id.imvCamera);
        imvCamera.setOnClickListener(this);

        imvProfile = (ImageView) findViewById(R.id.imvProfile);
        if (bmp != null) {
            imvProfile.setImageBitmap(bmp);

        }

    }

    private void showDate(int year, int month, int day) {
        txtDob.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDob:
                showDialog(999);
                Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imvCamera:
                showBSDialog();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showBSDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dailog_bottom, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleStringRecyclerViewAdapter adapter = new SimpleStringRecyclerViewAdapter(this);
        adapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                dialog.dismiss();
                switch (pos) {
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                        outputFileUri = Uri.fromFile(file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent_ga = new Intent();
                            intent_ga.setType("image/*");
                            intent_ga.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent_ga, "Select Picture"), RESULT_LOAD_IMAGE);
                        } else {
                            Intent intent1 = new Intent(Intent.ACTION_PICK);
                            intent1.setType("*/*");
                            intent1.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(Intent.createChooser(intent1, "Complete action using"), RESULT_LOAD_IMAGE);
                        }
                        break;
                    case 2:
                        imvProfile.setImageDrawable(null);
                        imvProfile.setBackgroundResource(R.mipmap.img_fitness);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.setContentView(view);
        dialog.show();
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        public ItemClickListener mItemClickListener;

        public void setItemClickListener(ItemClickListener listener) {
            mItemClickListener = listener;
        }

        public interface ItemClickListener {
            public void onItemClick(int pos);
        }

        private Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.tv);
            }


        }

        public SimpleStringRecyclerViewAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.mTextView.setText(shareStr[position]);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return shareStr.length;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                try {
                    if (selectedImageUri != null) {
                        selectedImagePath = getPath(selectedImageUri);
                        Orientation = getImageOrientation(selectedImagePath);
                        getAspectRatio();
                        resizeimage = getResizedOriginalBitmap();
                        bmp = resizeimage.copy(resizeimage.getConfig(), true);
                        if (bmp != null) {
                            imvProfile.setImageBitmap(bmp);

                        }
                    } else {
                        return;
                    }

                } catch (Exception e) {
                    return;
                }

            }
            if (requestCode == CAMERA_PIC_REQUEST) {
                switch (resultCode) {
                    case 0:
                        break;

                    case -1:
                        if (outputFileUri != null) {
                            selectedImagePath = outputFileUri.getPath();
                            Orientation = getImageOrientation(selectedImagePath);
                            getAspectRatio();
                            resizeimage = getResizedOriginalBitmap();
                            bmp = resizeimage.copy(resizeimage.getConfig(), true);
                            if (bmp != null) {
                                imvProfile.setImageBitmap(bmp);

                            }
                        }
                        break;
                }

            }

        }

    }

    private Bitmap getResizedOriginalBitmap() {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(selectedImagePath), null, options);

            int srcWidth = options.outWidth;
            int srcHeight = options.outHeight;

            int desiredWidth = imagwidth;
            int desiredHeight = imageheight;

            int inSampleSize = 1;
            while (srcWidth / 2 > desiredWidth) {
                srcWidth /= 2;
                srcHeight /= 2;
                inSampleSize *= 2;
            }

            float desiredWidthScale = (float) desiredWidth / srcWidth;
            float desiredHeightScale = (float) desiredHeight / srcHeight;

            // Decode with inSampleSize
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = inSampleSize;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap sampledSrcBitmap = BitmapFactory.decodeStream(
                    new FileInputStream(selectedImagePath), null, options);

            // Resize
            Matrix matrix = new Matrix();
            matrix.postScale(desiredWidthScale, desiredHeightScale);
            matrix.postRotate(Orientation);
            original = Bitmap.createBitmap(sampledSrcBitmap, 0, 0,
                    sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(),
                    matrix, true);
            sampledSrcBitmap = null;
            return original;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private void getAspectRatio() {
        float scaleFactor = 0;

        float scaleWidth = 0;

        float scaleHeight = 0;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(selectedImagePath, options);

        float origWidth = options.outWidth;

        float origHeight = options.outHeight;

        scaleFactor = origWidth / origHeight;

        if (scaleFactor > 1)

        {

            scaleWidth = 1000;

            scaleHeight = (scaleWidth / scaleFactor);

        } else

        {
            scaleHeight = 1000;

            scaleWidth = (scaleHeight * scaleFactor);

        }

        imagwidth = (int) scaleWidth;
        imageheight = (int) scaleHeight;

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private float getImageOrientation(String selectedImagePath) {
        int orientation = 0;
        float roatation = 0;

        try {

            ExifInterface exif = new ExifInterface(selectedImagePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            if (orientation == 6) {
                roatation = 90;
            } else if (orientation == 3) {
                roatation = 180;
            } else if (orientation == 8) {
                roatation = 270;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return roatation;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}