package br.com.leandro.design_patterns.service;

import br.com.leandro.design_patterns.dto.ClienteRequestDTO;
import br.com.leandro.design_patterns.dto.ClienteResponseDTO;
import br.com.leandro.design_patterns.model.Cliente;

public interface ClienteService {

    Iterable<ClienteResponseDTO> buscarTodos();
    ClienteResponseDTO buscarPorId(Long id);
    ClienteResponseDTO inserir(ClienteRequestDTO dto);
    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);
    void deletar(Long id);
}
