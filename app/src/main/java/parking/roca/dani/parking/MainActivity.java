package parking.roca.dani.parking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void callManagement(View view){
        Intent intent = new Intent(this, Parking_Management.class);
        startActivity(intent);
    }

    public void callAyudaPrincipal(View view){
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

    public void callAcerca(View view){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, "Dani Roca", duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
