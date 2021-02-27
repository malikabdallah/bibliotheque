package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.repertoire.AdminRepertoire;
import com.bookstore.projet.model.donnee.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/gestion")
public class AdminGestionController {











        @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepertoire adminRepertoire;

    @GetMapping
    public String hello(Model model, @RequestParam(value="page", required = false) Integer p){

        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Admin> listes = adminRepertoire.findAll(pageable);

        model.addAttribute("listes", listes);

        long count = adminRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);



        return "admin/gestion/index";


    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable int id, RedirectAttributes redirectAttributes) {

        adminRepertoire.deleteById(id);



        redirectAttributes.addFlashAttribute("message", "admin supprimer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/gestion";
    }



    @GetMapping("ajouter")
    public String add(Model model,@ModelAttribute Admin admin){
        return "admin/gestion/ajouter";
    }



    @PostMapping("/ajouter")
    public String add(@Valid Admin admin, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/gestion/ajouter";
        }

        redirectAttributes.addFlashAttribute("message", "admin ajouter");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


            Admin adminexiste = this.adminRepertoire.findByNomAndPrenom(admin.getNom(),admin.getPrenom());

        if ( adminexiste != null ) {
            redirectAttributes.addFlashAttribute("message", "l admin existe,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("admin", admin);

        } else {


            admin.setPassword(passwordEncoder.encode(admin.getPassword()));

            adminRepertoire.save(admin);
        }

        return "redirect:/admin/gestion/ajouter";
    }



    @GetMapping("/editer/{id}")
    public String getEditer(@PathVariable int id,Model model){


        Admin admin = adminRepertoire.getOne(id);

        model.addAttribute("admin", admin);

        return "admin/gestion/editer";
    }






    @PostMapping("/editer")
    public String edit(@Valid Admin admin, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Admin adminActuelle = adminRepertoire.getOne(admin.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("adminname", adminActuelle.getNom());
            return "admin/gestion/editer";
        }

        redirectAttributes.addFlashAttribute("message", "admin Editer avec succes");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Admin adminExiste = adminRepertoire.findByNomAndPrenom(admin.getNom(),admin.getPrenom());

        if ( adminExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "l admin existe deja ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        } else {


            adminRepertoire.save(admin);
        }

        return "redirect:/admin/gestion/editer/" + admin.getId();
    }


}
