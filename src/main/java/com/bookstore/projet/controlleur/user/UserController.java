package com.bookstore.projet.controlleur.user;

import com.bookstore.projet.model.donnee.Commentaire;
import com.bookstore.projet.model.donnee.*;
import com.bookstore.projet.model.repertoire.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private AdminRepertoire adminRepertoire;


    @GetMapping("/profile")
    public String monprofile(Model model, HttpServletRequest request, Principal principal, @RequestParam(value = "page", required = false) Integer p) {


        if (request.isUserInRole("ROLE_ADMIN")) {

            Admin admin = adminRepertoire.findByUsername(principal.getName());

            // String message="vous etes un admin ! acceder a votre profile cote admin <br/>";
            //  String message="<a th:href=\'@{/admin/profile}\'/"+admin.getId()+"> admin </a>";
            String message = "/admin/profile/" + admin.getId();
            model.addAttribute("message", message);


        } else {


            Etudiant etudiant = etudiantRepertoire.findByUsername(principal.getName());
            model.addAttribute("etudiant", etudiant);


            int perPage = 4;
            //int p;
            int page = (p != null) ? p : 0;
            Pageable pageable = PageRequest.of(page, perPage);




            Page<Emprunts> liste = empruntsRepertoire.findByetudiantid(etudiant.getId(), pageable);


            List<Emprunts> listee = empruntsRepertoire.findAllByetudiantid(etudiant.getId());


            List<Livre> livres = livreRepertoire.findAll();

            HashMap<Integer, String> cats = new HashMap<>();
            for (Livre livre : livres) {
                cats.put(livre.getId(), livre.getTitre());
            }

            int count = listee.size();
            double pageCount = Math.ceil((double) count / (double) perPage);

            model.addAttribute("pageCount", (int) pageCount);
            model.addAttribute("perPage", perPage);
            model.addAttribute("count", count);
            model.addAttribute("page", page);


            model.addAttribute("livres", cats);


            model.addAttribute("emprunts", liste);


        }


        return "/user/profile";

    }


    @Autowired
    private EmpruntsRepertoire empruntsRepertoire;

    @Autowired
    private LivreRepertoire livreRepertoire;

    @Autowired
    private EtudiantRepertoire etudiantRepertoire;


    @GetMapping("/emprunter/{id}")
    public String emprunter(@PathVariable int id, Model model, HttpServletRequest request, Principal principal) {

        if (request.getUserPrincipal() != null && request.isUserInRole("ROLE_ADMIN")) {


            model.addAttribute("messageAdmin", "un admin ne peut pas emprunter de livre");
            return "admin/dash";
        }


        if (request.getUserPrincipal() != null && request.isUserInRole("ROLE_USER")) {

            Livre livre = livreRepertoire.getOne(id);
            Emprunts emprunts = new Emprunts();
            emprunts.setLivreid(livre.getId());
            emprunts.setRenouvellement(0);
            emprunts.setRetard(0);
            String pseudo = principal.getName();
            Etudiant etudiant = etudiantRepertoire.findByUsername(pseudo);
            emprunts.setEtudiantid(etudiant.getId());


            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());

            emprunts.setDateemprunt(date);


            String oldDate = date;
            System.out.println("Date before Addition: " + oldDate);
            //Specifying date format that matches the given date
            Calendar c = Calendar.getInstance();
            try {
                //Setting the date to the given date
                c.setTime(simpleDateFormat.parse(oldDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Number of Days to add
            c.add(Calendar.DAY_OF_MONTH, 14);
            //Date after adding the days to the given date
            String newDate = simpleDateFormat.format(c.getTime());
            //Displaying the new Date after addition of Days
            emprunts.setDateretour(newDate);

            empruntsRepertoire.save(emprunts);

            livre.setDisponible(0);
            livreRepertoire.save(livre);

            model.addAttribute("ajout", "livre bien emprunter !");


            List<Livre> livres = livreRepertoire.findLivre();

            model.addAttribute("livres", livres);


            List<Autheurs> autheurs = autheurRepertoire.findAll();
            HashMap<Integer, String> auth = new HashMap<>();
            for (Autheurs autheur : autheurs) {
                auth.put(autheur.getId(), autheur.getNom());
            }
            model.addAttribute("autheurs", auth);


            return "/user/nous";
        }

        return "login";
    }




    @GetMapping("/autheur/consulter/{id}")
    public String getEditer(@PathVariable int id, Model model, @RequestParam(value = "page", required = false) Integer p) {


        int perPage = 9;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Livre> livres = livreRepertoire.findByautheursId(id, pageable);
        model.addAttribute("livres", livres);


        long count = livres.getTotalElements();

        Autheurs autheurs = autheurRepertoire.getOne(id);
        model.addAttribute("autheur", autheurs);
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);


        List<Autheurs> autheurrs = autheurRepertoire.findAll();

        HashMap<Integer, String> auth = new HashMap<>();
        for (Autheurs autheur : autheurrs) {
            auth.put(autheur.getId(), autheur.getNom());
        }

        model.addAttribute("autheurs", autheurrs);


        List<Type> types = typeRepertoire.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Type type : types) {
            cats.put(type.getId(), type.getNom());
        }

        model.addAttribute("types", cats);


        return "/user/consulterAutheur";

    }


    @GetMapping("")
    public String home(Model model) {


        List<Livre> livres = livreRepertoire.findLivre();

        model.addAttribute("livres", livres);


        List<Emprunts> listes = empruntsRepertoire.findAll();
        Map<Integer, Long> listePlus = listes.stream()
                .collect(groupingBy(Emprunts::getLivreid, Collectors.counting()));

        System.out.println(listePlus);
        Map<Integer, Long> treeMap = new TreeMap<Integer, Long>(listePlus);
        System.out.println("*****************************************");
        System.out.println(treeMap);


        List<Livre> Bbest = new ArrayList<>();

        List<Livre> livreBest = livreRepertoire.findAll();
        for (Long id : treeMap.values()) {
            Integer i = (int) (long) id;
            Livre l = livreBest.get(i);
            Bbest.add(l);

        }


        System.out.println("\n\n" + Bbest);
        model.addAttribute("best", Bbest);


        List<Autheurs> autheurs = autheurRepertoire.findAll();
        HashMap<Integer, String> auth = new HashMap<>();
        for (Autheurs autheur : autheurs) {
            auth.put(autheur.getId(), autheur.getNom());
        }
        model.addAttribute("autheurs", auth);

        return "/user/home";
    }


    @GetMapping("/home")
    public String gethome(Model model) {

        List<Livre> livres = livreRepertoire.findLivre();

        model.addAttribute("livres", livres);


        List<Autheurs> autheurs = autheurRepertoire.findAll();
        HashMap<Integer, String> auth = new HashMap<>();
        for (Autheurs autheur : autheurs) {
            auth.put(autheur.getId(), autheur.getNom());
        }
        model.addAttribute("autheurs", auth);

        return "/user/home";


    }


    @GetMapping("/nous")
    public String nous() {
        return "/user/nous";
    }


    @Autowired
    private AutheurRepertoire autheurRepertoire;


    @GetMapping("/autheurs")
    public String autheurs(Model modem, @RequestParam(value = "page", required = false) Integer p) {


        int perPage = 10;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Autheurs> products = autheurRepertoire.findAll(pageable);

        modem.addAttribute("autheurs", products);

        long count = autheurRepertoire.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        modem.addAttribute("pageCount", (int) pageCount);
        modem.addAttribute("perPage", perPage);
        modem.addAttribute("count", count);
        modem.addAttribute("page", page);


        return "/user/autheurs";
    }


    @Autowired
    private TypeRepertoire typeRepertoire;

    @GetMapping("/categories/{id}")
    public String categories(@PathVariable int id, Model model, @RequestParam(value = "page", required = false) Integer p) {


        int perPage = 16;
        //int p;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        List<Livre> livres = livreRepertoire.findBytypeId(id, pageable);

        model.addAttribute("livres", livres);


        long count = livreRepertoire.findBytypeId(id).size();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);


        List<Autheurs> autheurs = autheurRepertoire.findAll();
        HashMap<Integer, String> auth = new HashMap<>();
        for (Autheurs autheur : autheurs) {
            auth.put(autheur.getId(), autheur.getNom());
        }
        model.addAttribute("autheurs", auth);
        Type type = typeRepertoire.getOne(id);
        model.addAttribute("type", type);
        return "user/categories";


    }


    @GetMapping("/renouvelle/{id}")
    public String categories(@PathVariable int id, Model model, HttpServletRequest request,Principal principal, @RequestParam(value = "page", required = false) Integer p) {

        if (request.isUserInRole("ROLE_ADMIN")) {

            Admin admin = adminRepertoire.findByUsername(principal.getName());

            // String message="vous etes un admin ! acceder a votre profile cote admin <br/>";
            //  String message="<a th:href=\'@{/admin/profile}\'/"+admin.getId()+"> admin </a>";
            String message = "/admin/profile/" + admin.getId();
            model.addAttribute("message", message);


        } else {


            Etudiant etudiant = etudiantRepertoire.findByUsername(principal.getName());
            model.addAttribute("etudiant", etudiant);


            int perPage = 4;
            //int p;
            int page = (p != null) ? p : 0;
            Pageable pageable = PageRequest.of(page, perPage);


            Page<Emprunts> liste = empruntsRepertoire.findByetudiantid(etudiant.getId(), pageable);


            List<Emprunts> listee = empruntsRepertoire.findAllByetudiantid(etudiant.getId());


            List<Livre> livres = livreRepertoire.findAll();

            HashMap<Integer, String> cats = new HashMap<>();
            for (Livre livre : livres) {
                cats.put(livre.getId(), livre.getTitre());
            }

            int count = listee.size();
            double pageCount = Math.ceil((double) count / (double) perPage);

            model.addAttribute("pageCount", (int) pageCount);
            model.addAttribute("perPage", perPage);
            model.addAttribute("count", count);
            model.addAttribute("page", page);


            model.addAttribute("livres", cats);


            model.addAttribute("emprunts", liste);


        }

        Emprunts emprunts=empruntsRepertoire.getOne(id);
        if(emprunts.getRenouvellement()==1 ){



            model.addAttribute("messageE", "livre deja emprunter");
            return "/user/profile";
        }

        if(emprunts.isRetard()==1){
            model.addAttribute("messageE", "livre en retards pas possible de l emprunter");
            return "/user/profile";
        }








        emprunts.setRenouvellement(1);








        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


        String date = emprunts.getDateretour();

        emprunts.setDateemprunt(date);


        String oldDate = date;
        System.out.println("Date before Addition: " + oldDate);
        //Specifying date format that matches the given date
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(simpleDateFormat.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 14);
        //Date after adding the days to the given date
        String newDate = simpleDateFormat.format(c.getTime());
        //Displaying the new Date after addition of Days
        emprunts.setDateretour(newDate);
















        empruntsRepertoire.save(emprunts);
        return "/user/profile";

    }




    @GetMapping("/retourner/{id}")
    public String getEditer(Model model, @PathVariable int id, HttpSession session) {



        if (session.getAttribute("id") == null) {


            session.setAttribute("id", id);
        } else {
           session.removeAttribute("id");
            session.setAttribute("id", id);


        }




        Emprunts emprunts=empruntsRepertoire.getOne(id);
        model.addAttribute("emprunt",emprunts);


        model.addAttribute("commentaire",new Commentaire());


        return  "/user/retourner";




    }




    @PostMapping("/retourner")
    public String poster(Commentaire commentaire,Model model,HttpSession session) {



        int id =(int)session.getAttribute("id");
        System.out.println("id="+id);

        Emprunts emprunts=empruntsRepertoire.getOne(id);
        System.out.println(emprunts);

        System.out.println("id etudiant="+emprunts.getEtudiantid());






        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());

        emprunts.setDateretoureffective(date);



        commentaire.setIdetudiant(emprunts.getEtudiantid());
        commentaire.setIdlivre(emprunts.getLivreid());


        System.out.println("commentaire ="+commentaire);

        commentaireRepertoire.save(commentaire);

        empruntsRepertoire.save(emprunts);
















        Livre livre=livreRepertoire.getOne(emprunts.getLivreid());
        livre.setDisponible(0);
        livreRepertoire.save(livre);





       // System.out.println(emprunts);

        model.addAttribute("ajout","le livre a bien ete retourner");

        return  "/user/nous";




    }


    @Autowired
    private CommentaireRepertoire commentaireRepertoire;




    @GetMapping("/livre/consulter/{id}")
    public String getConsulter(Model model, @PathVariable int id){
        Livre livre=livreRepertoire.getOne(id);
        List<Emprunts>emprunts=empruntsRepertoire.findAllBylivreid(id);
        model.addAttribute("livre",livre);
        model.addAttribute("nb",emprunts.size());

        List<Commentaire>commentaires=commentaireRepertoire.findAllByidlivre(id);
        model.addAttribute("commentaires",commentaires);



        List<Etudiant> etudiants = etudiantRepertoire.findAll();

        HashMap<Integer, String> auth = new HashMap<>();
        for (Etudiant etudiant : etudiants) {
            auth.put(etudiant.getId(), etudiant.getUsername());
        }



        model.addAttribute("etudiants",auth);




        return  "/user/livreHistorique";









    }


    }
