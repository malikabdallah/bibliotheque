package com.bookstore.projet.model.donnee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "livre")
@Data
public class Livre {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;




    @Setter @Getter
    private String titre;





    @Column(name = "autheur_id")
    @Getter @Setter
    private int autheursId;



    @Getter @Setter
    private String image;

    @Column(name = "type_id")
    @Getter @Setter
    private int typeId;

    @Getter @Setter
    private int annee;


    @Getter @Setter
    private int disponible;


    public Integer getautheursId() {
        return this.autheursId;
    }

    public Integer getAutheursId() {
        return this.autheursId;
    }

    public Integer gettypeId() {
        return typeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAutheursId(int autheursId) {
        this.autheursId = autheursId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }
}
