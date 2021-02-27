package com.bookstore.projet.securite;


import com.bookstore.projet.model.repertoire.AdminRepertoire;
import com.bookstore.projet.model.repertoire.EtudiantRepertoire;
import com.bookstore.projet.model.donnee.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/inscription")
public class ControlleurInscription {





    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepertoire adminRepertoire;

    @GetMapping
    public String register(Etudiant etudiant, HttpServletRequest request) {

        if (request.isUserInRole("ROLE_ADMIN")) {
            return "/admin/dash";
        }

        if (request.isUserInRole("ROLE_USER")) {
            return "/user/home";
        }




        return "inscription";
    }



    @Autowired
    private EtudiantRepertoire etudiantRepertoire;

    @PostMapping
    public String register(@Valid Etudiant etudiant,
                           BindingResult bindingResult,
                           MultipartFile file,
                           RedirectAttributes redirectAttributes,
                           Model model) throws IOException{





        if (bindingResult.hasErrors()) {
            return "inscription";
        }
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/etudiant/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", " inscription faite avec success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String nom = etudiant.getNom();

        String prenom=etudiant.getPrenom();
        Etudiant etudiantExiste = etudiantRepertoire.findByNomAndPrenom(nom,prenom);

        Etudiant pseudoExiste =etudiantRepertoire.findByUsername(etudiant.getUsername());

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image doit etre jpg ,jpeg ou png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
            return "redirect:/inscription";

        }
         else if ( etudiantExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "le nom et prenom sont deja pris ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);
            return "redirect:/inscription";

        }else if ( pseudoExiste != null ) {
            redirectAttributes.addFlashAttribute("message", "le pseudo est deja pris ,choisisez en un autre");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("etudiant", etudiant);

            return "redirect:/inscription";

        }else {
            etudiant.setImage(filename);
            etudiantRepertoire.save(etudiant);

            Files.write(path, bytes);
        }









//admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return "redirect:/login";
    }

}
