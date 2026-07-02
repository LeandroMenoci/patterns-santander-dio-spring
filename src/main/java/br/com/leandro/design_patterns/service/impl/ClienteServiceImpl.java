package br.com.leandro.design_patterns.service.impl;

import br.com.leandro.design_patterns.dto.ClienteRequestDTO;
import br.com.leandro.design_patterns.dto.ClienteResponseDTO;
import br.com.leandro.design_patterns.model.Cliente;
import br.com.leandro.design_patterns.model.ClienteRepository;
import br.com.leandro.design_patterns.model.Endereco;
import br.com.leandro.design_patterns.model.EnderecoRepository;
import br.com.leandro.design_patterns.service.ClienteService;
import br.com.leandro.design_patterns.service.ViaCepService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


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
    public Iterable<ClienteResponseDTO> buscarTodos() {
        // Busca todos no banco e converte um por um para DTO
        List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
        return clientes.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());

//        return clienteRepository.findAll();
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
//        Optional<Cliente> cliente = clienteRepository.findById(id);
//        return cliente.get();

        // Atualizado, melhoria caso não encontre um erro, lançar um 404 ao invés de quebrar a aplicação com um erro 500
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o ID: " + id));

        return converterParaResponse(cliente);
    }

    @Override
    public ClienteResponseDTO inserir(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        return salvarClienteComCep(cliente, dto.cep());
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
//        Optional<Cliente> clienteBd = clienteRepository.findById(id);
//        if(clienteBd.isPresent()) {
//            salvarClienteComCep(cliente);
//        }

        //Atualizado melhoria
        // Valida se o cliente existe (se não existir, lança 404)
        buscarPorId(id);

        //Prepara a entidade com os dados atualizados do DTO
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.nome());

        //Salva e retorna
        return salvarClienteComCep(cliente, dto.cep());
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id); //Lança 404 se não existir
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO converterParaResponse(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getEndereco());
    }

    private ClienteResponseDTO salvarClienteComCep(Cliente cliente, String cep) {
        Endereco endereco = enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return converterParaResponse(clienteSalvo);
    }
}
