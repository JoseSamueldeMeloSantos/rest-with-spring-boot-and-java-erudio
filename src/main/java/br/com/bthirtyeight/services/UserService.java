package br.com.bthirtyeight.services;

import br.com.bthirtyeight.model.User;
import br.com.bthirtyeight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    //ta utilizando dois tipos de injecão ao mesmo tempo (contrutor e autowired)
    @Autowired
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//metodo do UserDetailsService
        User user = repository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");//exception do spring security
        }
    }
}
