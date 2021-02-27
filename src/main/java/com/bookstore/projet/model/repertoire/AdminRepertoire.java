package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Admin;
import com.bookstore.projet.model.donnee.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepertoire  extends JpaRepository<Admin,Integer> {
  Admin   findByNomAndPrenom(String nom,String prenom);

  Admin findByUsername(String username);

}
