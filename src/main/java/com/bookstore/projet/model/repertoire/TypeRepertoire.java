package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepertoire extends JpaRepository <Type,Integer>  {


    Type findByNom(String nom);

    Page<Type> findAll(Pageable pagable);



    Type findByNomAndIdNot(String slug, int id);

}
