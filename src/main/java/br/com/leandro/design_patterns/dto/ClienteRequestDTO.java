package br.com.leandro.design_patterns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório e não pode estar em branco")
        String nome,

        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos (ex: 01001000).")
        String cep) {
}
