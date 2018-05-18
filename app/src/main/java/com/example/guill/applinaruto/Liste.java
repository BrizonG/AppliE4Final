package com.example.guill.applinaruto;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bdd.GestionBD;
import metier.Personnage;

/**
 * Created by guill on 10/12/2017.
 */

public class Liste extends ListActivity implements AdapterView.OnItemClickListener {

    public ArrayList<String> lesValeurs = new ArrayList<String>();
    private ListView listV;
    TraitementJSON_v1 tjson = new TraitementJSON_v1(this);
    GestionBD sgbd;
    Button ajoute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihm_liste);
        tjson.execute("http://profcastillo.free.fr/lmp/personnages.json");
        sgbd = new GestionBD(this);
        init_liste();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesValeurs   );
        setListAdapter(adapter);
        sgbd.close();
        getListView().setOnItemClickListener(this);

        ajoute = (Button) findViewById(R.id.ajouteperso);
        ajoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ajpers = new Intent(getApplicationContext(), AjoutePerso.class);
                startActivity(Ajpers);
            }
        });

    }
    protected void init_liste(){
        sgbd.open();
        lesValeurs.addAll(sgbd.donneLesNoms());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),"ligne "+(position+1),Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"perso :  "+lesValeurs.get(position),Toast.LENGTH_LONG).show();
    }


    public class TraitementJSON extends AsyncTask<String, Void, Boolean> {

        private List<Personnage> lesPersonnages = new ArrayList<Personnage>();
        Context context;
        JSONObject jObj = null;
        URL url;
        HttpURLConnection connexion;


        public TraitementJSON(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            Log.i("doInBack","le dÃ©part : ");
            try {
                url = new URL(urls[0]);
                Log.i("doInBack","le fichier distant : "+urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Boolean result = false;
            try {
                String ficPersonnages;
                //RÃ©cupÃ©ration du contenu du fichier json sur serveur
                ficPersonnages = lectureFichierDistant();
                Log.i("doInBack","le fichier distant : "+ficPersonnages);
                //Transformation du fichier obtenu en objet JSON
                JSONObject jsonPersonnages = parsePersonnages(ficPersonnages);
                //Traitement de l'objet JSON pour obtenir des instances de Personnages
                Log.i("doInBack","le fichier json : "+jsonPersonnages.toString());
                recPersonnages(jsonPersonnages);
                Log.i("doInback","nombre de persos : "+lesPersonnages.size());
                int num = 1;
                StringBuilder message = new StringBuilder("les persos à la fin : ");
                for (Personnage perso : lesPersonnages) {
                    message.append(perso.getNom());
                    message.append("   :   ");
                    message.append(perso.getTopo());
                    message.append("\n");
                    num++;
                }
                Log.i("doInback 1","message 1: "+message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void recPersonnages(JSONObject jsonPersonnages) {

            JSONArray lesPersos = null;
            try {

                lesPersos = jsonPersonnages.getJSONArray("lespersonnages");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("recper","erreurJSArray");;
            }

            for(int i = 0; i < lesPersos.length(); i++){
                JSONObject nuplet = null;
                String leNom, leTopo;
                Long id;
                Personnage perso;
                try {
                    nuplet = lesPersos.getJSONObject(i);
                    leNom = nuplet.getString("nom");
                    lesValeurs.add(leNom);
                    leTopo = nuplet.getString("topo");
                    perso = new Personnage(leNom,leTopo);
                    lesPersonnages.add(perso);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private JSONObject parsePersonnages(String textPersonnages) {
            if (textPersonnages != null) {
                try {
                    jObj = new JSONObject(textPersonnages);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("parsePer","erreurJSObj");;
                }
                return jObj;
            }
            return null;
        }

        private String lectureFichierDistant() {
            StringBuilder builder = new StringBuilder();

            // adresse du serveur ( modification pour passage en production
            try {
                connexion = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line;
            BufferedReader br= null;
            try {
                br = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                while ((line=br.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        public List<Personnage> getLesPersonnages() {
            return lesPersonnages;
        }

        public ArrayList<String> getLesNoms(){
            ArrayList<String> liste = null;
            for (Personnage perso : lesPersonnages)
                liste.add(perso.getNom());
            return liste;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.i("doInback 2","message 2: "+lesValeurs);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, lesValeurs);
            setListAdapter(adapter2);
        }
    }


}
