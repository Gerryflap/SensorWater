package nl.gerben_meijer.gerryflap.sensorwater.Water;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerryflap on 2015-02-17.
 */
public class WaterController implements SensorEventListener{

    public int WIDTH = 1000;
    public int HEIGHT = 1000;
    private static final int WATER_NUM = 40;

    private List<Water> toBeCaluculated;
    private List<Water> waterClone;
    private Water[] waters;
    private WaterView waterView;
    private Sensor sensor;
    private SensorManager sensorManager;


    public WaterController(WaterView waterView, Context context){

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        sensorManager.registerListener(this, sensor, 40);


        this.waterClone = new ArrayList<Water>();
        this.toBeCaluculated = new ArrayList<Water>();
        this.waterView = waterView;
        Water.waterController = this;
        waters = new Water[WATER_NUM];
        for(int i = 0; i < waters.length; i++){
            waters[i] = new Water(i/5, i%5);
            waterClone.add(waters[i]);
        }
    }

    public Water[] getWaters() {
        return waters;
    }

    public List<Water> getToBeCalculated(){
        return this.toBeCaluculated;
    }

    public void calculateWaterLocations(float ddx, float ddy){
        toBeCaluculated.addAll(waterClone);
        for(Water w: waters){
            w.calculateCohesion();
            w.accelerate(ddx, ddy);
            toBeCaluculated.remove(w);
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
