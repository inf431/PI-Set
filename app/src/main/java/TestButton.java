import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;



/**
 * Created by Boris on 20/03/2015.
 */
public class TestButton extends Drawable {

    private final Paint paint = new Paint();



    @Override
    public void draw(Canvas canvas) {

        Rect b = getBounds();

        int alpha = paint.getAlpha();

        /* Draw border or background. */
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(b, paint);


    }


    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

}
