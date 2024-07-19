package com.fourcamp.NutriPlan.service;

import com.fourcamp.NutriPlan.dao.impl.JdbcTemplateDaoImpl;
import com.fourcamp.NutriPlan.model.Cliente;
import com.fourcamp.NutriPlan.utils.Constantes;
import com.fourcamp.NutriPlan.utils.JwtConfig;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class ClienteService {

    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDaoImpl;

    public String criarCliente(String nome, String email, String genero, Double peso, Double pesoDesejado, Double altura, Date dataNascimento, String senha, String categoria, String tempoMeta){
        jdbcTemplateDaoImpl.criarCliente(nome, email, genero, peso, pesoDesejado, altura, dataNascimento, senha, categoria , tempoMeta);
        return Constantes.MSG_CRIACAO_CLIENTE_SUCESSO;
    }

    public String login(String email, String senha) {
        Cliente cliente = jdbcTemplateDaoImpl.buscarClientePorEmail(email);
        if (cliente != null && cliente.getSenha().equals(senha)) {

            Key chaveSecreta = JwtConfig.getChaveSecreta();

            String token = Jwts.builder().claim("nome", cliente.getNome()).claim("email", cliente.getEmail())
                    .claim("genero", cliente.getGenero())
                    .claim("peso", cliente.getPeso()).claim("peso_desejado", cliente.getPesoDesejado())
                    .claim("altura",cliente.getAltura())
                    .claim("data_nascimento", cliente.getDataNascimento()).claim("senha", cliente.getSenha())
                    .claim("categoria", cliente.getCategoria()) .claim("tempo_meta", cliente.getTempoMeta())

                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(chaveSecreta).compact();
            return token;
        } else { 
            return null;
        }
    }

    public String alterarPeso(String email, double novoPeso) {
        Cliente cliente = jdbcTemplateDaoImpl.buscarClientePorEmail(email);

        cliente.setPeso(novoPeso);
        jdbcTemplateDaoImpl.atualizarPesoCliente(email, novoPeso);
        return "Peso alterado com sucesso";
    }

//    public double visualizarGeb(String email) {
//        return jdbcTemplateDaoImpl.viewSaldo(email);
//    }
}
