package nl.gerben_meijer.gerryflap.sensorwater.Water;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Gerryflap on 2015-02-17.
 */
public class WaterView extends View {
    WaterController waterController;
    private static Paint paint;

    public WaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        if(!isInEditMode()) {
            waterController = new WaterController(this, context);
        }

    }

    public static Paint getPaint() {
        return paint;
    }

    public void onDraw(Canvas canvas){
        if(!isInEditMode()) {
            for (Water w : waterController.getWaters()) {
                w.draw(canvas);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isInEditMode()) {
            waterController.WIDTH = getMeasuredWidth();
            waterController.HEIGHT = getMeasuredHeight();
        }
    }
}
