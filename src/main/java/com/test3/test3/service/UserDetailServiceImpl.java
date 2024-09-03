package com.test3.test3.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test3.test3.persistence.entity.UserEntity;
import com.test3.test3.persistence.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // aca tengo que que pasar user Entity a User que es propio de SpringSecurity
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        
        // aca tengo los roles
        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
                // aca tengo los permisos de esos roles
        userEntity.getRoles()
            .stream()
            .flatMap(role->role.getPermissionList().stream())
            .forEach(permission->authorityList.add(new SimpleGrantedAuthority(permission.getName())));
                
        // con esto le digo a springsecurity que busque los usuarios de la bd y tome los permisos y roles que entiende springsecurity
        
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isAccountNoLocked(),
                userEntity.isCredentialsNoExpired(),
                authorityList);
    }

}
