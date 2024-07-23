package com.fourcamp.NutriPlan.service;

import com.fourcamp.NutriPlan.dao.impl.JdbcTemplateDaoImpl;
import com.fourcamp.NutriPlan.dto.JwtData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
public class ObjetivoService {

    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDao;

    public double calcularGETSalvar(JwtData cliente, String categoriaAtividade) {
        double tmb = calcularTaxaMetabolica(cliente);
        double get = calcularGET(tmb, categoriaAtividade);

        jdbcTemplateDao.salvarTMBGET(cliente.getEmail(), tmb, get);

        return get;
    }

    private double calcularTaxaMetabolica(JwtData cliente) {
        double calculo = 0;

        LocalDate dataNascimento = convertToLocalDate(cliente.getDataNascimento());
        int idade = calcularIdade(dataNascimento);

        if (Objects.equals(cliente.getGenero(), "M")) {
            calculo = 66.5 + (13.75 * cliente.getPeso()) + (5.003 * (cliente.getAltura() * 100)) - (6.75 * idade);
        } else if (Objects.equals(cliente.getGenero(), "F")) {
            calculo = 655.1 + (9.563 * cliente.getPeso()) + (1.850 * cliente.getAltura()) - (4.676 * idade);
        }

        return calculo;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private int calcularIdade(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        return Period.between(dataNascimento, hoje).getYears();
    }

    private double calcularGET(double tmb, String categoriaAtividade) {
        double fatorAtividade;

        switch (categoriaAtividade) {
            case "nao_muito_ativo":
                fatorAtividade = 1.2;
                break;
            case "levemente_ativo":
                fatorAtividade = 1.375;
                break;
            case "ativo":
                fatorAtividade = 1.55;
                break;
            case "bastante_ativo":
                fatorAtividade = 1.725;
                break;
            default:
                throw new UnsupportedOperationException("Categoria de atividade desconhecida: " + categoriaAtividade);
        }

        return tmb * fatorAtividade;
    }
}