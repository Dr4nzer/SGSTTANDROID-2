package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Consultar_servicios extends Activity {
    private Spinner spinner;
    private TextView bienvenida,servicio,ruta,datetime,trasladista,cliente,pax,set;
    private String get,objetoseleccionado,act2;
    private Button btnincidencias,btnfinalizarservicio,btntrazaruta;
    private Bundle b;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_servicio);
        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent= getIntent();
        Bundle bundle = this.getIntent().getExtras();
        String idchofer=bundle.getString("idlogin");

        set=(TextView)findViewById(R.id.set1);
        spinner= (Spinner)findViewById(R.id.spinnerd);
        bienvenida=(TextView)findViewById(R.id.txtbienvenida);
        servicio=(TextView)findViewById(R.id.txtservicio);
        ruta=(TextView)findViewById(R.id.txtruta);
        datetime=(TextView)findViewById(R.id.txtdatetime);
        trasladista=(TextView)findViewById(R.id.txttrasladista);
        cliente=(TextView)findViewById(R.id.txtcliente);
        pax=(TextView)findViewById(R.id.txtpax);
        btnincidencias=(Button)findViewById(R.id.btnincidencias);
        btnfinalizarservicio=(Button)findViewById(R.id.btnfinalizar);
        btntrazaruta=(Button)findViewById(R.id.btntrazar);
        btnincidencias.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {


             Integer mesA = Integer.parseInt(datetime.getText().toString().substring(5, 7));
             Integer mesS = Integer.parseInt(getdatetimesistema().substring(5, 7));
             Integer diaA = Integer.parseInt(datetime.getText().toString().substring(8, 10));
             Integer diaS = Integer.parseInt(getdatetimesistema().substring(8, 10));
             Integer anoA = Integer.parseInt(datetime.getText().toString().substring(2, 4));
             Integer anoS = Integer.parseInt(getdatetimesistema().substring(2, 4));
             Integer horaA = Integer.parseInt(getdatetimesistema().substring(11, 13));
             Integer horaS = Integer.parseInt(datetime.getText().toString().substring(11, 13));
             Integer minA = Integer.parseInt(getdatetimesistema().substring(14, 16));
             Integer minS = Integer.parseInt(datetime.getText().toString().substring(14, 16));

             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Consultar_servicios.this);

             alertDialogBuilder.setTitle("Advertencia");
             alertDialogBuilder.setMessage("No puede registrar incidencias ");


             alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                 public void onClick(DialogInterface dialog, int id) {
                     dialog.cancel();
                 }

             });



             if (diaA == diaS && mesA==mesS && anoA==anoS ) {
                 act2 = set.getText().toString();
                 Intent c = new Intent(Consultar_servicios.this, Incidencias.class);
                 b = new Bundle();
                 b.putString("idservicio", act2);
                 c.putExtras(b);
                 startActivity(c);
                 finish();

             }else{
                 AlertDialog alertDialog = alertDialogBuilder.create();

                 alertDialog.show();



             }















         }
         }
    );
        btnfinalizarservicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Integer mesA = Integer.parseInt(datetime.getText().toString().substring(5, 7));
                    Integer mesS = Integer.parseInt(getdatetimesistema().substring(5, 7));
                    Integer diaA = Integer.parseInt(datetime.getText().toString().substring(8, 10));
                    Integer diaS = Integer.parseInt(getdatetimesistema().substring(8, 10));
                    Integer anoA = Integer.parseInt(datetime.getText().toString().substring(2, 4));
                    Integer anoS = Integer.parseInt(getdatetimesistema().substring(2, 4));
                    Integer horaA = Integer.parseInt(getdatetimesistema().substring(11, 13));
                    Integer horaS = Integer.parseInt(datetime.getText().toString().substring(11, 13));
                    Integer minA = Integer.parseInt(getdatetimesistema().substring(14, 16));
                    Integer minS = Integer.parseInt(datetime.getText().toString().substring(14, 16));

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Consultar_servicios.this);

                    alertDialogBuilder.setTitle("Advertencia");
                    alertDialogBuilder.setMessage("¿ Esta seguro de finalizar el servicio ?");


                    alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), finalizarservicio(set.getText().toString()), Toast.LENGTH_SHORT).show();
                            btnfinalizarservicio.setEnabled(false);
                            btnincidencias.setEnabled(true);
                        }

                    });


                    alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {


                            dialog.cancel();

                        }

                    });

                    AlertDialog.Builder alertDialogBuildere = new AlertDialog.Builder(Consultar_servicios.this);

                    alertDialogBuildere.setTitle("Advertencia");
                    alertDialogBuildere.setMessage("Todavia no puede finalizar el servicio");

                    alertDialogBuildere.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }

                    });
                    if (diaA == diaS && mesA==mesS && anoA==anoS && horaA==horaS && minA>minS ||diaA == diaS && mesA==mesS && anoA==anoS && horaA==horaS+1 && minA<minS ) {
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.show();



                    }else{
                        AlertDialog alertDialog = alertDialogBuildere.create();

                        alertDialog.show();



                    }
                }

                        }
                        );

        btntrazaruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*String directionweburl = "http://maps.google.com/maps?daddr=-12.075581404063755,-77.05321311950684&saddr=-12.063998611017295,-77.09578514099121";

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directionweburl));
                myIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(myIntent);*/

                String idservicio=getidservicio(objetoseleccionado);
                String cadena=getlatitudlongitud(idservicio);
                String directionweburl = "http://maps.google.com/maps?daddr="+cadena;

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directionweburl));
                myIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(myIntent);

            }
        });




        getNombrebienvenida tarea=new getNombrebienvenida();
        tarea.execute(idchofer);

        TareaWSListar obj=new TareaWSListar();
        obj.execute(idchofer);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        Toast.makeText(getApplicationContext(),"Seleccionado: " +
                                parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        objetoseleccionado=parent.getItemAtPosition(position).toString();
                        set.setText(objetoseleccionado);
                        String idservicio=getidservicio(objetoseleccionado);
                        getDescripcionservicio a= new getDescripcionservicio();
                        a.execute(getidtiposervicio(idservicio));
                        getRuta b=new getRuta();
                        b.execute(idservicio);
                        getFecha c=new getFecha();
                        c.execute(objetoseleccionado);
                        getNombreTrasladista d=new getNombreTrasladista();
                        d.execute(getidtrasladista(objetoseleccionado));
                        getNombrecliente e=new getNombrecliente();
                        e.execute(getidcliente(objetoseleccionado));
                        getPax f= new getPax();
                        f.execute(getidfile(objetoseleccionado));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        servicio.setText("");
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
    private class getNombrebienvenida extends AsyncTask<String,Integer,Boolean> {


        private String nombre;
        private String apellido;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido=respJSON.getString("lastName");


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                bienvenida.setText("Bienvenido " + nombre + " " + apellido);
            }
        }
    }

    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String id = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/lista/"+id);

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

                    String idserv = obj.getString("firstName");

                    servicios[i] = idserv;
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
                        new ArrayAdapter<String>(Consultar_servicios.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }

    private class getFecha extends AsyncTask<String,Integer,Boolean> {


        private String fecha;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/HoraFecha/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                fecha = respJSON.getString("firstName");



            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                datetime.setText(fecha);
            }
        }
    }

    private String getidservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idservicio/"+variable);

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

    private class getRuta extends AsyncTask<String,Integer,Boolean> {


        private String direcc;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/OrigenDestino/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                direcc = respJSON.getString("firstName");



            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                ruta.setText(direcc);
            }
        }
    }

    private String getidtiposervicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/IdDescripcionservicio/"+variable);

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

    private class getDescripcionservicio extends AsyncTask<String,Integer,Boolean> {


        private String descripcion;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/Descripcionservicio/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");



            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                servicio.setText(descripcion);
            }
        }
    }

    private String getidtrasladista(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idtrasl/"+variable);

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

    private class getNombreTrasladista extends AsyncTask<String,Integer,Boolean> {


        private String nombre,apellido;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/nombretrasl/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido = respJSON.getString("lastName");


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
               trasladista.setText(nombre+" "+apellido);
            }
        }
    }

    private String getidfile(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idPax/"+variable);

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

    private class getPax extends AsyncTask<String,Integer,Boolean> {


        private String Nombrepax;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/Nombrepax/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                Nombrepax = respJSON.getString("firstName");


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                pax.setText(Nombrepax);
            }
        }
    }

    private String getidcliente(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getIdCliente/"+variable);

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

    private class getNombrecliente extends AsyncTask<String,Integer,Boolean> {


        private String Nombrecliente;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getNombreCliente/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                Nombrecliente = respJSON.getString("firstName");


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                cliente.setText(Nombrecliente);
            }
        }
    }

    private String finalizarservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/cambiarestadoservicio/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = "Servicio finalizado";

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
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

    private String getlatitudlongitud(String id){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getOrigendestino/"+id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String destino = respJSON.getString("firstName");
            String origen=respJSON.getString("lastName");
            get=origen+"&saddr="+destino;
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

}
