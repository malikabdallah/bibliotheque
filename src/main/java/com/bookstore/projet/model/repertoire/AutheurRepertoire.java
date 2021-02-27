package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Autheurs;
import com.bookstore.projet.model.donnee.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutheurRepertoire extends JpaRepository<Autheurs,Integer> {

    Page<Autheurs> findAll(Pageable pagable);

    Autheurs findByNomAndPrenom(String nom,String prenom);

}
