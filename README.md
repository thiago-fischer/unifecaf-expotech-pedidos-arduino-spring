# 🏭 Mini Sistema Logístico com Arduino + Spring Boot

### **Separação Automatizada de Pacotes & Otimizador de Rotas**

Este projeto foi desenvolvido como parte das disciplinas do curso de Engenharia da Computação, com o objetivo de integrar conceitos de **programação**, **eletrônica**, **algoritmos**, **estrutura de dados**, **pesquisa operacional** e até princípios físicos aplicados ao funcionamento de sistemas eletromecânicos.

Criamos um **sistema logístico simplificado**, composto pelas seguintes partes principais:

---

## 🚀 Funcionalidades Principais

### 🔹 1. **Separação Automática de Pacotes via Arduino**

O front-end envia ao sistema um número de pedido.
O Spring Boot recebe essa requisição e **comunica-se com o Arduino via porta serial**, enviando o número correspondente.
O Arduino então ativa um **braço mecânico com servomotores**, responsável por mover e separar o pacote solicitado.

Fluxo resumido:

1. Usuário seleciona o produto no front.
2. O front envia `/pedido/{id}` para o back-end.
3. O Spring Boot repassa esse comando pela porta serial **COM5**.
4. O Arduino interpreta o valor e movimenta o braço.

---

### 🔹 2. **Otimizador de Rotas com Algoritmo de Dijkstra**

O usuário escolhe um **ponto de origem** e **destino** entre A e H.
O sistema calcula automaticamente o menor caminho entre os pontos usando **Dijkstra**, baseado numa matriz fixa de pesos.

Exemplo de uso:

```
/rotas/A/F  →  [A, C, F]
```

Tudo pronto para simular otimização logística real.

---

### 🔹 3. **Gerenciamento de Produtos**

O sistema possui uma entidade `Produto`, permitindo que o front-end carregue:

* nome
* descrição
* imagem
* preço

Perfeitinho para exibir no catálogo da lojinha.

---

## 🧩 Arquitetura Geral

### Backend – **Spring Boot**

Principais componentes:

| Componente                             | Descrição                                                                                   |
| -------------------------------------- | ------------------------------------------------------------------------------------------- |
| `ArduinoSerial`                        | Garante a comunicação física via porta serial (COM5). Abre, envia comandos e fecha conexão. |
| Controllers                            | Tratam rotas como `/pedido`, `/produtos` e `/rotas`.                                        |
| `ProdutoService` & `ProdutoRepository` | CRUD simples de produtos usando Spring Data JPA.                                            |
| `GrafoService`                         | Implementa o algoritmo de Dijkstra para otimizar rotas.                                     |
| `GlobalHandlerException`               | Trata erros de forma elegante e padronizada.                                                |

---

### Front-end – **HTML + CSS + JS**

* Catálogo de produtos carregado direto da API.
* Tela de detalhes com imagem, descrição e botão "Fazer Envio".
* Envio do pedido para o backend com `fetch`.
* SweetAlert2 para alertas bonitos.
* Consumindo endpoints REST e exibindo dados dinamicamente.

---

### Hardware – **Arduino + Servomotores**

* Porta serial configurada a **9600 baud**.
* Servos responsáveis pelo movimento do braço mecânico.
* Interpretação do número enviado e execução da ação equivalente.

---

## 🎓 Conteúdos Aplicados das Disciplinas

### 🧠 **1. Programação Orientada a Objetos**

* Controllers organizados por responsabilidade (`ProdutoController`, `PedidoController`, etc)
* Services separando lógica de negócio
* Repositories aplicando o padrão Repository
* Classes coesas e bem distribuídas (Single Responsibility)
* Injeção de dependência do Spring

### 📚 **2. Estrutura de Dados e Algoritmos**

* Implementação completa do **algoritmo de Dijkstra**
* Utilização de:

  * Listas
  * Matrizes de adjacência
  * Arrays de distâncias
  * Reconstrução de caminhos

Tudo isso aplicado no **otimizador de rotas**.

### 🔌 **3. Circuitos Elétricos Digitais e Analógicos**

* Controle de servomotores através de sinais PWM
* Lógica digital para interpretar comandos vindos da serial
* Integração entre eletrônica (Arduino) e software (Java)

### 📊 **4. Pesquisa Operacional**

* Problema clássico de otimização de caminhos
* Uso direto de técnicas de **grafos e pesos**
* Cálculo do menor custo entre vértices

Conceitos ensinados em PO aplicados de forma prática no sistema logístico.

### ⚙️ **5. Física Aplicada à Computação**

* Funcionamento dos servos (torque, ângulo, velocidade)
* Entendimento de sinais elétricos, corrente e alimentação dos motores
* Comportamento mecânico do braço robótico

---

## 🛠️ Tecnologias Utilizadas

### Backend

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Comunicação Serial (jSerialComm)

### Front-end

* HTML5 / CSS3
* JavaScript
* SweetAlert2

### Hardware

* Arduino UNO
* Servomotores
* Comunicação Serial USB

---

## 🧪 Como Rodar o Projeto

### 1. Backend

```
mvn spring-boot:run
```

### 2. Front-end

Apenas abra o `index.html` ou use o servidor estático do próprio Spring.

### 3. Arduino

* Carregar o código equivalente na IDE do Arduino
* Certificar-se de que a porta usada é **COM5**

---

## 📌 Conclusão

Este projeto integra **software + hardware** de forma completa, simulando parte real de um sistema logístico moderno.
Do front-end ao Arduino, passando por algoritmos de roteirização, ele demonstra como diferentes áreas da engenharia se conectam para resolver problemas do mundo real.
