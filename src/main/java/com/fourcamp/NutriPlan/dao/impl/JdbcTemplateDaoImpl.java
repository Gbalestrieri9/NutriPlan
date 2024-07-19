package com.fourcamp.NutriPlan.dao.impl;

import com.fourcamp.NutriPlan.dao.JdbcTemplateDao;
import com.fourcamp.NutriPlan.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.List;

@Service
public class JdbcTemplateDaoImpl implements JdbcTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarCliente(String nome, String email, String genero, double peso, double pesoDesejado, double altura, Date dataNascimento, String senha, String categoria, String tempoMeta) {
        String sql = "CALL criar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {nome, email, genero, peso, pesoDesejado, altura, new java.sql.Date(dataNascimento.getTime()), senha, categoria , tempoMeta};
        int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.DATE, Types.VARCHAR , Types.NULL, Types.NULL};

        jdbcTemplate.update(sql, params, types);
    }

    public Cliente buscarClientePorEmail(String email) {
        String query = "SELECT * FROM clientes WHERE email = ?";
        @SuppressWarnings("deprecation")
        List<Cliente> clientes = jdbcTemplate.query(query, new Object[] { email }, (rs, rowNum) -> {
            return new Cliente(rs.getString("nome"), rs.getString("email"), rs.getString("genero"), rs.getDouble("peso"),
                    rs.getDouble("peso_desejado"),rs.getDouble("altura"), rs.getDate("data_nascimento"), rs.getString("senha"), rs.getString("categoria"), rs.getString("tempo_meta"));
        });

        if (!clientes.isEmpty()) {
            return clientes.get(0);
        } else {
            return null;
        }
    }

    public void atualizarPesoCliente(String email, double novoPeso) {
        String callProcedure = "CALL atualizar_peso_cliente(?, ?)";
        jdbcTemplate.update(callProcedure, email, novoPeso);
    }

    public void formularioObjetivo(String email, String categoria, String tempoMeta) {
        String callProcedure = "CALL formulario_objetivo(?, ?, ?)";
        jdbcTemplate.update(callProcedure,email,categoria,tempoMeta);
    }

//    public double viewSaldo(String email) {
//
//    }
}
