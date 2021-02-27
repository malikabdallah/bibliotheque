package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Emprunts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpruntsRepertoire extends JpaRepository<Emprunts,Integer> {

    List<Emprunts> findAllByetudiantid(int id);
;

    List<Emprunts> findAllBylivreid(int id);

    Page<Emprunts> findAllBylivreid(int id,Pageable pageable);


    Page<Emprunts> findByetudiantid(int id, Pageable pageable);



    @Query(
            value = "SELECT * FROM emprunts u WHERE u.retard = 1",
            nativeQuery = true)
    List<Emprunts> trouvertouslesretards();


}
