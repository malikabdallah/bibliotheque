package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.repertoire.TypeRepertoire;
import com.bookstore.projet.model.donnee.Type;
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

@Controller
@RequestMapping("/admin/type")
public class AdminTypeController {




    @Autowired
    private TypeRepertoire typeRepertoire;






    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable int id, RedirectAttributes redirectAttributes) {

        typeRepertoire.deleteById(id);



        redirectAttributes.addFlashAttribute("message", "type supprimer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/type";
    }




    @GetMapping
    public String home(Model model, @RequestParam(value="page", required = false) Integer p){



        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Type> products = typeRepertoire.findAll(pageable);

        model.addAttribute("types", products);

        long count = typeRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);


        return "admin/type/index";
    }


    @GetMapping("/ajouter")
    public String add(Type type, Model model) {

        return "admin/type/ajouter";
    }






    @PostMapping("/ajouter")
    public String add(@Valid Type type,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            return "admin/type/ajouter";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/adminmedia/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "type ajouter");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String nom = type.getNom();

        Type typeExiste = typeRepertoire.findByNom(nom);

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image doit etre jpg ,jpeg ou png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("type", type);
        }
        else if ( typeExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "le type existe ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("type", type);
        } else {
            type.setImage(filename);
            typeRepertoire.save(type);

            Files.write(path, bytes);
        }

        return "redirect:/admin/type/ajouter";
    }













    @PostMapping("/editer")
    public String editi(@Valid Type type,
                       BindingResult bindingResult,
                       MultipartFile file,
                       RedirectAttributes redirectAttributes,
                       Model model) throws IOException {




        Type currenttype = typeRepertoire.getOne(type.getId());


        if (bindingResult.hasErrors()) {
            model.addAttribute("typename", currenttype.getNom());
            return "admin/type/editer";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/adminmedia/" + filename);

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg")|| filename.endsWith("jpeg") || filename.endsWith("png") ) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "type editer");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = type.getNom();

        Type productExists = typeRepertoire.findByNomAndIdNot(slug, type.getId());

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("type", type);
        }
        else if ( productExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("type", type);
        } else {

            type.setNom(slug);

            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/adminmedia/" + currenttype.getImage());
                Files.delete(path2);
                type.setImage(filename);
                Files.write(path, bytes);
            } else {
                type.setImage(currenttype.getImage());
            }

            typeRepertoire.save(type);

        }

        return "redirect:/admin/type/editer/" + type.getId();
    }






    @GetMapping("/editer/{id}")
    public String getEditer(@PathVariable int id,Model model){


        Type type = typeRepertoire.getOne(id);

        model.addAttribute("type", type);

        return "admin/type/editer";
    }

}
