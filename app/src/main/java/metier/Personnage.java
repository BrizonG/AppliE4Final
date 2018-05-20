package metier;

/**
 * Created by guill on 13/12/2017.
 */

public class Personnage {
    String nom;
    String topo;

    public Personnage(String nom, String topo) {
        this.nom = nom;
        this.topo = topo;
    }

    public String getNom() {
        return nom;
    }

    public String getTopo() {
        return topo;
    }

}