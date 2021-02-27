package com.bookstore.projet;


import com.bookstore.projet.model.donnee.Type;
import com.bookstore.projet.model.repertoire.TypeRepertoire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class DonneePartage {




    @Autowired
    private TypeRepertoire typeRepertoire;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal) {

        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }


        List<Type>list=typeRepertoire.findAll();
        model.addAttribute("listeType",list);




    }
}
