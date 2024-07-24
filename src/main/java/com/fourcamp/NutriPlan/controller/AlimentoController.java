package com.fourcamp.NutriPlan.controller;

import com.fourcamp.NutriPlan.dto.AlimentoDto;
import com.fourcamp.NutriPlan.model.Alimento;
import com.fourcamp.NutriPlan.service.AlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlimentoController {

    @Autowired
    private AlimentoService alimentoService;

    @PostMapping("/criar-alimento")
    public ResponseEntity<String> criarAlimento(@RequestBody AlimentoDto alimento) {
        String mensagem = alimentoService.criarAlimento(alimento.getKcal(), alimento.getCarboidrato(), alimento.getProteina(), alimento.getGordura(), alimento.getQuantidade(), alimento.getNome());

        return ResponseEntity.ok().body(mensagem);
    }
}
