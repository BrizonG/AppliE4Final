package com.example.guill.applinaruto;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import bdd.GestionBD;
import metier.Personnage;

/**
 * Created by guill on 20/05/2018.
 */

public class InfosPerso extends AppCompatActivity {

    TextView nom;
    TextView topo;
    TextView manga;
    GestionBD sgbd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infos_perso);
        sgbd = new GestionBD(this);
        String str = (String) getIntent().getSerializableExtra("perso");
        nom= (TextView) findViewById(R.id.nom);
        //On modifie le contenu de l'étiquette avec le texte saisi par l'utilisateur
        //par l''utilisateur
        nom.setText(str);
        sgbd.open();
        String resTopo = sgbd.donneLeTopo(str);
        topo = (TextView) findViewById(R.id.topo);
        topo.setText(resTopo);
        //String resManga = sgbd.donneLeManga(str);
        //manga = (TextView) findViewById(R.id.manga);
        //manga.setText(resManga);
        sgbd.close();

    }

}
