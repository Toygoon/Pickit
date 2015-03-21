package kr.prizm.pickit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by koma on 15. 3. 21..
 */

public class ImageViewActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_imageview);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if(getIntent().getIntExtra("int", 2) == 1) {

            Uri uriFromPath = Uri.fromFile(new File(getIntent().getStringExtra("picture")));

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            Bitmap resized = null;

            while (height > 2000) {
                resized = Bitmap.createScaledBitmap(bitmap, (width * 2000) / height, 2000, true);
                height = resized.getHeight();
                width = resized.getWidth();

            }
            imageView.setImageBitmap(resized);
        } else {
            Bundle extras = getIntent().getExtras();
            byte[] food = extras.getByteArray("picture");
            Bitmap fo = BitmapFactory.decodeByteArray(food, 0, food.length);
            imageView.setImageBitmap(fo);
        }

        /*

*/
        /*

        Bitmap bmp = (Bitmap) extras.getParcelable("image");

        imageView.setUri
        */
    }
}
