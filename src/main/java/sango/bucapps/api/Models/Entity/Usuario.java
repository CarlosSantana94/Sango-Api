package sango.bucapps.api.Models.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {

    @Id
    private String id;

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

    @Column(columnDefinition = "boolean default false")
    private Boolean puedePagarConCC;

}
