package coba.api;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IQBAL on 6/29/2016.
 */
public class Lokasi  extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;


    // buat json object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> lokasiList;
    // url untuk get semua buku tamu
    private static String url_semua_lokasi = "http://testapi.bintangweb.com/lokasi.php";


    // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LOKASI = "lokasi";
    private static final String TAG_LATI = "lati";
    private static final String TAG_LOGI = "logi";
    private static final String TAG_NAMA = "nama";


    // lokasi JSONArray
    JSONArray lokasi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lokasi);
        // Hashmap untuk ListView
        lokasiList = new ArrayList<HashMap<String, String>>();
        ListView lv = (ListView) findViewById(R.id.lv_lokasi);
        // Loading products in Background Thread
        new LoadSemuaLokasi().execute();
    }





    /**
     * Background Async Task untuk menampilkan semua daftar bukutamu menggunakan http request
     */
    class LoadSemuaLokasi extends AsyncTask<String, String, String> {



        /**
         * sebelum melakukan thread di background maka jalankan progres dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Lokasi.this);
            pDialog.setMessage("Mohon tunggu, Loading Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * dapetkan semua produk dari get url di background
         */
        protected String doInBackground(String... args) {



            // Buat Parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // ambil json dari url
            JSONObject json = jParser.makeHttpRequest(url_semua_lokasi, "GET", params);


            // cek logcat untuk response dari json
            Log.d("Semua Lokasi: ", json.toString());

            try {
                // cek jika tag success
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // data ditemukan
                    // ambil array dari bukutamu
                    lokasi = json.getJSONArray(TAG_LOKASI);

                    // tampilkan perulangan semua produk
                    for (int i = 0; i < lokasi.length(); i++) {
                        JSONObject c = lokasi.getJSONObject(i);

                        // simpan pada variabel
                        String lati = c.getString(TAG_LATI);
                        String logi = c.getString(TAG_LOGI);
                        String nama = c.getString(TAG_NAMA);

                        // buat new hashmap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // key => value
                        map.put(TAG_LATI, lati);
                        map.put(TAG_LOGI, logi);
                        map.put(TAG_NAMA, nama);

                        // masukan HashList ke ArrayList
                        lokasiList.add(map);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lokasi Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika pekerjaan di belakang layar selesai maka hentikan progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // hentikan progress ketika semua data didapat
            pDialog.dismiss();

            // perbarui screen
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * perbarui json ke arraylist
                     * */
                    ListView lvItem;
                    lvItem = (ListView) findViewById(R.id.lv_lokasi);
                    ListAdapter adapter = new SimpleAdapter(
                            Lokasi.this, lokasiList,
                            R.layout.list_lokasi, new String[]{TAG_LATI,
                            TAG_LOGI, TAG_NAMA},
                            new int[]{R.id.lati, R.id.logi, R.id.nama});
                    // perbarui list lokasi
                    lvItem.setAdapter(null);
                    lvItem.setAdapter(adapter);

                }
            });

        }

    }
}
