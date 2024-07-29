package com.fourcamp.NutriPlan.service;

import com.fourcamp.NutriPlan.dao.impl.JdbcTemplateDaoImpl;
import com.fourcamp.NutriPlan.dto.JwtData;
import com.fourcamp.NutriPlan.dto.MacrosDto;
import com.fourcamp.NutriPlan.model.CategoriaAtividadeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service
public class ObjetivoService {

    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDao;

    public double calcularGETSalvar(JwtData jwtData, String categoriaAtividade) {
        double tmb = calcularTaxaMetabolica(jwtData);
        double get = calcularGET(tmb, categoriaAtividade);
        String categoria = jwtData.getCategoria().trim().toUpperCase(Locale.ROOT);
        String tempoCategoria = jwtData.getTempoMeta().trim().toUpperCase(Locale.ROOT);

        switch (categoria) {
            case "PERDER PESO":
                if ("RAPIDO".equals(tempoCategoria)) {
                    get -= 1000;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                } else if ("MEDIO".equals(tempoCategoria)) {
                    get -= 600;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                } else if ("LONGO PRAZO".equals(tempoCategoria)) {
                    get -= 400;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                }
                break;
            case "MANUTENCAO":
                jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                break;
            case "HIPERTROFIA":
                if ("RAPIDO".equals(tempoCategoria)) {
                    get += 800;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                } else if ("MEDIO".equals(tempoCategoria)) {
                    get += 500;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                } else if ("LONGO PRAZO".equals(tempoCategoria)) {
                    get += 300;
                    jdbcTemplateDao.salvarTMBGET(jwtData.getEmail(), tmb, get);
                }
                break;
            default:
                throw new UnsupportedOperationException("Categoria de atividade desconhecida: " + categoria);
        }

        return get;
    }

    private double calcularTaxaMetabolica(JwtData cliente) {
        double calculo = 0;

        LocalDate dataNascimento = convertToLocalDate(cliente.getDataNascimento());
        int idade = calcularIdade(dataNascimento);

        if (Objects.equals(cliente.getGenero(), "M")) {
            calculo = 66.5 + (13.75 * cliente.getPeso()) + (5.003 * (cliente.getAltura() * 100)) - (6.75 * idade);
        } else if (Objects.equals(cliente.getGenero(), "F")) {
            calculo = 655.1 + (9.563 * cliente.getPeso()) + (1.850 * (cliente.getAltura() * 100)) - (4.676 * idade);
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

//    public String acessarPlano(JwtData jwtData, MacrosDto macros) {
//        double gastoEnergetico = calcularGETSalvar(jwtData);
//
//        String categoria = jwtData.getCategoria().trim().toUpperCase(Locale.ROOT);
//        String tempoCategoria = jwtData.getTempoMeta().trim().toUpperCase(Locale.ROOT);
//
//        double caloriasNecessarias = gastoEnergetico;
//        double proteinas = 0;
//        double carboidratos = 0;
//        double gorduras = jwtData.getPeso();
//
//        switch (categoria) {
//            case "PERDER PESO":
//                switch (tempoCategoria) {
//                    case "RAPIDO":
//                        proteinas = 2.5 * jwtData.getPeso();
//                        break;
//                    case "MEDIO":
//                        proteinas = 2.2 * jwtData.getPeso();
//                        break;
//                    case "LONGO PRAZO":
//                        proteinas = 2.0 * jwtData.getPeso();
//                        break;
//                    default:
//                        throw new UnsupportedOperationException("Tempo de meta desconhecido: " + tempoCategoria);
//                }
//                break;
//            case "MANUTENCAO":
//            case "HIPERTROFIA":
//                proteinas = calcularProteina(jwtData);
//                break;
//            default:
//                throw new UnsupportedOperationException("Categoria de atividade desconhecida: " + categoria);
//        }
//
//        carboidratos = calcularCarboidratos(gastoEnergetico, proteinas, gorduras);
//
//        String planoNutricional = "Seu plano nutricional: \n" +
//                "Calorias necessárias: " + caloriasNecessarias + " kcal\n" +
//                "Proteínas: " + proteinas + " g\n" +
//                "Carboidratos: " + carboidratos + " g\n" +
//                "Gorduras: " + gorduras + " g";
//
//        return planoNutricional;
//    }
//
//    private double calcularProteina(JwtData cliente) {
//        return 2.0 * cliente.getPeso();
//    }
//
//    private double calcularCarboidratos(double gastoEnergetico, double proteinas, double gorduras) {
//        double proteinasEmCalorias = proteinas * 4;
//        double gordurasEmCalorias = gorduras * 9;
//        return (gastoEnergetico - proteinasEmCalorias - gordurasEmCalorias) / 4;
//    }

}