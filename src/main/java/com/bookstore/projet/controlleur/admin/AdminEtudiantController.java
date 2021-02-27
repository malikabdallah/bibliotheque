package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.repertoire.EmpruntsRepertoire;
import com.bookstore.projet.model.repertoire.EtudiantRepertoire;
import com.bookstore.projet.model.repertoire.LivreRepertoire;
import com.bookstore.projet.model.donnee.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("admin/etudiant")
public class AdminEtudiantController {



    @Autowired
    private EtudiantRepertoire etudiantRepertoire;

    @GetMapping
    public String hello(Model model, @RequestParam(value="page", required = false) Integer p){

        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Etudiant> listes = etudiantRepertoire.findAll(pageable);

        model.addAttribute("listes", listes);

        long count = etudiantRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);



        return "admin/etudiant/index";


    }

    @GetMapping("/ajouter")
    public String ajouter(Model model,Etudiant etudiant){

        return "admin/etudiant/ajouter";
    }




    @PostMapping("/ajouter")
    public String add(@Valid Etudiant etudiant,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            return "admin/etudiant/ajouter";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/etudiant/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "etudiant ajouter");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String nom = etudiant.getNom();

        String prenom=etudiant.getPrenom();
        Etudiant etudiantExiste = etudiantRepertoire.findByNomAndPrenom(nom,prenom);

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image doit etre jpg ,jpeg ou png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
        }
        else if ( etudiantExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "le nom et prenom sont deja pris ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
        } else {
            etudiant.setImage(filename);
            etudiantRepertoire.save(etudiant);

            Files.write(path, bytes);
        }

        return "redirect:/admin/etudiant/ajouter";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable int id, RedirectAttributes redirectAttributes) {

        etudiantRepertoire.deleteById(id);



        redirectAttributes.addFlashAttribute("message", "etudiant(e) supprimer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/etudiant";
    }


    @GetMapping("/editer/{id}")
    public String getEditer(@PathVariable int id, Model model){


        Etudiant etudiant = etudiantRepertoire.getOne(id);

        model.addAttribute("etudiant", etudiant);

        return "admin/etudiant/editer";
    }





    @PostMapping("/editer")
    public String editi(@Valid Etudiant etudiant,
                        BindingResult bindingResult,
                        MultipartFile file,
                        RedirectAttributes redirectAttributes,
                        Model model) throws IOException {




        Etudiant etudiantEditer = etudiantRepertoire.getOne(etudiant.getId());


        if (bindingResult.hasErrors()) {
            model.addAttribute("etuname", etudiantEditer.getNom());
            return "admin/etudiant/editer";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/etudiant/" + filename);

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg")|| filename.endsWith("jpeg") || filename.endsWith("jpg")||filename.endsWith("png") ) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "etudiant editer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Etudiant etudiant1 = etudiantRepertoire.findByNomAndPrenomAndIdNot(etudiant.getNom(),etudiant.getPrenom(), etudiant.getId());

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image doit etre jpeg, jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
        }
        else if ( etudiant1 != null ) {
            redirectAttributes.addFlashAttribute("message", "etudiant existe, choisir un  autre nom ou prenom");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
        } else {


            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/etudiant/" + etudiantEditer.getImage());
                Files.delete(path2);
                etudiant.setImage(filename);
                Files.write(path, bytes);
            } else {
                etudiant.setImage(etudiantEditer.getImage());
            }

            etudiantRepertoire.save(etudiant);

        }

        return "redirect:/admin/etudiant/editer/" + etudiant.getId();
    }




    @Autowired
    private EmpruntsRepertoire empruntsRepertoire;


    @GetMapping("/historique/{id}")
    public String historique(@PathVariable int id,Model model, @RequestParam(value="page", required = false) Integer p){



        Etudiant etudiant =etudiantRepertoire.getOne(id);


        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);






        Page<Emprunts>liste=empruntsRepertoire.findByetudiantid(etudiant.getId(),pageable);




        List<Emprunts>listee=empruntsRepertoire.findAllByetudiantid(etudiant.getId());



        List<Livre>livres=livreRepertoire.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Livre livre : livres) {
            cats.put(livre.getId(), livre.getTitre());
        }

        int count =listee.size();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);




        model.addAttribute("livres",cats);





        model.addAttribute("etudiant",etudiant);
        model.addAttribute("emprunts",liste);



        return "/admin/etudiant/historique";
    }

    @Autowired
    private LivreRepertoire livreRepertoire;


}
