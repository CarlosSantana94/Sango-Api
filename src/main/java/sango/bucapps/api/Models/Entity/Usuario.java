package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private Long tel;

    @Column
    private String token;

    @Column
    private String img;



}
