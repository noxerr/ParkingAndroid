package parking.roca.dani.parking;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class Parking_Management extends FragmentActivity implements ButtonListFragment.CallBack{

    private SharedPreferences prefs;
    private Random ran;
    private static DBInOut db;
    private static ArrayList<ArrayList<Object>> coches;
    private int[] diasMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking__management);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        diasMes = new int[] {31,28,31,30,31,30,31,31,30,31,30,31};
        prefs = this.getSharedPreferences("matriculas", Context.MODE_PRIVATE);
        ran = new Random();
        db = new DBInOut(getApplicationContext());
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
        if (!prefs.contains("score1")){
            prefs.edit().putInt("score1",00000).apply();*/
    }

    public static void iniciarCoches(){
        coches = db.cochesDentro();
        int colores[] = new int[12];
        for (int i = 0; i < 12; i++) colores[i] = -1;
        int plazaAux = 0;
        for (int i = 0; i < coches.size(); i++){
            plazaAux = (int) coches.get(i).get(3);
            //Log.i("Plaza", "plaza aux: " + plazaAux);
            colores[plazaAux-1] = (int) coches.get(i).get(2);
        }
        PlazaParking.initCoches(colores);
        //Log.i("AA:", "finOncreate");
    }

    @Override
    public void onEntradaSelecionada(String id) {
        if (id.equalsIgnoreCase("ayuda")){
            final Dialog ayudaD = new Dialog(this, R.style.AlertCustom_two);
            ayudaD.setContentView(R.layout.ayuda_principal);
            ayudaD.setCanceledOnTouchOutside(true);
            Button volver = (Button) ayudaD.findViewById(R.id.volverAyudaPrincipal);
            volver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ayudaD.dismiss();
                }
            });
            ayudaD.show();
        }

        else if (id.equalsIgnoreCase("acciones")) {
            //if((prefs.getAll()).size() < 12){
            //Log.i("size: ", "coches size: "+ coches.size());
            final Dialog principal = new Dialog(this, R.style.AlertCustom_two);
            principal.setContentView(R.layout.menu_parking);
            configAcciones(principal);
            principal.setCanceledOnTouchOutside(true);
            principal.show();
        }

        else if (id.equalsIgnoreCase("deshacer")) {
            //if((prefs.getAll()).size() < 12){
            //Log.i("size: ", "coches size: "+ coches.size());
            if (!(prefs.getInt("action", 2) == 2)) {
                final Dialog principal = new Dialog(this, R.style.AlertCustom_two);
                principal.setContentView(R.layout.deshacer);
                configDeshacer(principal);
                principal.setCanceledOnTouchOutside(true);
                principal.show();
            }
            else{
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(Parking_Management.this, "No hay acciones disponibles para deshacer", duration);
                toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }

        else if (id.equalsIgnoreCase("limpiar")) {
            for (String s : prefs.getAll().keySet()){
                prefs.edit().remove(s).apply();
            }//TODO UPDATE COCHE
            for(int i = 0; i < coches.size(); i++){
                db.deleteRowTabla((String) coches.get(i).get(0));
            }
            for (int i = 1; i < 13; i++) PlazaParking.updateCoche(i, -1);
            coches = db.cochesDentro();
        }
        /*if (dosFragmentos) {
            Bundle arguments = new Bundle();
            arguments.putString(Fragment_Detalle.ARG_ID_ENTRADA_SELECIONADA, id);
            Fragment_Detalle fragment = new Fragment_Detalle();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_contenedor_detalle, fragment).commit();
        } else {
            Intent detailIntent = new Intent(this, Activity_Detalle.class);
            detailIntent.putExtra(Fragment_Detalle.ARG_ID_ENTRADA_SELECIONADA, id);
            startActivity(detailIntent);
        }*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parking__management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private void configAcciones(final Dialog principal) {
        Button entrar = (Button) principal.findViewById(R.id.entrarB);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coches.size() < 12){
                    final Dialog principal2 = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                    principal2.setContentView(R.layout.modos);
                    configEntrada(principal2, principal);
                    principal2.setCanceledOnTouchOutside(true);
                    principal2.show();
                }
                else {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(Parking_Management.this, "No quedan plazas libres", duration);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });

        Button salir = (Button) principal.findViewById(R.id.salirB);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coches.size() > 0) {
                    final Dialog salida = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                    salida.setContentView(R.layout.salir);
                    configSalida(salida, principal);
                    salida.setCanceledOnTouchOutside(true);
                    salida.show();
                } else {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(Parking_Management.this, "No hay coches", duration);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });

        final Button matriculas = (Button) principal.findViewById(R.id.matriculas);
        matriculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog matricula = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                matricula.setContentView(R.layout.matriculas);
                matricula.setCanceledOnTouchOutside(true);
                Button volver = (Button) matricula.findViewById(R.id.okMatr);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        matricula.dismiss();
                    }
                });
                TextView plazas[] = new TextView[12];
                plazas[0] = (TextView) matricula.findViewById(R.id.plaza1);
                plazas[1] = (TextView) matricula.findViewById(R.id.plaza2);
                plazas[2] = (TextView) matricula.findViewById(R.id.plaza3);
                plazas[3] = (TextView) matricula.findViewById(R.id.plaza4);
                plazas[4] = (TextView) matricula.findViewById(R.id.plaza5);
                plazas[5] = (TextView) matricula.findViewById(R.id.plaza6);
                plazas[6] = (TextView) matricula.findViewById(R.id.plaza7);
                plazas[7] = (TextView) matricula.findViewById(R.id.plaza8);
                plazas[8] = (TextView) matricula.findViewById(R.id.plaza9);
                plazas[9] = (TextView) matricula.findViewById(R.id.plaza10);
                plazas[10] = (TextView) matricula.findViewById(R.id.plaza11);
                plazas[11] = (TextView) matricula.findViewById(R.id.plaza12);
                for (int i = 0; i < coches.size(); i++) {

                    plazas[(int) coches.get(i).get(3) - 1].setText((String) coches.get(i).get(0));
                    //if (((String) coches.get(i).get(0)).equalsIgnoreCase(matricula));
                }
                matricula.show();
            }
        });

        final ImageButton ayuda = (ImageButton) principal.findViewById(R.id.help);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ayudaD = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                ayudaD.setContentView(R.layout.ayuda_inside);
                ayudaD.setCanceledOnTouchOutside(true);
                Button volver = (Button) ayudaD.findViewById(R.id.volverAyuda);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ayudaD.dismiss();
                    }
                });
                ayudaD.show();
            }

        });


        final Button ingresos = (Button) principal.findViewById(R.id.ingresos);
        ingresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ingresosD = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                ingresosD.setContentView(R.layout.ingresos);
                ingresosD.setCanceledOnTouchOutside(true);
                Button volver = (Button) ingresosD.findViewById(R.id.volverIngresos);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ingresosD.dismiss();
                    }
                });
                configIngresos(ingresosD);
                ingresosD.show();
            }

        });


        final Button resumenMes = (Button) principal.findViewById(R.id.resum);
        resumenMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ingresosD = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                ingresosD.setContentView(R.layout.resumen_mensual);
                ingresosD.setCanceledOnTouchOutside(true);
                Button volver = (Button) ingresosD.findViewById(R.id.volverResumenIn);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ingresosD.dismiss();
                    }
                });
                configResumenIngr(ingresosD);
                ingresosD.show();
            }

        });


        final Button export = (Button) principal.findViewById(R.id.export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExportDatabaseCSVTask().execute();
            }});

    }






    /////////////////////////////////////////////////////////////////////////////////

    //////////////////FIN DE PONER CLICKS EN LOS BOTONES DEL MENU///////////
    /////////////////////////////////////////////////////////////////////////////////




    private void configDeshacer(final Dialog principal) {
        TextView texto = (TextView) principal.findViewById(R.id.textDeshacer);
        texto.setText("¿Seguro que quieres deshacer la ultima " + ((prefs.getInt("action",2) == 0) ? "entrada " : "salida ") +
            "con matricula: " + prefs.getString("matr", " - ") + "?");
        final Button desHacer = (Button) principal.findViewById(R.id.deshacerDialog);
        desHacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getInt("action",2) == 0){
                    PlazaParking.quitarCoche(prefs.getInt("plaza", 1));
                    db.DeshacerEntrar(prefs.getString("matr", " - "));
                    coches = db.cochesDentro();
                    /*db.SacarCoche(matricula, timeEntrada, timeSalida, precioV);*/
                }
                else {
                    PlazaParking.updateCoche(prefs.getInt("plaza", 1), prefs.getInt("color", 1));
                    db.DeshacerSacarCoche(prefs.getString("matr", " - "), prefs.getString("entrada", "-"),
                            prefs.getString("fecha", "-"), prefs.getInt("color", 1), prefs.getInt("plaza", 1));
                    coches = db.cochesDentro();
                }

                prefs.edit().putInt("action",2).apply();
                principal.dismiss();
            }

        });

        Button volver = (Button) principal.findViewById(R.id.cancelDeshacer);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                principal.dismiss();
            }
        });
    }

    private void configResumenIngr(final Dialog ingresosD) {
        final Spinner spinnerAno = (Spinner) ingresosD.findViewById(R.id.rano);
        final Spinner spinnerAno2 = (Spinner) ingresosD.findViewById(R.id.rano2);
        final Spinner spinnerMes = (Spinner) ingresosD.findViewById(R.id.rmes);
        final Spinner spinnerMes2 = (Spinner) ingresosD.findViewById(R.id.rmes2);

        ArrayList<String> years = new ArrayList();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1990; i--) years.add(String.valueOf(i));
        ArrayAdapter<String> adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, years); //simple_spinner_item

        spinnerAno.setAdapter(adapter);
        spinnerAno.setPrompt("Año");
        spinnerAno2.setAdapter(adapter);
        spinnerAno2.setPrompt("Año");

        ArrayList<Integer> months = new ArrayList();
        adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, months);

        for (int i = 1; i <= 12; i++)  months.add(i);
        spinnerMes.setAdapter(adapter);
        spinnerMes.setPrompt("Mes");
        spinnerMes2.setAdapter(adapter);
        spinnerMes2.setPrompt("Mes");

        Button Consultar = (Button) ingresosD.findViewById(R.id.consultaResumen);
        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inicio, fin;
                DecimalFormat dff = new DecimalFormat("0.00");
                String auxIn = spinnerAno.getSelectedItem().toString();
                String auxIn2 = spinnerMes.getSelectedItem().toString();
                if (auxIn.length() < 2) auxIn = '0' + auxIn;
                if (auxIn2.length() < 2) auxIn2 = '0' + auxIn2;
                inicio = auxIn + "." + auxIn2 + ".00.00." + "00";

                auxIn = spinnerAno2.getSelectedItem().toString();
                auxIn2 = spinnerMes2.getSelectedItem().toString();
                if (auxIn.length() < 2) auxIn = '0' + auxIn;
                if (auxIn2.length() < 2) auxIn2 = '0' + auxIn2;
                fin = auxIn + "." + auxIn2 + ".31.24.00";

                ArrayList<ArrayList<Object>> registro = db.cochesPeriodo(inicio, fin);


                float total = 0;
                String aux;
                for (int i = 0; i < registro.size(); i++) {
                    aux = ((String) registro.get(i).get(3)).replace(',','.');
                    if (!aux.equalsIgnoreCase("-")) total += Float.parseFloat(aux);
                }


                final Dialog resumen = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                resumen.setContentView(R.layout.resumen_mes);
                resumen.setCanceledOnTouchOutside(true);
                Button volver = (Button) resumen.findViewById(R.id.volverResumenMes);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resumen.dismiss();
                    }
                });
                ((TextView) resumen.findViewById(R.id.entradas)).setText("" + db.cochesPeriodoEntradas(inicio, fin));
                ((TextView) resumen.findViewById(R.id.salidas)).setText("" + registro.size());
                ((TextView) resumen.findViewById(R.id.recMedia)).setText(dff.format(total/registro.size()) + " €");
                ((TextView) resumen.findViewById(R.id.distintos)).setText(""+db.cochesPeriodoDistinct(inicio, fin));
                ((TextView) resumen.findViewById(R.id.recaudacion)).setText(dff.format(total) + " €");
                resumen.show();
            }});
    }

    private void configIngresos(final Dialog ingresosD) {
        final Spinner spinnerAno = (Spinner) ingresosD.findViewById(R.id.ano);
        final Spinner spinnerAno2 = (Spinner) ingresosD.findViewById(R.id.ano2);

        final Spinner spinnerMes = (Spinner) ingresosD.findViewById(R.id.mes);
        final Spinner spinnerMes2 = (Spinner) ingresosD.findViewById(R.id.mes2);

        final Spinner spinnerDia = (Spinner) ingresosD.findViewById(R.id.dia);
        final Spinner spinnerDia2 = (Spinner) ingresosD.findViewById(R.id.dia2);

        final Spinner spinnerHora = (Spinner) ingresosD.findViewById(R.id.hora);
        final Spinner spinnerHora2 = (Spinner) ingresosD.findViewById(R.id.hora2);

        ArrayList<String> years = new ArrayList();
        //years.add("Año");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1990; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, years); //simple_spinner_item
        //select_dialog_item

        spinnerAno.setAdapter(adapter);
        spinnerAno.setPrompt("Año");
        spinnerAno2.setAdapter(adapter);
        spinnerAno2.setPrompt("Año");

        ArrayList<Integer> months = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, months);

        spinnerMes.setAdapter(adapter);
        spinnerMes.setPrompt("Mes");
        spinnerMes2.setAdapter(adapter);
        spinnerMes2.setPrompt("Mes");


        ArrayList<Integer> days = new ArrayList();
        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }
        adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, days);

        spinnerDia.setAdapter(adapter);
        spinnerDia.setPrompt("Dia");
        spinnerDia2.setAdapter(adapter);
        spinnerDia2.setPrompt("Dia");


        ArrayList<Integer> hours = new ArrayList();
        for (int i = 0; i <= 23; i++) {
            hours.add(i);
        }
        adapter = new ArrayAdapter(Parking_Management.this,
                android.R.layout.simple_spinner_dropdown_item, hours);

        spinnerHora.setAdapter(adapter);
        spinnerHora.setPrompt("Hora");
        spinnerHora2.setAdapter(adapter);
        spinnerHora2.setPrompt("Hora");

        final CheckBox checkHoy = (CheckBox) ingresosD.findViewById(R.id.checkToday);

        checkHoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout rolones1 = (LinearLayout) ingresosD.findViewById(R.id.rolones);
                LinearLayout rolones2 = (LinearLayout) ingresosD.findViewById(R.id.rolones2);
                if (((CheckBox) v).isChecked()) {
                    rolones1.setVisibility(View.INVISIBLE);
                    rolones2.setVisibility(View.INVISIBLE);
                }
                else {
                    rolones1.setVisibility(View.VISIBLE);
                    rolones2.setVisibility(View.VISIBLE);
                }
            }
        });
        final DecimalFormat dff = new DecimalFormat("00");
        Button Consultar = (Button) ingresosD.findViewById(R.id.consulta);
        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inicio, fin;
                if(!checkHoy.isChecked()) {
                    inicio = spinnerAno.getSelectedItem().toString() + "." +
                            spinnerMes.getSelectedItem().toString() + "." + spinnerDia.getSelectedItem().toString() +
                            "." + spinnerHora.getSelectedItem().toString() + "." + "00";
                    fin = spinnerAno2.getSelectedItem().toString() + "." +
                            spinnerMes2.getSelectedItem().toString() + "." + spinnerDia2.getSelectedItem().toString()
                            + "." + spinnerHora2.getSelectedItem().toString() + "." + "00";
                }
                else {
                    Calendar cal = new GregorianCalendar();
                    inicio = ""+cal.get(Calendar.YEAR) +
                            "." + dff.format(cal.get(Calendar.MONTH) + 1) + "." + dff.format(cal.get(Calendar.DAY_OF_MONTH)) +
                            ".00.00";
                    fin = ""+cal.get(Calendar.YEAR) +
                            "." + dff.format(cal.get(Calendar.MONTH) + 1) + "." + dff.format(cal.get(Calendar.DAY_OF_MONTH)) +
                            "." + dff.format(cal.get(Calendar.HOUR_OF_DAY)) + "." + dff.format(cal.get(Calendar.MINUTE));
                }
                ArrayList<ArrayList<Object>> registro = db.cochesPeriodo(inicio, fin);

                final Dialog resumen = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                resumen.setContentView(R.layout.ingresos_resumen);
                resumen.setCanceledOnTouchOutside(true);
                Button volver = (Button) resumen.findViewById(R.id.volverResumen);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resumen.dismiss();
                    }
                });
                configResumen(resumen, registro);
                resumen.show();
            }});
    }


    private void configResumen(Dialog resumen, final ArrayList<ArrayList<Object>> registro){
        final TextView totalText = (TextView) resumen.findViewById(R.id.totalTicket);
        final TextView matrText = (TextView) resumen.findViewById(R.id.resumenMatr);
        final TextView entradaText = (TextView) resumen.findViewById(R.id.resumenEntrada);
        final TextView salidaText = (TextView) resumen.findViewById(R.id.resumenSalida);
        final TextView cobradoText = (TextView) resumen.findViewById(R.id.resumenCobrado);
        float total = 0;
        String aux;
        matrText.setText("");
        entradaText.setText("");
        salidaText.setText("");
        cobradoText.setText("");
        for (int i = 0; i < registro.size(); i++) {
            matrText.append((String) registro.get(i).get(0)+ "\n\n\n");
            entradaText.append(convertDate((String) registro.get(i).get(1)) + "\n\n");
            aux = (String) registro.get(i).get(2);
            if (aux.length()>2) salidaText.append(convertDate((String) registro.get(i).get(2)) + "\n\n");
            else salidaText.append(" - " + "\n\n\n");
            aux = ((String) registro.get(i).get(3)).replace(',','.');
            if (!aux.equalsIgnoreCase("-")) total += Float.parseFloat(aux);
            cobradoText.append(aux + "\n\n\n");
        }
        DecimalFormat dff = new DecimalFormat("0.00");
        totalText.setText(dff.format(total) + " €");
    }

    private String convertDate(String time){
        String aux11, aux12, aux10;
        int ind1 = time.lastIndexOf(".");
        int ind11 = time.lastIndexOf(".", ind1-1);
        aux10 = time.substring(0,ind11);
        aux11 = time.substring(ind11+1,ind1);
        aux12 = time.substring(ind1+1,time.length());

        DecimalFormat dff = new DecimalFormat("00");
        String salida;
        salida = aux10 + "\n" + dff.format(Integer.valueOf(aux11)) +
                ":" + dff.format(Integer.valueOf(aux12));
        //" €"
        return salida;
    }

    private void configEntrada(final Dialog principal, final Dialog old){
        //LinearLayout CandadoCrazy = (LinearLayout) principal.findViewById(R.id.modoCrazy);
        // LinearLayout LibreCrazy = (LinearLayout) principal.findViewById(R.id.modoCrazyLibre);
        Button cancelar = (Button) principal.findViewById(R.id.bcancel);
        Button entrar = (Button) principal.findViewById(R.id.entrar);
        //final CheckBox minusv = (CheckBox) principal.findViewById(R.id.checkMinus);
        final EditText editText = (EditText) principal.findViewById(R.id.editMatricula);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                principal.cancel();
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matricula = String.valueOf(editText.getText());
                if (matricula.isEmpty()) editText.setError("La matricula no puede estar vacia");
                else {
                    matricula = matricula.toUpperCase();
                    boolean exist = false;
                    for (int i = 0; i < coches.size() && !exist; i++) {
                        if (((String) coches.get(i).get(0)).equalsIgnoreCase(matricula)) exist = true;
                    }
                    if (exist) editText.setError("El coche ya está dentro");
                        //if (prefs.contains(matricula)) editText.setError("El coche ya está dentro");
                    else {
                        Calendar cal = new GregorianCalendar();
                        int color;
                        if (prefs.contains(matricula)) color = prefs.getInt(matricula,-1);
                        else {
                            color = ran.nextInt(5);
                            prefs.edit().putInt(matricula, color).apply();
                        }
                        int plaza = 0;
                        boolean found = false, fifor;
                        while(!found){
                            plaza = ran.nextInt(12) + 1;
                            fifor = false;
                            for (int i = 0; i < coches.size() && !fifor; i++){
                                if (plaza == (int)coches.get(i).get(3)) fifor = true;
                            }
                            if (!fifor) found = true;
                        }
                        DecimalFormat dff = new DecimalFormat("00");
                        String time = ""+cal.get(Calendar.YEAR) +
                                "." + dff.format(cal.get(Calendar.MONTH) + 1) + "." + dff.format(cal.get(Calendar.DAY_OF_MONTH)) +
                                "." + dff.format(cal.get(Calendar.HOUR_OF_DAY)) + "." + dff.format(cal.get(Calendar.MINUTE));

                        /*String time = ""+cal.get(Calendar.YEAR) +
                                "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) +
                                "." + cal.get(Calendar.HOUR_OF_DAY) + "." + cal.get(Calendar.MINUTE);*/
                        //matricula.substring();
                        long succes = db.EntrarCoche(matricula,time,color,plaza);

                        if (succes != -1){
                            PlazaParking.updateCoche(plaza, color);
                            coches = db.cochesDentro();
                            ParkingPintarFragment.canvas.plazaAlerta(plaza);
                            /*CanvasParking.playG = true;
                            CanvasParking.entrar = android.os.SystemClock.uptimeMillis();
                            ParkingPintarFragment.canvas.invalidate();*/

                            prefs.edit().putInt("action",0).
                                    putString("fecha", time).
                                    putInt("plaza", plaza).
                                    putInt("color", color).
                                    putString("matr", matricula).apply();

                            principal.dismiss();
                            old.dismiss();

                            final Dialog ticket = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                            ticket.setContentView(R.layout.ticket);
                            Button cancelar = (Button) ticket.findViewById(R.id.okTicket);
                            cancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ticket.cancel();
                                    CanvasParking.playG = true;
                                    CanvasParking.entrar = android.os.SystemClock.uptimeMillis();
                                    ParkingPintarFragment.canvas.invalidate();
                                }
                            });
                            TextView matr = (TextView) ticket.findViewById(R.id.matriculaTicket);
                            matr.setText(matricula);

                            TextView ticketPlaza = (TextView) ticket.findViewById(R.id.plazaTicket);
                            ticketPlaza.setText("" + plaza);

                            String aux11, aux12,aux10;
                            int ind1 = time.lastIndexOf(".");
                            int ind11 = time.lastIndexOf(".", ind1-1);
                            aux10 = time.substring(0,ind11);
                            aux11 = time.substring(ind11+1,ind1);
                            aux12 = time.substring(ind1+1,time.length());


                            TextView sal = (TextView) ticket.findViewById(R.id.EntradaTicket);
                            sal.setText(aux10 + " - " +
                                    dff.format(Integer.valueOf(aux11)) +
                                    ":" + dff.format(Integer.valueOf(aux12)));
                            TextView precio = (TextView) ticket.findViewById(R.id.precioTicket);
                            precio.setText("2cent/min");
                            ticket.setCanceledOnTouchOutside(true);
                            ticket.show();
                        }
                        else{
                            int duration2 = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(Parking_Management.this, "Error al entrar coche", duration2);
                            db.SacarCoche(matricula, "Error", "Error", "0");
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                    }
                }
            }
        });
    }



    private void configSalida(final Dialog principal, final Dialog old){
        Button cancelar = (Button) principal.findViewById(R.id.bcancel2);
        Button entrar = (Button) principal.findViewById(R.id.salir);
        final EditText editText = (EditText) principal.findViewById(R.id.editMatriculaSalir);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                principal.cancel();
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String matricula = String.valueOf(editText.getText());
            if (matricula.isEmpty()) editText.setError("La matricula no puede estar vacia");
            else {
                matricula = matricula.toUpperCase();
                String timeEntrada = "";
                int plaza = 0;
                int color = 0;
                boolean exist = false;
                for (int i = 0; i < coches.size() && !exist; i++) {
                    if (((String) coches.get(i).get(0)).equalsIgnoreCase(matricula)) {
                        exist = true;
                        timeEntrada = (String) coches.get(i).get(1);
                        color = (Integer) coches.get(i).get(2);
                        plaza = (Integer) coches.get(i).get(3);
                    }
                }
                if (exist) {
                    DecimalFormat dff = new DecimalFormat("00");
                    Calendar cal = new GregorianCalendar();
                    String timeSalida = ""+cal.get(Calendar.YEAR) +
                            "." + dff.format(cal.get(Calendar.MONTH) + 1) + "." + dff.format(cal.get(Calendar.DAY_OF_MONTH)) +
                            "." + dff.format(cal.get(Calendar.HOUR_OF_DAY)) + "." + dff.format(cal.get(Calendar.MINUTE));
                    principal.dismiss();
                    old.dismiss();


                    final Dialog ticket = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                    ticket.setContentView(R.layout.ticket);
                    Button cancelar = (Button) ticket.findViewById(R.id.okTicket);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ticket.cancel();
                            CanvasParking.playG = true;
                            CanvasParking.entrar = android.os.SystemClock.uptimeMillis();
                            ParkingPintarFragment.canvas.invalidate();
                        }
                    });
                    TextView matr = (TextView) ticket.findViewById(R.id.matriculaTicket);
                    matr.setText(matricula);

                    TextView ticketPlaza = (TextView) ticket.findViewById(R.id.plazaTicket);
                    ticketPlaza.setText("" + plaza);


                    String aux11, aux12, aux21, aux22, aux10, aux20;
                    int ind1 = timeSalida.lastIndexOf("."), ind11 = timeSalida.lastIndexOf(".", ind1-1),
                    ind2 = timeEntrada.lastIndexOf("."), ind21 = timeEntrada.lastIndexOf(".", ind2-1);
                    aux10 = timeSalida.substring(0,ind11);
                    aux11 = timeSalida.substring(ind11+1,ind1);
                    aux12 = timeSalida.substring(ind1+1,timeSalida.length());
                    aux20 = timeEntrada.substring(0,ind21);
                    aux21 = timeEntrada.substring(ind21+1,ind2);
                    aux22 = timeEntrada.substring(ind2+1,timeEntrada.length());



                    TextView entr = (TextView) ticket.findViewById(R.id.EntradaTicket);
                    entr.setText(aux20 + " - " + dff.format(Integer.valueOf(aux21)) + ":" + dff.format(Integer.valueOf(aux22)));
                    TextView sal = (TextView) ticket.findViewById(R.id.SalidaTicket);
                    sal.setText(aux10 + " - " +
                            dff.format(Integer.valueOf(aux11)) +
                            ":" + dff.format(Integer.valueOf(aux12)));
                    TextView precio = (TextView) ticket.findViewById(R.id.precioTicket);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String precioV = df.format(calculaPrecio(timeEntrada, timeSalida));
                    precio.setText(precioV + " €");
                    ticket.setCanceledOnTouchOutside(true);

                    db.SacarCoche(matricula, timeEntrada, timeSalida, precioV);
                    PlazaParking.quitarCoche(plaza);
                    coches = db.cochesDentro();
                    ParkingPintarFragment.canvas.plazaAlerta(plaza);

                    prefs.edit().putInt("action",1).
                            putString("fecha", timeSalida).
                            putString("entrada", timeEntrada).
                            putInt("color", color).
                            putInt("plaza", plaza).
                            putString("matr", matricula).apply();

                    ticket.show();
                }
                else {
                    editText.setError("El coche NO está dentro");
                }
            }
            }
        });
    }

    public double calculaPrecio(String timeEntrada, String timeSalida){
        double precio = 0;
        int indice1 = 0, indice2 = 0, aux1, aux2;
        indice1 = timeSalida.indexOf(".", 0);
        indice2 = timeEntrada.indexOf(".", 0);
        int ano1 = Integer.valueOf(timeSalida.substring(0, indice1));
        int ano2 = Integer.valueOf(timeEntrada.substring(0, indice2));
        ////
        aux1 = indice1+1;
        aux2 = indice2+1;
        indice1 = timeSalida.indexOf(".", indice1+1);
        indice2 = timeEntrada.indexOf(".", indice2+1);
        int mes1 = Integer.valueOf(timeSalida.substring(aux1, indice1));
        int mes2 = Integer.valueOf(timeEntrada.substring(aux2, indice2));
        //
        aux1 = indice1+1;
        aux2 = indice2+1;
        indice1 = timeSalida.indexOf(".", indice1+1);
        indice2 = timeEntrada.indexOf(".", indice2+1);
        int dia1 = Integer.valueOf(timeSalida.substring(aux1, indice1));
        int dia2 = Integer.valueOf(timeEntrada.substring(aux2, indice2));
        //
        aux1 = indice1+1;
        aux2 = indice2+1;
        indice1 = timeSalida.indexOf(".", indice1+1);
        indice2 = timeEntrada.indexOf(".", indice2+1);
        int hora1 = Integer.valueOf(timeSalida.substring(aux1, indice1));
        int hora2 = Integer.valueOf(timeEntrada.substring(aux2, indice2));
        //
        aux1 = indice1+1;
        aux2 = indice2+1;
        indice1 = timeSalida.length();
        indice2 = timeEntrada.length();
        int minuto1 = Integer.valueOf(timeSalida.substring(aux1, indice1));
        int minuto2 = Integer.valueOf(timeEntrada.substring(aux2, indice2));
        int diasMes1 = 0, diasMes2 = 0;
        for (int i = 0; i < mes1-1; i++){
            diasMes1+= diasMes[i];
        }
        for (int i = 0; i < mes2-1; i++){
            diasMes2+= diasMes[i];
        }
        long minutosTotales1 = (minuto1 + (hora1 + (dia1 + ano1*365 + diasMes1)*24)*60);
        long minutosTotales2 = (minuto2 + (hora2 + (dia2 + ano2*365 + diasMes2)*24)*60);
        precio = (minutosTotales1-minutosTotales2) * 0.02;
        return precio;
    }



    /**
     * Created by Dani on 05/01/2016.
     */
    private class ExportDatabaseCSVTask extends AsyncTask<String, String, String> {
        private final ProgressDialog dialog = new ProgressDialog(Parking_Management.this);

        // to show Loading dialog box
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exportando datos...");
            this.dialog.show();
        }

        // to write process
        protected String doInBackground(final String... args) {
            String success;
            File myFile;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String TimeStampDB = sdf.format(cal.getTime());
            success = TimeStampDB;
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/parking_datos");
            if(!directory.isDirectory()) directory.mkdirs();

            myFile = new File(directory.getAbsolutePath() + "/Export_"+TimeStampDB+".csv");
            FileOutputStream fOut;
            OutputStreamWriter myOutWriter;
            try {
                myFile.createNewFile();
                fOut = new FileOutputStream(myFile);
                myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("Matricula;Fecha entrada;Fecha salida;Precio");
                myOutWriter.append("\n");
                String matr, fEntr, fSal, precio;
                ArrayList<ArrayList<Object>> registro = db.cochesPeriodo("0000", "9999");
                for (int i = 0; i < registro.size(); i++) {
                    matr = (String) registro.get(i).get(0);
                    fEntr = (String) registro.get(i).get(1);
                    fSal = (String) registro.get(i).get(2);
                    precio = (String) registro.get(i).get(3);
                    myOutWriter.append(matr+";"+fEntr+";"+fSal+";"+precio);
                    myOutWriter.append("\n");
                }
                myOutWriter.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                success = "false";
            }
            return success;
        }

        // close dialog and give msg
        protected void onPostExecute(String success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            //Log.i("Succes", "Value: " + success);
            if (!success.equalsIgnoreCase("false")) {
                final Dialog ok = new Dialog(Parking_Management.this, R.style.AlertCustom_two);
                ok.setContentView(R.layout.export_complete);
                ok.setCanceledOnTouchOutside(true);
                Button volver = (Button) ok.findViewById(R.id.okExport);
                volver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.dismiss();
                    }
                });
                TextView text = (TextView) ok.findViewById(R.id.exportText);
                text.setText("Los datos se han exportado con exito. El fichero esta en la carpeta Parking_datos del almacenamiento interno,\ncon el nombre: " +
                        success + ".");
                ok.show();
            } else{
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(Parking_Management.this, "Error al exportar. Reintentalo", duration);
                toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }
}




