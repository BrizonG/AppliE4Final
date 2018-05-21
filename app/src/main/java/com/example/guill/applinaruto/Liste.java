package com.example.guill.applinaruto;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.ArrayList;

import bdd.GestionBD;

/**
 * Created by guill on 10/12/2017.
 */

public class Liste extends ListActivity implements AdapterView.OnItemClickListener {

    public ArrayList<String> lesValeurs = new ArrayList<String>();
    TraitementJSON_v1 tjson = new TraitementJSON_v1(this);
    GestionBD sgbd;
    Button ajoute;
    Button update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihm_liste);
        tjson.execute("http://profcastillo.free.fr/lmp/personnages.json");
        sgbd = new GestionBD(this);
        init_liste();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesValeurs   );
        setListAdapter(adapter);
        sgbd.close();
        getListView().setOnItemClickListener(this);

        //Action pour ouvrir la view d'ajout de perso
        ajoute = (Button) findViewById(R.id.ajouteperso);
        ajoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ajpers = new Intent(getApplicationContext(), AjoutePerso.class);
                startActivity(Ajpers);
            }
        });

        //Action pour rafraîchir la liste quand on clique sur le bouton
        update = (Button) findViewById(R.id.updatelistview);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lesValeurs.clear();
                init_liste();
                setListAdapter(adapter);
            }
        });
    }
    protected void init_liste(){
        sgbd.open();
        lesValeurs.addAll(sgbd.donneLesNoms());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Action pour ouvrir la view des details d'un perso
        Intent lesInfos = new Intent(getApplicationContext(), InfosPerso.class);
        //Passe le parametre dans l'intent
        lesInfos.putExtra("perso", lesValeurs.get(position));
        startActivity(lesInfos);
    }
}
