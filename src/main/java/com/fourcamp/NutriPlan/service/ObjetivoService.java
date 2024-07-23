package com.fourcamp.NutriPlan.service;

import com.fourcamp.NutriPlan.dao.impl.JdbcTemplateDaoImpl;
import com.fourcamp.NutriPlan.dto.ClienteDto;
import com.fourcamp.NutriPlan.dto.JwtData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class ObjetivoService {

//    public double calcularTaxaMetabolica(ClienteDto cliente) {
//        double calculo = 0;
//
//        LocalDate dataNascimento = convertToLocalDate(cliente.getDataNascimento());
//        int idade = calcularIdade(dataNascimento);
//
//        if (Objects.equals(cliente.getGenero(), "M")) {
//            calculo = 66.5 + (13.75 * cliente.getPeso()) + (5.003 * cliente.getAltura()) - (6.75 * idade);
//        } else if (Objects.equals(cliente.getGenero(), "F")) {
//            calculo = 655.1 + (9.563 * cliente.getPeso()) + (1.850 * cliente.getAltura()) - (4.676 * idade);
//        }
//
//        return calculo;
//    }
//
//    private LocalDate convertToLocalDate(Date date) {
//        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    }
//
//    private int calcularIdade(LocalDate dataNascimento) {
//        LocalDate hoje = LocalDate.now();
//        return Period.between(dataNascimento, hoje).getYears();
//    }

    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDao;

    public double calcularETMSalvar(JwtData cliente) {

        double tmb = calcularTaxaMetabolica(cliente);
        jdbcTemplateDao.salvarTMB(cliente.getEmail(), tmb);

        return tmb;
    }

    private double calcularTaxaMetabolica(JwtData cliente) {
        double calculo = 0;

        LocalDate dataNascimento = convertToLocalDate(cliente.getDataNascimento());
        int idade = calcularIdade(dataNascimento);

        if (Objects.equals(cliente.getGenero(), "M")) {
            calculo = 66.5 + (13.75 * cliente.getPeso()) + (5.003 * cliente.getAltura()) - (6.75 * idade);
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
//        return Period.between(hoje, dataNascimento).getYears();
        int anoNascimento = dataNascimento.getYear();
        int diaAtual = hoje.getYear();

        return  diaAtual-anoNascimento;

}
}
