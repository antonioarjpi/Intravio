package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.CredenciaisDTO;
import com.intraviologistica.intravio.dto.TokenDTO;
import com.intraviologistica.intravio.dto.UsuarioDTO;
import com.intraviologistica.intravio.dto.input.AlterarSenhaDTO;
import com.intraviologistica.intravio.dto.input.UsuarioInputDTO;
import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.repository.UsuarioRepository;
import com.intraviologistica.intravio.security.JwtService;
import com.intraviologistica.intravio.service.exceptions.AuthenticateErrorException;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public UsuarioDTO cadastrarUsuario(UsuarioInputDTO dto) {
        Usuario usuario = toEntity(dto);

        validaEmail(dto);

        usuario.setId(getUuid()); // Atribui uma STRING para ID
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioSalvo);
    }

    @Transactional
    public Usuario encontraUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado: " + email));
    }

    // Método para autenticar no sistema
    @Transactional
    public TokenDTO fazerLogin(CredenciaisDTO dto) {
        Usuario usuario = encontraUsuarioPorEmail(dto.getEmail()); // Busca de usuário por e-mail. Caso não seja encontrado, será lançada uma exceção

        validaSenhas(dto, usuario);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()));

        String jwtToken = jwtService.generateToken(usuario); // Gera o Token

        return new TokenDTO(jwtToken);
    }

    // Lista todos usuários retornando DTO
    @Transactional
    public List<UsuarioDTO> listarUsuários() {
        return usuarioRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Usuario::getPrimeiroNome))
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO encontraUsuarioPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return new UsuarioDTO(usuario);
    }

    @Transactional
    public Usuario atualizaUsuario(String id, UsuarioInputDTO dto) {
        Usuario usuario = findById(id);
        usuario.setId(id);
        usuario.setPrimeiroNome(dto.getPrimeiroNome());
        usuario.setSegundoNome(dto.getSegundoNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletaUsuario(String id) {
        findById(id);
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void alterarSenha(String id, AlterarSenhaDTO senhaDTO) {
        Usuario usuario = findById(id);

        boolean matches = passwordEncoder.matches(senhaDTO.getSenhaAtual(), usuario.getSenha());
        if (!matches) {
            throw new RuleOfBusinessException("A senha atual inserida está incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(senhaDTO.getSenhaNova()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario findById(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não localizado"));
    }

    // Método para verificar se um e-mail já existe. Caso exista, será lançada uma exceção
    private void validaEmail(UsuarioInputDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuleOfBusinessException("Erro ao cadastrar novo usuário. O e-mail já existe no sistema. Por favor, verifique as informações e tente novamente.");
        }
    }

    private void validaSenhas(CredenciaisDTO dto, Usuario usuario) {
        boolean matches = passwordEncoder.matches(dto.getSenha(), usuario.getSenha()); // Verifica se a senha criptografada armazenada é igual à senha enviada no DTO e retorna verdadeiro em caso afirmativo

        if (!matches) {
            throw new AuthenticateErrorException("Senha incorreta."); // Caso o matches for falso, retorna uma exceção
        }
    }

    private Usuario toEntity(UsuarioInputDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setPrimeiroNome(dto.getPrimeiroNome());
        usuario.setSegundoNome(dto.getSegundoNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return usuario;
    }
}
