package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.donnee.Admin;
import com.bookstore.projet.model.repertoire.AdminRepertoire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/profile")
public class AdminExtra {

    @Autowired
    private AdminRepertoire adminRepertoire;


    @GetMapping("/{id}")
    public String profile(@PathVariable int id, Model model) {
        Admin admin=adminRepertoire.getOne(id);
        model.addAttribute("admin",admin);
        return "admin/gestion/profile";



    }
}
