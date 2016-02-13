package parking.roca.dani.parking;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dani on 26/10/2015.
 */
public class PlazaParking {
    public static float[][] coches = new float[12][2]; //0 = x, 1 = y
    public static int[] colorCoches = new int[12];// int = color
    static float restaWidth = 0;
    static float restaHeight = 0;

    public static int getColor(int col){
        switch (col){
            //magenta
            case 0: return 0xCCA6429D;
            //case 1: return Color.BLUE;
            case 1: return 0xCC666BFF;
            //red
            case 2: return 0xCCCC5252;
            //green
            case 3: return 0xCC6AB85C;
            //light blue
            case 4: return 0xBB57D9CC;
            default: return Color.BLACK;
        }
    }

    public static void initCoches(int[] color){
        //Log.i("AA:", "initCoches");
        for (int i = 0; i < 12; i++) {
            if (color[i] != -1) coches[i] = getPlaza(i+1);
            //colorCoches[i] = getColor(color[i]);
            colorCoches[i] = color[i];
        }
    }

    public static void updateCoche(int plaza, int color){
        //Log.i("AA:", "updateCoche");
        //colorCoches[plaza-1] = getColor(color);
        colorCoches[plaza-1] = color;
        coches[plaza-1] = getPlaza(plaza);
    }

    public static void quitarCoche(int plaza){
        colorCoches[plaza-1] = -1;
    }

    public static float[] getPlaza(int num){
        return coords(num);
    }

    public static boolean esMinus(int num){
        return (num == 5 || num == 9);
    }
    public static float _ARatioW = 1.5f, _ARatioH = 1.5f;

    public static void setRatio (int width, int height){
        //Log.i("AA:", "setRatio");
        _ARatioW = (float)width/1920f;
        _ARatioH = (float)height/846f;
        restaWidth = 95f*PlazaParking._ARatioW;
        restaHeight = 90f*PlazaParking._ARatioH;
    }

    private static float[] coords(int num){
        switch (num){
            case 1: return new float[] {(205f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};
            case 2: return new float[] {(500f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};
            case 3: return new float[] {(796f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};

            case 4: return new float[] {(205f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};
            case 5: return new float[] {(500f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};
            case 6: return new float[] {(796f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};

            case 7: return new float[] {(1098f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};
            case 8: return new float[] {(1394f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};
            case 9: return new float[] {(1695f*_ARatioW - restaWidth), (205f*_ARatioH - restaHeight)-35};

            case 10: return new float[] {(1098f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};
            case 11: return new float[] {(1394f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};
            case 12: return new float[] {(1695f*_ARatioW - restaWidth), (620f*_ARatioH - restaHeight)+35};

            default: return new float[] {400, 400};
        }
    }
}
/*
Plaza 1:  X: 203.80058. Y: 243.48126
Plaza 2: X: 498.73895. Y: 247.90814
Plaza 3: X: 796.8832. Y: 255.388

Plaza 7: X: 1100.8285. Y: 250.96112
Plaza 8: X: 1392.8663. Y: 256.6092
Plaza 9: X: 1700.8339. Y: 240.27563

Plaza 4: X: 213.26547. Y: 611.3682
Plaza 5: 501.79214. Y: 620.0693
Plaza 6: X: 796.7305. Y: 632.1286

Plaza 10: X: 1095.4854. Y: 618.8481
Plaza 11: X: 1396.3774. Y: 625.71735
Plaza 12:  X: 1690.5525. Y: 616.10034
 */