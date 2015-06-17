package com.urp.translivik;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import java.lang.reflect.Type;

public class MainActivity extends Activity {



    private Button btningresar;
    private EditText usuario,password;
    private String id1,id2,get;
    Bundle b;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        usuario=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpassword);
        btningresar=(Button)findViewById(R.id.btningresar);
        btningresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                id1 = getusuarioid(usuario.getText().toString());
                id2 = getpasswordid(password.getText().toString());
                if (usuario.getText().toString().equals("")||password.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Complete los campos", Toast.LENGTH_SHORT).show();

                }else if(id1.equals(id2) && Integer.parseInt(id1)>0 && Integer.parseInt(id2)>0) {
                    Toast.makeText(getApplicationContext(), "Iniciando sesion...", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(MainActivity.this,Consultar_servicios.class);
                    b=new Bundle();
                    b.putString("idlogin",id1);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }

                else{
                    usuario.setText("");
                    password.setText("");
                    Toast.makeText(getApplicationContext(), "Usuario o password incorrecto", Toast.LENGTH_SHORT).show();

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


    private String getusuarioid(String variable){

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del =
				new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidloginusuario/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("userid");

        }
		catch(Exception ex)
		{
            Log.e("ServicioRest","Error!", ex);
		}
		return get;
	}

    private String getpasswordid(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet("ipconfig/SGSTTSERVICES/service/user/getidloginpassword/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("userid");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

}
