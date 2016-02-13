package parking.roca.dani.parking;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.InputStream;

/**
 * Created by Dani on 25/10/2015.
 */
public class CanvasParking extends View {
    private BitmapFactory.Options op = new BitmapFactory.Options();
    private Resources resources;
    private Bitmap bitmapCoche;
    private boolean draw = false;
    private int _width, _height;
    Paint paint[];
    public static boolean playG = false;
    private Movie movie;
    private long _start_time;
    public static long entrar;
    private boolean firstTime = true;
    float coordsAlerta[];



    public CanvasParking(Context context) {
        super(context);
        InputStream is;
        is=getResources().openRawResource(R.raw.arrow);
        movie=Movie.decodeStream(is);
        resources = context.getResources();
        op.inMutable = true;
        init();
    }

    public CanvasParking(Context context, AttributeSet attrs) {
        super(context, attrs);
        InputStream is;
        is=getResources().openRawResource(R.raw.arrow);
        movie=Movie.decodeStream(is);
        resources = context.getResources();
        op.inMutable = true;
        init();
    }

    public CanvasParking(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InputStream is;
        is=getResources().openRawResource(R.raw.arrow);
        movie=Movie.decodeStream(is);
        resources = context.getResources();
        op.inMutable = true;
        init();
    }

    public void init(){
        coordsAlerta = new float[] {108, 120};
        bitmapCoche = BitmapFactory.decodeResource(resources, R.drawable.coche_azul);
        bitmapCoche = Bitmap.createScaledBitmap(bitmapCoche, 190, 250, true);
        //Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter)
        paint = new Paint[5];
        ColorFilter filter;
        for (int i = 0; i < 5; i++) {
            paint[i] = new Paint();
            paint[i].setAntiAlias(true);
            paint[i].setFilterBitmap(true);
            paint[i].setDither(true);
            filter = new LightingColorFilter(PlazaParking.getColor(i), 1);
            paint[i].setColorFilter(filter);
        }
        //paint.setColor(Color.argb(255,205,100,100));
        //paint.setAlpha(80);
        draw = true;
    }

    @Override
    public void dispatchDraw(Canvas canvas){
        //try {
        super.dispatchDraw(canvas);
        //}catch (StackOverflowError e) {
            /*this.postInvalidateDelayed(100);
            Log.i("Pasa: ", "error");
            /*mHandler.postDelayed(new Runnable() {
                public void run() {
                    invalidate();}}, 1);}*/
        canvas.save();
        if (!firstTime && draw && playG){
            int width = canvas.getWidth();
            long _current_time = android.os.SystemClock.uptimeMillis();
            if (0 == _start_time) {
                _start_time = _current_time;
            }
            if (playG && (null != movie)) {
                final int _relatif_time = (int) ((_current_time - _start_time) % (movie.duration()+1));
                movie.setTime(_relatif_time);
                movie.draw(canvas, coordsAlerta[0], coordsAlerta[1]);
                if (_current_time - entrar > 3600) playG = false;
                //movie.draw(canvas, _width / 2, _height - 5);
                //movie.draw(canvas, _width / 2, 0);
            }
            else{
                //canvas.drawBitmap(dice2, (canvas.getWidth()/2)+(dice.getWidth()/3), (canvas.getHeight()/2-temany_curt), null);
            }
            //if (playG)
            this.postInvalidateDelayed(1000/50);
        }
        else if (firstTime){
            //Log.i("AA:", "firstTime");
            _height = getHeight();
            _width = getWidth();
            firstTime = false;
            PlazaParking.setRatio(_width, _height);
            Parking_Management.iniciarCoches();
            //coordsAlerta = PlazaParking.getPlaza(1);
            //coordsAlerta[0] -= 95*PlazaParking._ARatioW;
            //coordsAlerta[1] -= 90*PlazaParking._ARatioH;
            bitmapCoche = Bitmap.createScaledBitmap(bitmapCoche, (int)(180*PlazaParking._ARatioW),
                    (int)(225*PlazaParking._ARatioH), true);
            //image.setImageBitmap(resultBitmap);
            invalidate();
        }
        else if (draw){
            //canvas.drawBitmap(bitmapCoche, coordsAlerta[0], coordsAlerta[1], paint);
            for(int i = 0; i < 12; i++){
                //Log.i("aa: ", "Valor: " + PlazaParking.colorCoches[i]);
                if(PlazaParking.colorCoches[i] != -1) {
                    //Log.i("Valores: ", "X: " + x + ". Y: " + y);
                    //canvas.drawBitmap(bitmapCoche, 200f, 200f, paint);
                    canvas.drawBitmap(bitmapCoche, PlazaParking.coches[i][0], PlazaParking.coches[i][1],
                            paint[PlazaParking.colorCoches[i]]);
                }
            }
            //canvas.drawBitmap(figura, coord[0], coord[1], paint);
            invalidate();
        }
        else {
            _height = getHeight();
            _width = getWidth();
            firstTime = false;
            invalidate();
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        //super.draw(canvas);
    }

    public void plazaAlerta(int plaza){
        coordsAlerta = PlazaParking.getPlaza(plaza);
        //coordsAlerta[0] -= 95;
        //coordsAlerta[1] -= 90;
    }


}
