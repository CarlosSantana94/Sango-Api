package sango.bucapps.api.Models.DTO.conekta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodDto {
    private String type;
    private String token_id;
}
