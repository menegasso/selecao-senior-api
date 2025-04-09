package com.selecao.senior.api.config.data;

import com.selecao.senior.api.entity.Cidade;
import com.selecao.senior.api.entity.Endereco;
import com.selecao.senior.api.entity.Lotacao;
import com.selecao.senior.api.entity.Pessoa;
import com.selecao.senior.api.entity.ServidorEfetivo;
import com.selecao.senior.api.entity.Unidade;
import com.selecao.senior.api.entity.Role;
import com.selecao.senior.api.entity.Usuario;
import com.selecao.senior.api.repository.CidadeRepository;
import com.selecao.senior.api.repository.EnderecoRepository;
import com.selecao.senior.api.repository.LotacaoRepository;
import com.selecao.senior.api.repository.PessoaRepository;
import com.selecao.senior.api.repository.ServidorEfetivoRepository;
import com.selecao.senior.api.repository.UnidadeRepository;
import com.selecao.senior.api.repository.RoleRepository;
import com.selecao.senior.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ServidorEfetivoRepository servidorEfetivoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private LotacaoRepository lotacaoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Inserindo dados para Cidade
        if (cidadeRepository.count() == 0) {
            Cidade cidade1 = new Cidade();
            cidade1.setNome("São Paulo");
            cidade1.setUf("SP");
            cidadeRepository.save(cidade1);

            Cidade cidade2 = new Cidade();
            cidade2.setNome("Rio de Janeiro");
            cidade2.setUf("RJ");
            cidadeRepository.save(cidade2);
        }

        // Inserindo dados para Endereço (assumindo que já há cidades cadastradas)
        if (enderecoRepository.count() == 0) {
            // Recupera a primeira cidade cadastrada para exemplo
            Cidade cidade = cidadeRepository.findAll().get(0);

            Endereco endereco1 = new Endereco();
            endereco1.setTipoLogradouro("Avenida");
            endereco1.setLogradouro("Paulista");
            endereco1.setNumero(1000L);
            endereco1.setBairro("Bela Vista");
            endereco1.setCidade(cidade);
            enderecoRepository.save(endereco1);

            Endereco endereco2 = new Endereco();
            endereco2.setTipoLogradouro("Rua");
            endereco2.setLogradouro("das Laranjeiras");
            endereco2.setNumero(200L);
            endereco2.setBairro("Centro");
            // Se preferir, use outra cidade (caso haja mais de uma)
            endereco2.setCidade(cidade);
            enderecoRepository.save(endereco2);
        }

        // Inserindo dados para Pessoa
        if (pessoaRepository.count() == 0) {
            Pessoa pessoa1 = new Pessoa();
            pessoa1.setNome("João da Silva");
            pessoa1.setDataNascimento(LocalDate.of(1990, 5, 20));
            pessoa1.setSexo("Masculino");
            pessoa1.setMae("Maria da Silva");
            pessoa1.setPai("José da Silva");
            pessoaRepository.save(pessoa1);

            Pessoa pessoa2 = new Pessoa();
            pessoa2.setNome("Ana Santos");
            pessoa2.setDataNascimento(LocalDate.of(1985, 7, 15));
            pessoa2.setSexo("Feminino");
            pessoa2.setMae("Luísa Santos");
            pessoa2.setPai("Carlos Santos");
            pessoaRepository.save(pessoa2);
        }

        // Inserindo dados para Servidor Efetivo (utilizando Pessoa existente - exemplo: pessoa1)
        if (servidorEfetivoRepository.count() == 0) {
            ServidorEfetivo servidorEfetivo = new ServidorEfetivo();
            // Preenche os campos herdados de Pessoa
            servidorEfetivo.setNome("João da Silva");
            servidorEfetivo.setDataNascimento(LocalDate.of(1990, 5, 20));
            servidorEfetivo.setSexo("Masculino");
            servidorEfetivo.setMae("Maria da Silva");
            servidorEfetivo.setPai("José da Silva");
            // Campos específicos de ServidorEfetivo
            servidorEfetivo.setMatricula("MATR001");

            servidorEfetivoRepository.save(servidorEfetivo);
        }


        // Inserindo dados para Unidade
        if (unidadeRepository.count() == 0) {
            Unidade unidade1 = new Unidade();
            unidade1.setNome("Unidade Central");
            unidade1.setSigla("UC");
            unidadeRepository.save(unidade1);

            Unidade unidade2 = new Unidade();
            unidade2.setNome("Unidade Secundária");
            unidade2.setSigla("US");
            unidadeRepository.save(unidade2);
        }

        // Inserindo dados para Lotação (relacionando Pessoa e Unidade)
        if (lotacaoRepository.count() == 0) {
            Pessoa pessoa = pessoaRepository.findAll().get(0);
            Unidade unidade = unidadeRepository.findAll().get(0);
            Lotacao lotacao = new Lotacao();
            lotacao.setPessoa(pessoa);
            lotacao.setUnidade(unidade);
            lotacao.setDataLotacao(LocalDate.now());
            lotacao.setPortaria("Portaria 001");
            lotacaoRepository.save(lotacao);
        }

        // Inserindo dados para Role e Usuário
        if (usuarioRepository.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setNome("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);

            Usuario usuario = new Usuario();
            usuario.setUsername("admin");
            usuario.setSenha(passwordEncoder.encode("admin123"));
            usuario.setRoles(Collections.singletonList(roleAdmin));
            usuarioRepository.save(usuario);
        }
    }
}
