package souza.hm.com.consultarcep;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //


        //
        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchCEP = (EditText) findViewById(R.id.search_cep);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchCEP.getText())) {
                    Toast.makeText(getApplicationContext(), R.string.msgVazio, Toast.LENGTH_SHORT).show();
                } else {
                    BuscarCEP(searchCEP.getText().toString());
                    clearInfo();
                }
            }
        });

        //
    }
    void BuscarCEP(String profile)
    {
//
        String requestUrl = "http://correiosapi.apphb.com/cep/" + profile;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getProfileInfo", "FAIL");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final TextView Urua = (TextView) findViewById(R.id.RuaID);
                final TextView Ubairro = (TextView) findViewById(R.id.BairroID);
                final TextView Ucep = (TextView) findViewById(R.id.CepID);
                final TextView Ucidade = (TextView) findViewById(R.id.CidadeID);
                final TextView Uestado = (TextView) findViewById(R.id.estadoID);

                final String jsonData = response.body().string();
                Log.i("getProfileInfo", jsonData);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CardView userCard = (CardView) findViewById(R.id.card_user_info);
                            try {
                                JSONObject rootObj = new JSONObject(jsonData);

                                if (rootObj != null) {

                                    Urua.setText("Rua:" + rootObj.getString("logradouro"));
                                    Ubairro.setText("Bairro:" + rootObj.getString("bairro"));
                                    Ucep.setText("Cep:" + rootObj.getString("cep"));
                                    Ucidade.setText("Cidade:" + rootObj.getString("cidade"));
                                    Uestado.setText("Estado:" + rootObj.getString("estado"));

                                    userCard.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                userCard.setVisibility(View.GONE);

                            }
                        }
                    });
                }
            }
        });
        //
    }

//
private void clearInfo() {
    EditText searchCEP = (EditText) findViewById(R.id.search_cep);
    RelativeLayout  bdy = (RelativeLayout) findViewById(R.id.BodyID);
    searchCEP.setText("");
    bdy.setBackgroundColor(Color.BLUE);
}
    //
}
