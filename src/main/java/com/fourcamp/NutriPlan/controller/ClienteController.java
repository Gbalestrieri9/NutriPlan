package com.fourcamp.NutriPlan.controller;

import com.fourcamp.NutriPlan.dto.ClienteDto;
import com.fourcamp.NutriPlan.dto.JwtData;
import com.fourcamp.NutriPlan.dto.LoginRequestDto;
import com.fourcamp.NutriPlan.dto.PesoDto;
import com.fourcamp.NutriPlan.model.Cliente;
import com.fourcamp.NutriPlan.service.ClienteService;
import com.fourcamp.NutriPlan.utils.Constantes;
import com.fourcamp.NutriPlan.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/create/account")
    public ResponseEntity<String> addCliente(@RequestBody Cliente cliente) {
        Date dataSql = new Date(cliente.getDataNascimento().getTime());
        String mensagem = clienteService.criarCliente(
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getGenero(),
                cliente.getPeso(),
                cliente.getPesoDesejado(),
                cliente.getAltura(),
                dataSql,
                cliente.getSenha(),
                cliente.getCategoria(),
                cliente.getTempoMeta()
        );

        return ResponseEntity.ok().body(mensagem);
    }

    @PostMapping("/login")
    @Operation(description = "Fazer o login da conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login com sucesso"),
            @ApiResponse (responseCode = "400", description = "Falha no login")
    })
    public String login(@RequestBody LoginRequestDto loginRequest) {
        return clienteService.login(loginRequest.getEmail(), loginRequest.getSenha());
    }


//    @GetMapping("/Geb")
//    @Operation(description = "Visualizar o gasto energetico basal do cliente logado")
//    @ApiResponses(value = {
//            @ApiResponse (responseCode = "200", description = "Retorno do Geb"),
//            @ApiResponse (responseCode = "400", description = "Falha no retorno do Geb")
//    })
//    public ResponseEntity<Double> visualizarGeb(@RequestHeader("Authorization") String token) {
//        JwtData jwtData = JwtUtils.decodeToken(token);
//
//        double saldo = clienteService.visualizarGeb(jwtData.getEmail());
//
//        return ResponseEntity.ok(saldo);
//    }

    @PostMapping("/atualizar-peso")
    public ResponseEntity<String> alterarPeso(@RequestHeader("Authorization") String token, @RequestBody PesoDto novoPeso){
        JwtData jwtData = JwtUtils.decodeToken(token);

        String mensagem = clienteService.alterarPeso(jwtData.getEmail(), novoPeso.getNovoPeso());
        return ResponseEntity.ok(mensagem);
    }

    @PostMapping("/formulario")
    public ResponseEntity<String> formularioObjetivo(@RequestHeader("Authorization") String token, @RequestBody ClienteDto cliente){
        JwtData jwtData = JwtUtils.decodeToken(token);

        String mensagem = clienteService.formularioObjetivo(jwtData.getEmail(), cliente.getCategoria(), cliente.getTempoMeta());
        return ResponseEntity.ok(mensagem);
    }

}
