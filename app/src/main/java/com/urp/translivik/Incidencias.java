package com.urp.translivik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Incidencias extends Activity {
    private Spinner spinner;
    private TextView vd;
    private String get,objetoseleccionado,act2;
    private int idinc;
    private Button btnregistrar;
    private Bundle b;
    private EditText descrip;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidencias);
        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent= getIntent();
        Bundle bundle = this.getIntent().getExtras();
        final String idchofer=bundle.getString("idservicio");

        vd=(TextView)findViewById(R.id.textView12);
        spinner= (Spinner)findViewById(R.id.spinnerd);
        btnregistrar=(Button)findViewById(R.id.btnregistrar);
        descrip=(EditText)findViewById(R.id.txtdescripcion);
        TareaWSListar obj=new TareaWSListar();
        obj.execute();

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        Toast.makeText(getApplicationContext(), "Seleccionado: " +
                                parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        objetoseleccionado = parent.getItemAtPosition(position).toString();
                        vd.setText(objetoseleccionado);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        vd.setText("");
                    }
                });

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(descrip.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Describa la incidencia...", Toast.LENGTH_SHORT).show();
                }else {
                    String idtipo = vd.getText().toString().substring(0, vd.getText().toString().indexOf('°'));

                    Toast.makeText(getApplicationContext(), insertarincidencia(getidincidencia(), descrip.getText().toString().replaceAll(" ", "%20"), idtipo, idchofer, getdatetimesistema().toString().replaceAll(" ", "%20"), getdatetimesistema().toString().replaceAll(" ", "%20")), Toast.LENGTH_SHORT).show();

                    descrip.setText("");
                }
                }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            //String id = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listaincidencias/");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                servicios = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    String id = obj.getString("lastName");
                    String idserv = obj.getString("firstName");

                    servicios[i] = id+"° "+idserv;
                }
            }
            catch(Exception ex)
            {
                resul = false;
                Log.e("ServicioRest","Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(Incidencias.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }

    private String getdatetimesistema(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/HoraFechaSistema/");

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("firstName");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private int getidincidencia(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idincidencias/");

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            idinc = respJSON.getInt("userid")+1;

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return idinc;
    }

    private String insertarincidencia(int idincidencia,String descripcion,String idtipoincidencia,String idservicio,String fechaA,String fechaM){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/insertinc/"+idincidencia+"/zx/"+descripcion+"/cv/1/bn/"+idtipoincidencia+"/ml/"+idservicio+"/kj/"+fechaA+"/ej/"+fechaM);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = "Incidencia registrada exitosamente ! ";

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }


}
