package br.com.fecaf.arduino.service;

import br.com.fecaf.arduino.exception.GrafoErrorException;
import br.com.fecaf.arduino.model.AnaliseRota;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GrafoService {

    // Matriz de rotas
    private final List<List<Integer>> rotas = List.of(
            List.of(0, 5, 5, 0, 0, 0, 3, 0),  // A
            List.of(5, 0, 0, 0, 6, 0, 0, 7),  // B
            List.of(5, 0, 0, 0, 0, 12, 4, 0), // C
            List.of(0, 0, 0, 0, 5, 6, 0, 4),  // D
            List.of(0, 6, 0, 5, 0, 0, 0, 0),  // E
            List.of(0, 0, 12, 6, 0, 0, 0, 0), // F
            List.of(3, 0, 4, 0, 0, 0, 0, 2),  // G
            List.of(0, 7, 0, 4, 0, 0, 2, 0)   // H
    );

    public List<String> calcularMenorRota(String origem, String destino) {

        if (origem == null || destino == null ||
                origem.length() != 1 || destino.length() != 1 ||
                origem.charAt(0) < 'A' || origem.charAt(0) > 'H' ||
                destino.charAt(0) < 'A' || destino.charAt(0) > 'H') {
            throw new GrafoErrorException("Invalid origin or destination. Use letters A through H.");
        }

        try {
            int start = origem.charAt(0) - 'A';
            int end = destino.charAt(0) - 'A';

            List<Integer> caminho = dijkstra(start, end);

            return caminho.stream()
                    .map(i -> String.valueOf((char) ('A' + i)))
                    .toList();
        } catch (RuntimeException e) {
            throw new GrafoErrorException(e.getMessage());
        }
    }

    private List<Integer> dijkstra(int origem, int destino) {
        int n = rotas.size();

        int[] dist = new int[n];
        boolean[] visitado = new boolean[n];
        int[] anterior = new int[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(anterior, -1);
        dist[origem] = 0;

        for (int i = 0; i < n; i++) {
            // Escolhe o nó ainda não visitado com menor distância
            int u = -1;
            int menor = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (!visitado[j] && dist[j] < menor) {
                    menor = dist[j];
                    u = j;
                }
            }

            // Acabou
            if (u == -1) break;
            visitado[u] = true;

            // Relaxa vizinhos
            for (int v = 0; v < n; v++) {
                int peso = rotas.get(u).get(v);
                if (peso > 0 && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                    anterior[v] = u;
                }
            }
        }

        // Reconstruir caminho
        List<Integer> caminho = new ArrayList<>();
        for (int atual = destino; atual != -1; atual = anterior[atual]) {
            caminho.add(atual);
        }

        Collections.reverse(caminho);
        return caminho;
    }

    public AnaliseRota analisar(String origem, String destino) {

        int start = origem.charAt(0) - 'A';
        int end = destino.charAt(0) - 'A';

        // --- 1. Dijkstra ---
        List<Integer> caminhoIdx = dijkstra(start, end);

        List<String> caminho = caminhoIdx.stream()
                .map(i -> String.valueOf((char) ('A' + i)))
                .toList();

        // --- 2. Métricas do menor caminho ---
        int pesoTotalCaminho = 0;
        int menorPesoAresta = Integer.MAX_VALUE;
        int maiorPesoAresta = Integer.MIN_VALUE;

        for (int i = 0; i < caminhoIdx.size() - 1; i++) {
            int u = caminhoIdx.get(i);
            int v = caminhoIdx.get(i + 1);
            int peso = rotas.get(u).get(v);

            pesoTotalCaminho += peso;
            menorPesoAresta = Math.min(menorPesoAresta, peso);
            maiorPesoAresta = Math.max(maiorPesoAresta, peso);
        }

        int etapas = caminhoIdx.size();

        // --- 3. CÁLCULO: TODAS AS ROTAS ENTRE ORIGEM → DESTINO -----
        List<List<Integer>> todasRotas = new ArrayList<>();
        boolean[] visitado = new boolean[rotas.size()];

        dfs(start, end, visitado, new ArrayList<>(), todasRotas);

        int totalRotasPossiveis = todasRotas.size();

        Integer menorRota = null;
        Integer maiorRota = null;

        for (List<Integer> r : todasRotas) {
            int peso = calcularPeso(r);

            if (menorRota == null || peso < menorRota)
                menorRota = peso;

            if (maiorRota == null || peso > maiorRota)
                maiorRota = peso;
        }

        Integer diferenca = (menorRota != null && maiorRota != null)
                ? (maiorRota - menorRota)
                : null;


        // --- 4. Métricas globais do grafo ---
        int totalRotas = 0;
        int pesoTotalGrafo = 0;
        int menorRotaExistente = Integer.MAX_VALUE;
        int maiorRotaExistente = Integer.MIN_VALUE;

        for (int i = 0; i < rotas.size(); i++) {
            for (int j = 0; j < rotas.get(i).size(); j++) {
                int peso = rotas.get(i).get(j);

                if (peso > 0) {
                    totalRotas++;
                    pesoTotalGrafo += peso;

                    menorRotaExistente = Math.min(menorRotaExistente, peso);
                    maiorRotaExistente = Math.max(maiorRotaExistente, peso);
                }
            }
        }

        return new AnaliseRota(
                caminho,
                pesoTotalCaminho,
                menorPesoAresta,
                maiorPesoAresta,
                etapas,
                totalRotas,
                pesoTotalGrafo,
                menorRotaExistente,
                maiorRotaExistente,
                totalRotasPossiveis,
                menorRota,
                maiorRota,
                diferenca
        );
    }

    private void dfs(int atual, int destino, boolean[] visitado, List<Integer> caminho, List<List<Integer>> resultado) {
        visitado[atual] = true;
        caminho.add(atual);

        if (atual == destino) {
            resultado.add(new ArrayList<>(caminho));
        } else {
            for (int i = 0; i < rotas.size(); i++) {
                if (rotas.get(atual).get(i) > 0 && !visitado[i]) {
                    dfs(i, destino, visitado, caminho, resultado);
                }
            }
        }

        caminho.remove(caminho.size() - 1);
        visitado[atual] = false;
    }

    private int calcularPeso(List<Integer> rota) {
        int peso = 0;
        for (int i = 0; i < rota.size() - 1; i++) {
            peso += rotas.get(rota.get(i)).get(rota.get(i + 1));
        }
        return peso;
    }



}

