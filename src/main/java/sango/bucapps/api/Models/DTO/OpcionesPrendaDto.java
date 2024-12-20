package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpcionesPrendaDto {
    private Long id;
    private String nombre;
    private String img;
    private Long servicioId;
    private List<SubOpcionesPrendaDto> subOpcionesPrenda;
}
