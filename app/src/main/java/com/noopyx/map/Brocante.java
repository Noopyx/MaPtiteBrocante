
package com.noopyx.map;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Quentin on 29/03/2016.
 */
public class Brocante {

    private int codePostale;
    private GregorianCalendar date;
    private String departement;
    private String emailOrg;
    private String heureFin;
    private String libelle;
    private String nomOrg;
    private String pays;
    private String rue;
    private String salle;
    private String telOrg;
    private String ville;
    private boolean handicap;
    private double prix;

    public Brocante (String libelle, GregorianCalendar date, String ville) {
        this.libelle = libelle;
        this.date = date;
        this.ville = ville;
    }
    public String getEmailOrg() {
        return emailOrg;
    }

    public void setEmailOrg(String emailOrg) {
        this.emailOrg = emailOrg;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        if(heureFin.length() <= 2)
            this.heureFin = heureFin+"h00";
        else if(heureFin.length() == 3 )
            this.heureFin = heureFin.substring(0,2)+"h00";
        else
            this.heureFin = heureFin.substring(0,2)+"h"+heureFin.substring(3);
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getTelOrg() {
        return telOrg;
    }

    public void setTelOrg(String telOrg) {
        this.telOrg = telOrg;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isHandicap() {
        return handicap;
    }

    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(int codePostale) {
        this.codePostale = codePostale;
    }

    public GregorianCalendar getGregorian () {
        return date;
    }
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
        SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
        sdf.setCalendar(date);
        sdf2.setCalendar(date);
        sdf3.setCalendar(date);
        return sdf.format(date.getTime())+"\n"+sdf2.format(date.getTime())+"h"+sdf3.format(date.getTime())+" - "+heureFin;
        //return date.get(Calendar.DATE)+"\\"+date.get(Calendar.MONTH)+"\\"+date.get(Calendar.YEAR)+"\n   "+date.get(Calendar.HOUR)+"h"+date.get(Calendar.MINUTE)+" - "+heureFin;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String[] getArrayString() {
        return new String[]{ville,libelle,getDate()};
    }
}
