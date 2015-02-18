package nl.gerben_meijer.gerryflap.sensorwater.Water;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

/**
 * Created by Gerryflap on 2015-02-17.
 */
public class Water {
    public static final int DIAMETER = 60;
    public static final float REFLECTION_FACTOR = 0.7f;
    private float x;
    private float y;
    private float dx;
    private float dy;
    private static final double COHESIVENESS = 30;
    private static final double WEIGHT = 3;
    public static WaterController waterController;

    public Water(float x, float y){
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
    }

    public void calculateCohesion(){
        Water[] waters = waterController.getWaters();
        for (Water w: waters){
            if(!w.equals(this) && w != null){
                float[] cohesion = getCohesionTo(w);
                accelerate(cohesion[0], cohesion[1]);
            }
        }
    }

    private float[] getCohesionTo(Water water){
        double distance = Math.sqrt(Math.pow(this.x - water.getX(), 2) + Math.pow(this.y - water.getY(), 2));
        if(distance != 0) {
            double xDistance = water.getX() - this.x;
            double yDistance = water.getY() - this.y;
            double cohesion = cohesionFormula(Math.abs(distance), Math.signum(distance));
            double xCohesion = cohesion * (xDistance / distance);
            double yCohesion = cohesion * (yDistance / distance);
            return new float[]{(float) xCohesion, (float) yCohesion};
        }
        else {
           return new float[]{new Random().nextFloat()*-0.5f, new Random().nextFloat()-0.5f};
        }
    }

    private double cohesionFormula(double distance, double sign){
        double cohesion = 0.01 * (1/Math.pow(distance/250, 2)-(0.04/Math.pow(distance/250, 3)));
        if (cohesion > 1){
            cohesion = 1;
        } else if(cohesion < -1) {
            cohesion = -1;
        }

        return COHESIVENESS * sign * cohesion;
    }

    public void accelerate(float ddx, float ddy){
        dx += ddx/WEIGHT;
        dy += ddy/WEIGHT;
    }

    public void move(){
        this.x += dx;
        this.y += dy;
        if (this.x > waterController.WIDTH- DIAMETER){
            this.x = waterController.WIDTH- DIAMETER;
            this.dx = -REFLECTION_FACTOR*dx;
        }

        if (this.y > waterController.HEIGHT- DIAMETER){
            this.y = waterController.HEIGHT- DIAMETER;
            this.dy = -REFLECTION_FACTOR*dy;
        }

        if (this.x < 0+ DIAMETER){
            this.x = 0+ DIAMETER;
            this.dx = -REFLECTION_FACTOR*dx;
        }

        if (this.y < 0+ DIAMETER){
            this.y = 0+ DIAMETER;
            this.dy = -REFLECTION_FACTOR*dy;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x, y, DIAMETER, WaterView.getPaint());
    }

    public String toString(){
        return String.format("<Water Instance {x: %s, y: %s, dx: %s, dy: %s} >", x, y, dx, dy);
    }
}
