package sango.bucapps.api.Models.DTO.conekta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargesDto {
    private PaymentMethodDto payment_method;
    private Integer amount;
}
