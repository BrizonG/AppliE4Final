package com.example.guill.applinaruto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import bdd.GestionBD;

/**
 * Created by guill on 20/05/2018.
 */

public class InfosPerso extends AppCompatActivity {

    TextView nom;
    TextView topo;
    Button supp;
    GestionBD sgbd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infos_perso);
        sgbd = new GestionBD(this);
        //On recupere le nom de perso
        final String str = (String) getIntent().getSerializableExtra("perso");
        nom= (TextView) findViewById(R.id.nom);
        nom.setText(str);
        sgbd.open();
        //On recupere le topo au nom correspondant
        String resTopo = sgbd.donneLeTopo(str);
        topo = (TextView) findViewById(R.id.topo);
        topo.setText(resTopo);
        sgbd.close();

        //Action pour supprimer le personnage quand on clique sur le bouton
        supp = (Button) findViewById(R.id.deleteperso);
        supp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sgbd.open();
                sgbd.supprimeperso(str);
                sgbd.close();
                Toast.makeText(getApplicationContext(),"Vous avez supprimé ce personnage",Toast.LENGTH_LONG).show();
            }
        });
    }

}
