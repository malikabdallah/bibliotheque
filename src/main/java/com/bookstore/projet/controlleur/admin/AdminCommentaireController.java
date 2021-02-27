package com.bookstore.projet.controlleur.admin;


import com.bookstore.projet.model.donnee.*;
import com.bookstore.projet.model.repertoire.CommentaireRepertoire;
import com.bookstore.projet.model.repertoire.EtudiantRepertoire;
import com.bookstore.projet.model.repertoire.LivreRepertoire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/commentaire")
public class AdminCommentaireController {




    @Autowired
    private CommentaireRepertoire commentaireRepertoire;


    @Autowired
    private LivreRepertoire livreRepertoire;



    @Autowired
    private EtudiantRepertoire etudiantRepertoire;


    @GetMapping()
    public String hello(Model model, @RequestParam(value = "page", required = false) Integer p){






        int perPage = 10;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Commentaire> commentaires = commentaireRepertoire.findAll(pageable);

        model.addAttribute("commentaires", commentaires);



        List<Etudiant> etudiants = etudiantRepertoire.findAll();

        HashMap<Integer, String> auth = new HashMap<>();
        for (Etudiant etudiant : etudiants) {
            auth.put(etudiant.getId(), etudiant.getUsername());
        }

        model.addAttribute("etudiants",auth);


        List<Livre> livres = livreRepertoire.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Livre livre : livres) {
            cats.put(livre.getId(), livre.getTitre());
        }

        model.addAttribute("livres",cats);

        long count = commentaireRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        System.out.println("page "+page);
        return "admin/commentaire/index";
    }





}
