package com.example.demo.Models;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;


@Entity
@Table()
public class User implements UserDetails{
    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    private long id;
    private String name;
    private LocalDate dob;
    private String email;
    private String username;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;;
    private Boolean locked;
    private Boolean enabled;
    private String password;
    //this will not be in the database
    @Transient
    private Integer age;


    public User() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       /*Basic concrete implementation of a GrantedAuthority.
        Stores a String representation of an authority granted to the Authentication object.*/

        SimpleGrantedAuthority authority = 
        new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }


  
    
    public User(String name, LocalDate dob, String email, String username, UserRole userRole, Boolean locked,
            Boolean enabled, String password, Integer age) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.username = username;
        this.userRole = userRole;
        this.locked = locked;
        this.enabled = enabled;
        this.password = password;
        this.age = age;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public LocalDate getDob() {
        return dob;
    }


    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public Integer getAge() {
        return Period.between(this.dob,LocalDate.now()).getYears();
    }


    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }
    
    public UserRole getUserRole() {
        return userRole;
    }


    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


    public Boolean getLocked() {
        return locked;
    }


    public void setLocked(Boolean locked) {
        this.locked = locked;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public Boolean getEnabled() {
        return enabled;
    }


    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return !locked;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return enabled;
    }    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
        result = prime * result + ((locked == null) ? 0 : locked.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (dob == null) {
            if (other.dob != null)
                return false;
        } else if (!dob.equals(other.dob))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (userRole != other.userRole)
            return false;
        if (locked == null) {
            if (other.locked != null)
                return false;
        } else if (!locked.equals(other.locked))
            return false;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (age == null) {
            if (other.age != null)
                return false;
        } else if (!age.equals(other.age))
            return false;
        return true;
    }

}
