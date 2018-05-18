package bdd;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import java.util.ArrayList;
        import metier.Personnage;

public class GestionBD {
    private SQLiteDatabase maBase;
    private BDHelper monBDHelper;

    public GestionBD(Context context) {
        monBDHelper = new BDHelper(context, "baseperso", null, 1);
    }
    public void open(){
        maBase = monBDHelper.getWritableDatabase();
    }

    public void close(){
        maBase.close();
    }

    public long ajoutePerso(Personnage perso){
        ContentValues v = new ContentValues();
        v.put("nom", perso.getNom());
        v.put("topo", perso.getTopo());
        return maBase.insert("personnages", null, v);
    }

    public void supprimepersos(){
        maBase.delete("personnages", null, null);
    }

    public ArrayList<String> donneLesNoms(){
        ArrayList<String> liste = new ArrayList<String>();
        Cursor c = maBase.rawQuery("select nom from personnages order by nom",null);
        while (c.moveToNext())
            liste.add( c.getString(0));
        if(liste==null){
            liste.add("erreur de bdd!");
        }
        return liste;
    }

}
