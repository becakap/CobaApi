package coba.api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IQBAL on 6/29/2016.
 */
public class InputData   extends AppCompatActivity {

    String nama, alamat;

    EditText ed_nama, ed_alamat;
    Button btn_submit;

    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    // url to membuat produk baru
    private static String url_tambah_order = "http://testapi.bintangweb.com/create_order.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data);

        ed_nama     = (EditText)findViewById(R.id.ed_nama);
        ed_alamat   = (EditText)findViewById(R.id.ed_alamat);

        btn_submit = (Button)findViewById(R.id.btnSubmit);

        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                nama = ed_nama.getText().toString();
                alamat = ed_alamat.getText().toString();
                // Intent intent = new Intent(EstimasiBerat.this, OrderBerhasil.class);
                //startActivity(intent);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // get komplit pendaftaran dari thread di background
                new CreateNewOrder().execute();
            }
        });



    }





    /**
     * Background Async Task untuk membuat buku tamu baru
     * */
    class CreateNewOrder extends AsyncTask<String, String, String> {

        /**
         * tampilkan progress dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InputData.this);
            pDialog.setMessage("Sedang memproses order...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * buat order baru
         * */
        protected String doInBackground(String... args) {

            // parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("nama",   ""+nama));
            params.add(new BasicNameValuePair("alamat", ""+alamat));

            // json object
            JSONObject json = jsonParser.makeHttpRequest(url_tambah_order,"POST", params);

            // cetak respon di logcat
            Log.d("Create Response", json.toString());

            // cek tag success
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // sukses buat pendaftaran
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    // tutup screen
                    finish();
                } else {
                    // jika gagal
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika proses selesai maka hentikan progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }



}
