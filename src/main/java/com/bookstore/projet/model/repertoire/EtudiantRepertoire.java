package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EtudiantRepertoire extends JpaRepository<Etudiant,Integer> {


    Etudiant findByUsername(String username);


    Etudiant findByNomAndPrenom(String nom,String prenom);

    Etudiant findByNomAndPrenomAndIdNot(String nom,String prenom,int id);
}
