package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.repertoire.AutheurRepertoire;
import com.bookstore.projet.model.donnee.Autheurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/autheur")
public class AdminAutheurController {



    @Autowired
    private AutheurRepertoire autheurRepertoire;

    @GetMapping
    public String hello(Model modem, @RequestParam(value="page", required = false) Integer p){



        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Autheurs> products = autheurRepertoire.findAll(pageable);

        modem.addAttribute("autheurs", products);

        long count = autheurRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        modem.addAttribute("pageCount", (int)pageCount);
        modem.addAttribute("perPage", perPage);
        modem.addAttribute("count", count);
        modem.addAttribute("page", page);



        return "admin/autheur/index";


    }


    @GetMapping("/ajouter")
    public String add( Model model) {

        model.addAttribute("autheur", new Autheurs());
        return "admin/autheur/ajouter";
    }







    @PostMapping("/editer")
    public String edit(@Valid Autheurs autheur, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Autheurs autheursactuelle = autheurRepertoire.getOne(autheur.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", autheursactuelle.getNom());
            return "admin/autheur/edit";
        }

        redirectAttributes.addFlashAttribute("message", "autheur Editer avec succes");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Autheurs autheursExistes = autheurRepertoire.findByNomAndPrenom(autheur.getNom(),autheur.getPrenom());

        if ( autheursExistes != null ) {
            redirectAttributes.addFlashAttribute("message", "l autheurs existe deja ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        } else {


            autheurRepertoire.save(autheur);
        }

        return "redirect:/admin/autheur/editer/" + autheur.getId();
    }


    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable int id, RedirectAttributes redirectAttributes) {

        autheurRepertoire.deleteById(id);



        redirectAttributes.addFlashAttribute("message", "autheur supprimer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/autheur";
    }



    @GetMapping("/editer/{id}")
    public String getEditer(@PathVariable int id,Model model){


        Autheurs autheur = autheurRepertoire.getOne(id);

        model.addAttribute("autheur", autheur);

        return "admin/autheur/editer";
    }



    @PostMapping("/ajouter")
    public String add(@Valid Autheurs autheur, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/autheur/ajouter";
        }

        redirectAttributes.addFlashAttribute("message", "autheur ajouter");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Autheurs autheursExistes = this.autheurRepertoire.findByNomAndPrenom(autheur.getNom(),autheur.getPrenom());

        if ( autheursExistes != null ) {
            redirectAttributes.addFlashAttribute("message", "l autheur existe,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("autheur", autheur);

        } else {

            autheurRepertoire.save(autheur);
        }

        return "redirect:/admin/autheur/ajouter";
    }




}
