package com.fourcamp.NutriPlan.service;

import com.fourcamp.NutriPlan.dao.JdbcTemplateDao;
import com.fourcamp.NutriPlan.dao.impl.JdbcTemplateDaoImpl;
import com.fourcamp.NutriPlan.dto.JwtData;
import com.fourcamp.NutriPlan.dto.MacrosDto;
import com.fourcamp.NutriPlan.dto.RefeicaoRequest;
import com.fourcamp.NutriPlan.exception.PlanoException;
import com.fourcamp.NutriPlan.model.Alimento;
import com.fourcamp.NutriPlan.model.Diario;
import com.fourcamp.NutriPlan.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
public class DiarioService {

    @Autowired
    JdbcTemplateDao jdbcTemplateDao;

    public String adicionarRefeicao(JwtData jwtData, RefeicaoRequest refeicaoRequest) {
        MacrosDto tabelaAlimento = consultarTabelaNutricional(refeicaoRequest.getAlimento());
        MacrosDto tabelaAlimentoCalculado = calcularQuantidadeAlimento(tabelaAlimento, refeicaoRequest.getQuantidade());

        MacrosDto planoAtual = new MacrosDto(
                tabelaAlimentoCalculado.getKcalTotais(),
                tabelaAlimentoCalculado.getCarboidrato(),
                tabelaAlimentoCalculado.getProteina(),
                tabelaAlimentoCalculado.getGordura()
        );

        MacrosDto planoAposAdicao = consultarPlanoCliente(jwtData, planoAtual);

        jdbcTemplateDao.salvarDiario(
                jwtData.getEmail(),
                refeicaoRequest.getAlimento(),
                refeicaoRequest.getQuantidade(),
                roundToThreeDecimalPlaces(planoAposAdicao.getKcalTotais()),
                roundToThreeDecimalPlaces(planoAposAdicao.getCarboidrato()),
                roundToThreeDecimalPlaces(planoAposAdicao.getProteina()),
                roundToThreeDecimalPlaces(planoAposAdicao.getGordura()),
                new Date()
        );

        return Constantes.MSG_ATUALIZACAO_PLANO;
    }

    public MacrosDto consultarTabelaNutricional(String nomeAlimento) {
        Alimento alimento = jdbcTemplateDao.buscarAlimentoPorNome(nomeAlimento);

        return new MacrosDto(
                alimento.getKcal(),
                alimento.getCarboidrato(),
                alimento.getProteina(),
                alimento.getGordura()
        );
    }

    public MacrosDto calcularQuantidadeAlimento(MacrosDto macro, Double quantidade) {
        macro.setKcalTotais((quantidade / 100) * macro.getKcalTotais());
        macro.setCarboidrato((quantidade / 100) * macro.getCarboidrato());
        macro.setProteina((quantidade / 100) * macro.getProteina());
        macro.setGordura((quantidade / 100) * macro.getGordura());
        return macro;
    }

    public MacrosDto consultarPlanoCliente(JwtData jwtData, MacrosDto planoAtual) {
        List<Diario> diarios = jdbcTemplateDao.buscarPlanoCliente(jwtData.getEmail());

        if (!diarios.isEmpty()) {
            Diario diario = diarios.get(0);
            planoAtual.setKcalTotais(diario.getKcal() - planoAtual.getKcalTotais());
            planoAtual.setCarboidrato(diario.getCarboidrato() - planoAtual.getCarboidrato());
            planoAtual.setProteina(diario.getProteina() - planoAtual.getProteina());
            planoAtual.setGordura(diario.getGordura() - planoAtual.getGordura());
        }else {
            throw new PlanoException();
        }

        return planoAtual;
    }

    private double roundToThreeDecimalPlaces(double value) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
