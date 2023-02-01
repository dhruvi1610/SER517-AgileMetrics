package edu.asu.cassess.security;

import edu.asu.cassess.persist.entity.security.Authority;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        System.out.println("login: " + login + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---------------------------");
        log.debug("Authenticating {}", login);

        BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder(11);
        User user = userRepo.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        } else if (!user.getEnabled()) {
            throw new UserNotEnabledException("User " + login + " was not enabled");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Authority authority : user.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }

        return new org.springframework.security.core.userdetails.User(login, user.getPassword(),
                grantedAuthorities);
    }
/*
        return new org.springframework.security.core.userdetails.User(login, passEncode.encode(user.getPassword()),
                grantedAuthorities);
    }
    */
}
