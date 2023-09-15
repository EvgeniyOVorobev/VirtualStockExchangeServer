package ru.ev.VirtualStockExchangeServer.models.User;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.ev.VirtualStockExchangeServer.enums.Role;

import javax.persistence.*;

@Entity
@Table(name="user")
@Data
public class User implements UserDetails {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="password")
    private String password;
    @Column(name="name")
    private String name;
    @Column(name="date")
    private LocalDate date;
    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name="user_role",joinColumns = @JoinColumn (name="user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet= new HashSet<>();
    private void init(){
        date=LocalDate.parse("2014-06-09");
    }

    //Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSet;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
