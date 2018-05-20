package com.example.guill.applinaruto;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bdd.GestionBD;
import metier.Personnage;

/**
 * Created by guill on 18/05/2018.
 */

public class AjoutePerso extends AppCompatActivity {

    Button creer;
    EditText nomperso;
    EditText topoperso;
    //EditText mangaperso;
    Personnage ajou_pers;
    GestionBD sgbd;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajoute_perso);

        creer = (Button) findViewById(R.id.creerperso);
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sgbd = new GestionBD(context);
                nomperso = (EditText) findViewById(R.id.nomperso);
                topoperso = (EditText) findViewById(R.id.topoperso);
                //mangaperso = (EditText) findViewById(R.id.mangaperso);
                String n_perso = nomperso.getText().toString();
                String t_perso = topoperso.getText().toString();
                //String m_perso = mangaperso.getText().toString();
                ajou_pers = new Personnage(n_perso, t_perso);
                sgbd.open();
                sgbd.ajoutePerso(ajou_pers);
                Toast.makeText(getApplicationContext(),"Vous avez ajouté :  "+ n_perso,Toast.LENGTH_LONG).show();
                sgbd.close();
            }
        });
    }

}
