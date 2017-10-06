package com.kcs.billingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageLoadingUtils {
	private Context context;

	public ImageLoadingUtils(Context context) {
		this.context = context;

	}

	public int convertDipToPixels(float dips) {
		Resources r = context.getResources();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dips, r.getDisplayMetrics());
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;

		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}

	public Bitmap decodeBitmapFromPath(String filePath) {
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		scaledBitmap = BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options,
				convertDipToPixels(150), convertDipToPixels(200));
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = false;

		scaledBitmap = BitmapFactory.decodeFile(filePath, options);
		return scaledBitmap;
	}
    /**
     * Compressing image used encode and decode time retur compress images
     *
     *
     * @param imageUri
     * @param utils
     * @return
     */
    public String compressImage(Activity activity, String imageUri,
                                ImageLoadingUtils utils) {
        try {
            String filePath = getRealPathFromURI(activity, imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int bitampHeight = options.outHeight;
            int bitmapWidth = options.outWidth;
            float maxBitmapHeight = 816.0f;
            float maxBitmapWidth = 612.0f;
            float bitmapRatio = bitmapWidth / bitampHeight;
            float maxRatio = maxBitmapWidth / maxBitmapHeight;

            if (bitampHeight > maxBitmapHeight || bitmapWidth > maxBitmapWidth) {
                if (bitmapRatio < maxRatio) {
                    bitmapRatio = maxBitmapHeight / bitampHeight;
                    bitmapWidth = (int) (bitmapRatio * bitmapWidth);
                    bitampHeight = (int) maxBitmapHeight;
                } else if (bitmapRatio > maxRatio) {
                    bitmapRatio = maxBitmapWidth / bitmapWidth;
                    bitampHeight = (int) (bitmapRatio * bitampHeight);
                    bitmapWidth = (int) maxBitmapWidth;
                } else {
                    bitampHeight = (int) maxBitmapHeight;
                    bitmapWidth = (int) maxBitmapWidth;

                }
            }

            options.inSampleSize = utils.calculateInSampleSize(options,
                    bitmapWidth, bitampHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(bitmapWidth, bitampHeight,
                        Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = bitmapWidth / (float) options.outWidth;
            float ratioY = bitampHeight / (float) options.outHeight;
            float middleX = bitmapWidth / 2.0f;
            float middleY = bitampHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                    middleY - bmp.getHeight() / 2, new Paint(
                            Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);

                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                        matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String convertImageFileName = CM.getCaptureImagePath();
            try {
                out = new FileOutputStream(convertImageFileName);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return convertImageFileName;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    /**
     * Passing uri get SDCARD path
     *
     * @param activity
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Activity activity,
                                      String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null,
                null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public File getFilename() {
        String APP_FOLDER_NAME = "ImageUploadDemo";
        String DOCUMENT_SAVE_PATH = Environment
                .getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME;
        File file = new File(DOCUMENT_SAVE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + CM.getCaptureImagePath());
        return new File(uriSting);

    }
}
