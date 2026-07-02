package br.com.leandro.design_patterns.dto;

import br.com.leandro.design_patterns.model.Endereco;

public record ClienteResponseDTO(Long id, String nome, Endereco endereco) {
}
