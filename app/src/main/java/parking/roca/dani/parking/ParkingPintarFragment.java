package parking.roca.dani.parking;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dani on 22/10/2015.
 */
public class ParkingPintarFragment extends Fragment {
    public static CanvasParking canvas;

   /* public interface CallBack{
        private void
    }*/

    public ParkingPintarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_parking__management);
        /*setListAdapter(new Lista_adaptador(getActivity(), R.layout.layout_elemento_listado, Lista_contenido.ENTRADAS_LISTA){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_titulo);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((Lista_contenido.Lista_entrada) entrada).textoEncima);

                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen_miniatura);
                    if (imagen_entrada != null)
                        imagen_entrada.setImageResource(((Lista_contenido.Lista_entrada) entrada).idImagen);
                }
            }
        });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pintar_parking, container, false);
        canvas = (CanvasParking) rootView.findViewById(R.id.canvas);
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Log.i("Coords: ", "X: " + event.getX() + ". Y: " + event.getY());
                canvas.playG = false;
                return false;
            }
        });
        //canvas.init();
        //
        //Mostramos el contenido al usuario
        /*if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.textView_superior)).setText(mItem.textoEncima);
            ((TextView) rootView.findViewById(R.id.textView_inferior)).setText(mItem.textoDebajo);
            ((ImageView) rootView.findViewById(R.id.imageView_imagen)).setImageResource(mItem.idImagen);
        }*/

       return rootView;
    }

}
