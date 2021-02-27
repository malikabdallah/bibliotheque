package com.bookstore.projet.model.donnee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "type")
@Data
public class Type {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @Size(min=2, message = "le nom doit avoir au moins deux characteres")
    @Getter
    @Setter
    private String nom;


    @Getter @Setter
    private String image;


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


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}














}
