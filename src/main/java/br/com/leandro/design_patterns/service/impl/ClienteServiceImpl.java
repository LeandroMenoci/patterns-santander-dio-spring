package br.com.leandro.design_patterns.service.impl;

import br.com.leandro.design_patterns.model.Cliente;
import br.com.leandro.design_patterns.model.ClienteRepository;
import br.com.leandro.design_patterns.model.Endereco;
import br.com.leandro.design_patterns.model.EnderecoRepository;
import br.com.leandro.design_patterns.service.ClienteService;
import br.com.leandro.design_patterns.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class ClienteServiceImpl implements ClienteService {
    // Singleton: Injetar os componentes do Spring com @Autowired
    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;

    public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, ViaCepService viaCepService) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepService = viaCepService;
    }

    // Strategy: Implementar os métodos definidos na interface
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples


    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
//        Optional<Cliente> cliente = clienteRepository.findById(id);
//        return cliente.get();

        // Atualizado, melhoria caso não encontre um erro, lançar um 404 ao invés de quebrar a aplicação com um erro 500
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o ID: " + id));
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
//        Optional<Cliente> clienteBd = clienteRepository.findById(id);
//        if(clienteBd.isPresent()) {
//            salvarClienteComCep(cliente);
//        }

        //Atualizado melhoria
        buscarPorId(id);
        cliente.setId(id);
        salvarClienteComCep(cliente);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
