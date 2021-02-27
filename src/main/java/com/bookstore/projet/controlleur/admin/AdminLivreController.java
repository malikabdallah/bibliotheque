package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.donnee.*;
import com.bookstore.projet.model.repertoire.*;
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
@RequestMapping("/admin/livres")
public class AdminLivreController  {





    @Autowired
    private LivreRepertoire livreRepertoire;


    @Autowired
    private AutheurRepertoire autheurRepertoire;

    @Autowired
    private TypeRepertoire typeRepertoire;

    @GetMapping()
    public String hello(Model model, @RequestParam(value = "page", required = false) Integer p){






        int perPage = 10;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Livre> livres = livreRepertoire.findAll(pageable);

        model.addAttribute("livres", livres);



        List<Autheurs> autheurs = autheurRepertoire.findAll();

        HashMap<Integer, String> auth = new HashMap<>();
        for (Autheurs autheur : autheurs) {
            auth.put(autheur.getId(), autheur.getNom());
        }

        model.addAttribute("autheurs",auth);


        List<Type> types = typeRepertoire.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Type type : types) {
            cats.put(type.getId(), type.getNom());
        }

        model.addAttribute("types",cats);

        long count = livreRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        System.out.println("page "+page);
        return "admin/livres/index";
    }




    @PostMapping("/ajouter")
    public String add(@Valid Livre livre,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            return "admin/livres/ajouter";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/livre/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "livre ajouter");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Livre livreExiste = livreRepertoire.findByTitreAndIdNot(livre.getTitre(),livre.getId());

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image doit etre jpg ,jpeg ou png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("livre", livre);


            List<Autheurs> autheurs = autheurRepertoire.findAll();
            redirectAttributes.addFlashAttribute("autheurs", autheurs);

            List<Type> types = typeRepertoire.findAll();
            redirectAttributes.addFlashAttribute("types", types);
        }
        else if ( livreExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "le titre existe ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("livre", livre);




            List<Autheurs> autheurs = autheurRepertoire.findAll();
            redirectAttributes.addFlashAttribute("autheurs", autheurs);

            List<Type> types = typeRepertoire.findAll();
            redirectAttributes.addFlashAttribute("types", types);
        } else {
            livre.setImage(filename);
            livreRepertoire.save(livre);

            Files.write(path, bytes);
        }

        livre.setDisponible(1);
        return "redirect:/admin/livres/ajouter";
    }






    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable int id, RedirectAttributes redirectAttributes) {

        livreRepertoire.deleteById(id);



        redirectAttributes.addFlashAttribute("message", "livre supprimer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/livres";
    }



    @GetMapping("/editer/{id}")
    public String editer(@PathVariable int id,  Model model) {

        Livre livre=livreRepertoire.getOne(id);
        model.addAttribute("livre",livre);

        Autheurs autheur1=autheurRepertoire.getOne(livre.getAutheursId());
        Type type1 =typeRepertoire.getOne(livre.getTypeId());

        model.addAttribute("auth",autheur1);
        model.addAttribute("typea",type1);


        List<Autheurs> autheurs = autheurRepertoire.findAll();
        model.addAttribute("autheurs", autheurs);




        List<Type> types = typeRepertoire.findAll();
        model.addAttribute("types", types);






        return "admin/livres/editer";
    }


    @Autowired
    private EtudiantRepertoire etudiantRepertoire;


    @GetMapping("/historique/{id}")
    public String historique(@PathVariable int id,Model model, @RequestParam(value="page", required = false) Integer p){






        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Emprunts> emprunts = empruntsRepertoire.findAllBylivreid(id,pageable);



        HashMap<Integer, String> cats = new HashMap<>();
        for (Emprunts emprunts1 : emprunts) {
            if(emprunts1.getLivreid()==id) {
                Etudiant etudiant=etudiantRepertoire.getOne(emprunts1.getEtudiantid());
                cats.put(emprunts1.getId(), etudiant.getNom());

            }
        }


        model.addAttribute("cats",cats);



        long count = emprunts.getTotalElements();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("emprunts",emprunts);








        Livre livre=livreRepertoire.getOne(id);
        Autheurs autheurs=autheurRepertoire.getOne(livre.getautheursId());


        model.addAttribute("livre",livre);
Type type=typeRepertoire.getOne(livre.gettypeId());

model.addAttribute("autheur",autheurs);
model.addAttribute("type",type);





        return "/admin/livres/historique";
    }

    @Autowired
    private EmpruntsRepertoire empruntsRepertoire;



    @GetMapping("/ajouter")
    public String add(Livre livre, Model model) {

        List<Autheurs> autheurs = autheurRepertoire.findAll();
        model.addAttribute("autheurs", autheurs);

        List<Type> types = typeRepertoire.findAll();
        model.addAttribute("types", types);



        return "admin/livres/ajouter";
    }










    @PostMapping("/editer")
    public String editi(@Valid Livre livre,
                        BindingResult bindingResult,
                        MultipartFile file,
                        RedirectAttributes redirectAttributes,
                        Model model) throws IOException {




        Livre currentLivre = livreRepertoire.getOne(livre.getId());


        if (bindingResult.hasErrors()) {
            model.addAttribute("livrename", currentLivre.getTitre());
            return "admin/type/editer";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/livre/" + filename);

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg")|| filename.endsWith("jpeg") || filename.endsWith("png") ) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "livre editer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");


        Livre livreExiste = livreRepertoire.findByTitreAndIdNot(livre.getTitre(),livre.getId());

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("livre", livre);
        }
        else if ( livreExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "livre existe deja , choisir un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("livre", livre);
        } else {


            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/livre/" + currentLivre.getImage());
                Files.delete(path2);
                livre.setImage(filename);
                Files.write(path, bytes);
            } else {
                livre.setImage(currentLivre.getImage());
            }

            livre.setDisponible(1);
            livreRepertoire.save(livre);

        }

        return "redirect:/admin/livres/editer/" + livre.getId();
    }




}
