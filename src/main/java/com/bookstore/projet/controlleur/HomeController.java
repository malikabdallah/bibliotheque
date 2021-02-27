package com.bookstore.projet.controlleur;


import com.bookstore.projet.model.donnee.Autheurs;
import com.bookstore.projet.model.donnee.Emprunts;
import com.bookstore.projet.model.donnee.Livre;
import com.bookstore.projet.model.repertoire.AutheurRepertoire;
import com.bookstore.projet.model.repertoire.EmpruntsRepertoire;
import com.bookstore.projet.model.repertoire.LivreRepertoire;
import com.bookstore.projet.model.repertoire.TypeRepertoire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/")
public class HomeController {






    @GetMapping("/erreur")
    public String echec (HttpServletRequest request){

        if (request.isUserInRole("ROLE_ADMIN")) {
            return "/admin/dash";
        }

        if (request.isUserInRole("ROLE_USER")) {
            return "/user/home";
        }

        return "echec";


    }

    /*

    @GetMapping("/inscription")
    public String  inscription(){
        return "inscription";
    }

*/


    @GetMapping("/admin")
    public String admin(){
        return "admin/dash";
    }



    @GetMapping("/login")
    public String  login(HttpServletRequest request){

        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_ADMIN") ){



            return "/admin/dash";
        }
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_USER") ){



            return "/user/home";
        }

        return "login";

        }





    @GetMapping("admin/dash")
    public String dash(){
        return "admin/dash";
    }



    @GetMapping("")
    public String hello( HttpServletRequest request){
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_ADMIN") ){



            return "/admin/dash";
        }
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_USER") ){




            return "/user/home";
        }

        return "login";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request, Model model) {
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_ADMIN") ){



            return "admin/dash";
        }
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_USER") ){

            List<Livre> livres = livreRepertoire.findLivre();

            model.addAttribute("livres", livres);


            List<Emprunts>listes=empruntsRepertoire.findAll();
            Map<Integer, Long> listePlus = listes.stream()
                    .collect(groupingBy(Emprunts::getLivreid, Collectors.counting()));

            System.out.println(listePlus);
            Map<Integer, Long> treeMap = new TreeMap<Integer, Long>(listePlus);
            System.out.println("*****************************************");
            System.out.println(treeMap);



            List<Livre> Bbest=new ArrayList<>();

            List<Livre>livreBest=livreRepertoire.findAll();
            for(Long id:treeMap.values()){
                Integer i = (int) (long) id;
                Livre l=livreBest.get(i);
                Bbest.add(l);

            }





            model.addAttribute("best",Bbest);





            List<Autheurs> autheurs = autheurRepertoire.findAll();
            HashMap<Integer, String> auth = new HashMap<>();
            for (Autheurs autheur : autheurs) {
                auth.put(autheur.getId(), autheur.getNom());
            }
            model.addAttribute("autheurs",auth);

            return  "user/home";
        }

        return "login";
    }

    @Autowired
    private EmpruntsRepertoire empruntsRepertoire;

    @Autowired
    private LivreRepertoire livreRepertoire;

    @Autowired
    private AutheurRepertoire autheurRepertoire;

@GetMapping("/success")
public String getH(){
        return "success";
}

    @GetMapping("/redirige")
    public String redirige(HttpServletRequest request) {
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_ADMIN") ){



            return "admin/dash";
        }
        if(request.getUserPrincipal() != null && request.isUserInRole("ROLE_USER") ){



            return  "user/home";
        }

        return "login";
    }
}
