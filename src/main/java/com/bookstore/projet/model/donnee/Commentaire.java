package com.bookstore.projet.model.donnee;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "commentaire")
@Data
public class Commentaire {




    public int getIdetudiant() {
		return idetudiant;
	}

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;





    @Getter @Setter
    private int idetudiant;




    @Getter @Setter
    private int note;


    @Getter @Setter
    private int idlivre;



    @Getter @Setter
    private String poste;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdetudianttudiant() {
        return idetudiant;
    }

    public void setIdetudiant(int idEtudiant) {
        this.idetudiant = idEtudiant;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getIdlivre() {
        return idlivre;
    }

    public void setIdlivre(int idLivre) {
        this.idlivre = idLivre;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }
}
