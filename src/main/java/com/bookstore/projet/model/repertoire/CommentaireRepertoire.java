package com.bookstore.projet.model.repertoire;

import com.bookstore.projet.model.donnee.Commentaire;
import com.bookstore.projet.model.donnee.Emprunts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepertoire extends JpaRepository<Commentaire,Integer> {






    List<Commentaire> findAllByidlivre(int id);
}
