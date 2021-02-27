package com.bookstore.projet.securite;

import com.bookstore.projet.model.repertoire.AdminRepertoire;
import com.bookstore.projet.model.repertoire.EtudiantRepertoire;
import com.bookstore.projet.model.donnee.Admin;
import com.bookstore.projet.model.donnee.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    private EtudiantRepertoire userRepo;

    @Autowired
    private AdminRepertoire adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Etudiant user = userRepo.findByUsername(username);
        Admin admin = adminRepo.findByUsername(username);

        if (user != null) {
            return user;
        }

        if (admin != null) {
            return admin;
        }

        throw new UsernameNotFoundException("User: " + username + " not found!");
    }

}
