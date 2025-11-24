package br.com.fecaf.arduino.model;

import java.util.List;

public record AnaliseRota(
        List<String> caminho,
        int pesoTotalCaminho,
        int menorPesoAresta,
        int maiorPesoAresta,
        int etapas,
        int totalRotasGrafo,
        int pesoTotalGrafo,
        int menorRotaExistenteGrafo,
        int maiorRotaExistenteGrafo,
        int totalRotasPossiveisOD,
        Integer menorRotaOD,
        Integer maiorRotaOD,
        Integer diferencaMaiorMenorOD
) {}
