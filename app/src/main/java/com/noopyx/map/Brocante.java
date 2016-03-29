
package com.noopyx.map;

import java.util.Date;

/**
 * Created by Quentin on 29/03/2016.
 */
public class Brocante {

    private int codePostale;
    private Date date;
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

    public Brocante (String libelle, Date date, String ville) {
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
        this.heureFin = heureFin;
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

    public String getDate() {
        return date.getDay()+"/"+date.getMonth()+"/"+date.getYear()+"\n   "+date.getHours()+":"+date.getMinutes()+" - "+heureFin;
    }

    public void setDate(Date date) {
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
