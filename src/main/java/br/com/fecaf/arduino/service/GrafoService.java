package br.com.fecaf.arduino.service;

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
        int start = origem.charAt(0) - 'A';
        int end = destino.charAt(0) - 'A';

        List<Integer> caminho = dijkstra(start, end);

        return caminho.stream()
                .map(i -> String.valueOf((char) ('A' + i)))
                .toList();
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
}

