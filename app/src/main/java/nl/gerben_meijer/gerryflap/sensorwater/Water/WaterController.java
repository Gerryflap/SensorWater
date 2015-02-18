package nl.gerben_meijer.gerryflap.sensorwater.Water;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Gerryflap on 2015-02-17.
 */
public class WaterController implements SensorEventListener{

    public int WIDTH = 1000;
    public int HEIGHT = 1000;
    private static final int WATER_NUM = 30;

    private Water[] waters;
    private WaterView waterView;
    private Sensor sensor;
    private SensorManager sensorManager;


    public WaterController(WaterView waterView, Context context){

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        sensorManager.registerListener(this, sensor, 60);

        this.waterView = waterView;
        Water.waterController = this;
        waters = new Water[WATER_NUM];
        for(int i = 0; i < waters.length; i++){
            waters[i] = new Water(i/5, i%5);
        }
    }

    public Water[] getWaters() {
        return waters;
    }

    public void calculateWaterLocations(float ddx, float ddy){
        for(Water w: waters){
            w.calculateCohesion();
            w.accelerate(ddx, ddy);
        }

        for(Water w: waters) {
            w.move();
        }

        waterView.invalidate();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        calculateWaterLocations(-event.values[0], event.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
