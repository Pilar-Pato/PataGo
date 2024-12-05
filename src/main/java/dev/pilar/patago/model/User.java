package dev.pilar.patago.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Dog> dogs;

    // Implementar los métodos de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir roles en autoridades para usar en la autenticación
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(() -> role.getName());  // Asumiendo que role.getName() devuelve "ROLE_USER" o "ROLE_ADMIN"
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;  // Se hereda de UserDetails, así que no necesitas definirlo aquí.
    }

    @Override
    public String getUsername() {
        return this.username;  // Se hereda de UserDetails, así que no necesitas definirlo aquí.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Puedes ajustarlo según tus necesidades
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Puedes ajustarlo según tus necesidades
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Puedes ajustarlo según tus necesidades
    }

    @Override
    public boolean isEnabled() {
        return true;  // Puedes ajustarlo según tus necesidades
    }

    // Getters y setters adicionales

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

   

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
