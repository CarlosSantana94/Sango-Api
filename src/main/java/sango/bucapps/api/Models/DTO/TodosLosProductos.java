package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import sango.bucapps.api.Models.Entity.Servicio;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodosLosProductos {
    List<Servicio> servicios;
}