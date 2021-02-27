package com.bookstore.projet.model.donnee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "autheurs")
@Data
public class Autheurs {



    public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public String getPrenom() {
		return prenom;
	}



	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}



	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Size(min=2, message = "le nom doit avoir au moins deux characteres")
    @Getter
    @Setter
    private String nom;



    @Size(min=2, message = "le nom doit avoir au moins deux characteres")
    @Getter @Setter
    private String prenom;




}
