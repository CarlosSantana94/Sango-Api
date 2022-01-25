package sango.bucapps.api.Models.DTO;

import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;

import java.util.List;

public class OpcionesPrendaDto {
    private Long id;
    private String nombre;
    private List<SubOpcionesPrendaDto> subOpcionesPrenda;
}
