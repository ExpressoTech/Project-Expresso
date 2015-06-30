package com.expresso.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.expresso.activity.FeedPageLocation;
import com.expresso.model.FeedAttachment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anirdesh_And0001 on 30-05-2015.
 */
public class Utils {


    ProgressDialog progressDialog;

    public static void showToast(Activity activity, String toast) {
        Toast toastMsg=Toast.makeText(activity,toast,Toast.LENGTH_LONG);
        toastMsg.setGravity(Gravity.CENTER,0,0);
        toastMsg.show();
    }

    public static String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public static String compressImage(String imageUri,Activity activity) {

        String filePath = getRealPathFromURI(imageUri,activity);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(String contentURI, Activity activity) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String fileSize(String url)
    {
        File file = new File(url);
        long length = file.length();
        length = length/1024;
        return length+"KB";
    }

    public static String dateDiff(Date date) {
        long oldMillis = date.getTime();
        long currMillis = System.currentTimeMillis();
        String msg = null;
        long diff = java.lang.Math.abs(currMillis - oldMillis);
        long temp = diff / 60000;
        if (temp >= 60) {
            temp = temp / 60;
            if (temp >= 24) {
                temp = temp / 24;
                if (temp > 30) {
                    temp = temp / 30;
                    if (temp > 12) {
                        temp = temp / 12;

                        if (temp == 1) {
                            msg = temp + " year ago";
                        } else {
                            msg = temp + " years ago";
                        }
                    } else {

                        if (temp == 1) {
                            msg = temp + " month ago";
                        } else {
                            msg = temp + " months ago";
                        }
                    }
                } else {

                    if (temp == 1) {
                        msg = temp + " day ago";
                    } else {
                        msg = temp + " days ago";
                    }
                }
            } else {

                if (temp == 1) {
                    msg = temp + " hour ago";
                } else {
                    msg = temp + " hours ago";
                }
            }
        } else {

            if (temp == 1) {
                msg = temp + " min ago";
            } else {
                msg = temp + " mins ago";
            }
        }
        return msg;
    }

    public static Date getDate(String date) {

        String trimdate = date.substring(0, 19);
        SimpleDateFormat inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsed = null;
        try {
            parsed = inputFormat.parse(date);
        } catch (ParseException e) {
        }
        outputFormat.format(parsed);
        return parsed;
    }

    // Check supported file extensions
    private static boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (Constant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public static int getScreenWidth(Context context) {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public static ArrayList<String> getImagePaths(ArrayList<FeedAttachment> result) {
        ArrayList<String> imagePaths=new ArrayList<String>();
        for(int i=0;i<result.size();i++)
        {
            FeedAttachment item=result.get(i);
            imagePaths.add(item.getFeed_url());
        }
        return imagePaths;
    }
}
