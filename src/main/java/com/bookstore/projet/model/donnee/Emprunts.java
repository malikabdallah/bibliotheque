package com.bookstore.projet.model.donnee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "emprunts")
@Data
public class Emprunts {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;





    @Column(name = "etudiant_id")
    @Getter @Setter
    private int etudiantid;



    @Getter @Setter
    private int retard;

    @Column(name = "livre_id")
    @Getter @Setter
    private int livreid;


    @Getter @Setter
    private String dateemprunt;

    @Getter @Setter
    private String dateretour;


    @Getter @Setter
    private String dateretoureffective;


    @Getter @Setter
    private int renouvellement;


    public String getDateretoureffective() {
        return dateretoureffective;
    }

    public void setDateretoureffective(String dateretoureffective) {
        this.dateretoureffective = dateretoureffective;
    }

    public String getDateretour() {
        return dateretour;
    }

    public void setDateretour(String dateretour) {
        this.dateretour = dateretour;
    }

    public String getDateemprunt() {
        return dateemprunt;
    }

    public void setDateemprunt(String dateemprunt) {
        this.dateemprunt = dateemprunt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantid() {
        return etudiantid;
    }

    public void setEtudiantid(int etudiantid) {
        this.etudiantid = etudiantid;
    }

    public int isRetard() {
        return retard;
    }

    public void setRetard(int retard) {
        this.retard = retard;
    }

    public int getLivreid() {
        return livreid;
    }

    public void setLivreid(int livreid) {
        this.livreid = livreid;
    }

    public int getRenouvellement() {
        return renouvellement;
    }

    public void setRenouvellement(int renouvellement) {
        this.renouvellement = renouvellement;
    }


}
