package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivreRepertoire extends JpaRepository<Livre,Integer> {

    Livre findByTitreAndIdNot(String titre,int id);


    @Query(value="SELECT * FROM livre ORDER BY RAND() LIMIT 16", nativeQuery = true)
    List<Livre> findLivre();




    List<Livre> findBytypeId(int id);

    List<Livre> findByautheursId(int id);

    Page<Livre> findByautheursId(int id,Pageable pageable);

    List<Livre> findByautheursId(Pageable pageable);

    List<Livre> findBytypeId(int id, Pageable pageable);
}
