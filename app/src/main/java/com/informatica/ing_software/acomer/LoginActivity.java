package com.informatica.ing_software.acomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.informatica.ing_software.acomer.json.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    // url to get all products list
    private static String usuarios_login = "http://192.168.0.14/proyecto/p0_usuarios_login.php";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Procedemos con el login al pulsar le boton iniciar sesion
    public void loguearse(View view) {
        EditText eUsuario, ePassword;
        eUsuario = (EditText) findViewById(R.id.editTextUsername);
        ePassword = (EditText) findViewById(R.id.editTextPassword);

        String usuario = eUsuario.getText().toString().toLowerCase().trim();
        String password = ePassword.getText().toString().trim();

        if (usuario.equals("")) {
            // Si el campo usuario esta vacio, error
            eUsuario.setHint("Introduzca nombre de usuario");
        } else if (password.equals("")) {
            // Si el campo contraseña esta vacio, error
            ePassword.setHint("Introduzca contraseña");
        } else {
            // Si ambos campos han sido rellenados comprobamos el login
            new IniciarSesion().execute(usuario, password);
        }
    }

    // Abrimos la activity de registro al pulsar el boton registrarse
    public void registrarse(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Background Async Task to try login by making HTTP Request
     */
    class IniciarSesion extends AsyncTask<String, String, String> {
        String usuario_email;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Iniciando sesión...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
            params.add(new Pair<String, String>("email", args[0]));
            params.add(new Pair<String, String>("password", args[1]));
            params.add(new Pair<String, String>("tipo_usuario", "C"));

            int success = 0;
            usuario_email = args[0];


            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(usuarios_login, params);

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                return String.valueOf(success);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String result) {
            // dismiss the dialog after login
            pDialog.dismiss();

            if (result.equals("1")) {
                // navigate to Main Menu
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("usuario_email", usuario_email);
                startActivity(i);
            } else {
                TextView textViewErrorLogin = (TextView) findViewById(R.id.textViewErrorLogin);
                textViewErrorLogin.setVisibility(TextView.VISIBLE);
            }
        }
    }
}