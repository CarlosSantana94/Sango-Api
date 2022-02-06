package sango.bucapps.api.Models.DTO.conekta;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LineItemDto {
    private String name;
    private String description;
    private Integer unit_price;
    private Long quantity;
    private List<String> tags;
    private String type;
}
