package parking.roca.dani.parking;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Dani on 22/10/2015.
 */
public class ButtonListFragment extends Fragment {
    private CallBack mCallbacks = CallbacksVacios;

    public interface CallBack {
        public void onEntradaSelecionada(String id);
    }


    private static CallBack CallbacksVacios = new CallBack() {
        @Override
        public void onEntradaSelecionada(String id) {
        }
    };

    public ButtonListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_buttons_parking, container, false);
        /*((Button) rootView.findViewById(R.id.Entrada)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEntradaSelecionada("entrada");
            }
        });*/

        ((Button) rootView.findViewById(R.id.Acciones)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEntradaSelecionada("acciones");
            }
        });

        ((Button) rootView.findViewById(R.id.Clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEntradaSelecionada("limpiar");
            }
        });

        ((Button) rootView.findViewById(R.id.Ayuda_Primera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEntradaSelecionada("ayuda");
            }
        });

        ((Button) rootView.findViewById(R.id.Deshacer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onEntradaSelecionada("deshacer");
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof CallBack)) {
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento");
        }

        mCallbacks = (CallBack) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = CallbacksVacios;
    }


    /*@Override
    public void onListItemClick(ListView listView, View view, int posicion, long id) {
        super.onListItemClick(listView, view, posicion, id);
        //mCallbacks.onEntradaSelecionada(Lista_contenido.ENTRADAS_LISTA.get(posicion).id);
    }*/

}
