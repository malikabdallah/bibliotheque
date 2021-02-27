package com.bookstore.projet.controlleur.admin;

import com.bookstore.projet.model.repertoire.EmpruntsRepertoire;
import com.bookstore.projet.model.repertoire.EtudiantRepertoire;
import com.bookstore.projet.model.repertoire.LivreRepertoire;
import com.bookstore.projet.model.donnee.*;
import com.bookstore.projet.model.fonction.Calcul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/emprunts")
public class AdminEmpruntsController {


    @GetMapping("/retards")
    public String retard(Model model){





        List<Emprunts>liste=empruntsRepertoire.trouvertouslesretards();


        model.addAttribute("emprunts",liste);



        HashMap<Integer, String> livres = new HashMap<>();
        for (Livre livre : livreRepertoire.findAll()) {
            livres.put(livre.getId(), livre.getTitre());
        }

        model.addAttribute("livresMap",livres);




        HashMap<Integer, String> etudiants = new HashMap<>();
        for (Etudiant etudiant : etudiantRepertoire.findAll()) {
            etudiants.put(etudiant.getId(), etudiant.getNom());
        }

        model.addAttribute("etudiantMap",etudiants);



        model.addAttribute("livres",livreRepertoire.findAll());

        model.addAttribute("etudiants",etudiantRepertoire.findAll());



        model.addAttribute("livres",livres);






        return "admin/emprunt/retard";
    }


    @Autowired
    private EmpruntsRepertoire empruntsRepertoire;

    @Autowired
    private EtudiantRepertoire etudiantRepertoire;


    @Autowired
    private LivreRepertoire livreRepertoire;





    @GetMapping()
    public String hello(Model model, @RequestParam(value = "page", required = false) Integer p){






        int perPage = 4;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Emprunts> emprunts = empruntsRepertoire.findAll(pageable);

        model.addAttribute("emprunts", emprunts);



        List<Livre> livres = livreRepertoire.findAll();

        HashMap<Integer, String> auth = new HashMap<>();
        for (Livre livre : livres) {
            auth.put(livre.getId(), livre.getTitre());
        }

        model.addAttribute("livres",auth);




        List<Etudiant> etudiants = etudiantRepertoire.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Etudiant etudiant : etudiants) {
            cats.put(etudiant.getId(), etudiant.getNom());
        }

        model.addAttribute("etudiants",cats);





        List<Emprunts>listes=empruntsRepertoire.findAll();



        /*for(Emprunts emprunt:listes){
            if(emprunt.isRetard()!=1){
                if(emprunt.getDateretoureffective()==null){

                    String S1=emprunt.getDateretour();
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String date = simpleDateFormat.format(new Date());

                    String S2=date;

                    int index=-1;
                    try {
                         index=Calcul.compareDate(S1,S2);
                    } catch (ParseException e) {
                    }

                    if(index<0){

                        System.out.println(S1+ " VS "+ S2+" = "+index);

                        Emprunts copie=new Emprunts();
                        copie.setId(emprunt.getId());
                        copie.setDateemprunt(emprunt.getDateemprunt());
                        copie.setDateretour(emprunt.getDateretour());
                        copie.setDateretoureffective(emprunt.getDateretoureffective());
                        copie.setEtudiantid(emprunt.getEtudiantid());
                        copie.setLivreid(emprunt.getLivreid());
                        copie.setRenouvellement(emprunt.getRenouvellement());
                        copie.setRetard(1);

                        empruntsRepertoire.delete(emprunt);
                        empruntsRepertoire.save(copie);

                    }
                }else{

                    String S1=emprunt.getDateretoureffective();
                    String S2=emprunt.getDateretour();

                    int index=-1;
                    try {
                         index=Calcul.compareDate(S1,S2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(index<0){
                        System.out.println(S1+ " VS "+ S2+" = "+index);
                        Emprunts copie=new Emprunts();
                        copie.setId(emprunt.getId());
                        copie.setDateemprunt(emprunt.getDateemprunt());
                        copie.setDateretour(emprunt.getDateretour());
                        copie.setDateretoureffective(emprunt.getDateretoureffective());
                        copie.setEtudiantid(emprunt.getEtudiantid());
                        copie.setLivreid(emprunt.getLivreid());
                        copie.setRenouvellement(emprunt.getRenouvellement());
                        copie.setRetard(1);

                        empruntsRepertoire.delete(emprunt);
                        empruntsRepertoire.save(copie);

                    }
                }
            }
        }


*/
        long count = empruntsRepertoire.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/emprunt/index";
    }










}
