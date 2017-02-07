package com.idione.inoc.services;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.PocUserRole;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired private PocUserService pocUserService;
        
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Base.open();
        PocUser user = pocUserService.getPocUserByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Username not found"); 
        }
        List<GrantedAuthority> roles = getGrantedAuthorities(user);
        UserDetails ud =  new org.springframework.security.core.userdetails.User(user.getString("email_address"), user.getString("password"), roles);
        Base.close();
        return ud;
    }

    
    private List<GrantedAuthority> getGrantedAuthorities(PocUser user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for(PocUserRole pocUserRole : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority(pocUserRole.getString("role")));
            authorities.add(new SimpleGrantedAuthority("ROLE_"+pocUserRole.getString("role")));
        }
        return authorities;
    }
    
}
