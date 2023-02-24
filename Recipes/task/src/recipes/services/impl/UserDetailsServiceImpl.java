package recipes.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.models.User;
import recipes.models.dto.UserDTO;
import recipes.repositories.UserRepo;
import recipes.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean isExists(UserDTO userDTO) {
        return userRepo.findByEmail(userDTO.getEmail()).isPresent();
    }

    public void addUser(UserDTO userDTO) {
        userRepo.save(new User(userDTO));
    }

    public void deleteAll(){ userRepo.deleteAll();}

    public void printAll(){
        System.out.println(userRepo.findAll().toString());
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }
        return new UserDetailsImpl(user);
    }




}