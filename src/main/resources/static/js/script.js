document.addEventListener('DOMContentLoaded', () => {

    const navLinks = document.querySelectorAll('.nav-item');
    const sections = document.querySelectorAll('.page-section');

    // --- Elementos da Loja ---
    const productList = document.getElementById('product-list');
    const productDetails = document.getElementById('product-details');
    const backBtn = document.getElementById('back-btn');
    const sendBtn = document.getElementById('send-btn');
    let numeroPedido = null;
    let products = [];


    const originInput = document.getElementById('origin-input');
    const destInput = document.getElementById('dest-input');
    const calcBtn = document.getElementById('calc-route-btn');
    const mapSvg = document.getElementById('network-map');

   const mapNodes = {
     'A': { x: 21,  y: 183 },
     'B': { x: 216, y: 335 },
     'C': { x: 221, y: 20 },
     'D': { x: 456, y: 177 },
     'E': { x: 415, y: 333 },
     'F': { x: 580, y: 20 },
     'G': { x: 214, y: 176 },
     'H': { x: 306, y: 128 },
   };


    let mapConnections = [
    // A
    { from: 'A', to: 'B', dist: 5 },
    { from: 'A', to: 'C', dist: 5 },
    { from: 'A', to: 'G', dist: 3 },

    // B
    { from: 'B', to: 'E', dist: 6 },
    { from: 'B', to: 'H', dist: 7 },

    // C
    { from: 'C', to: 'F', dist: 12 },
    { from: 'C', to: 'G', dist: 4 },

    // D
    { from: 'D', to: 'E', dist: 5 },
    { from: 'D', to: 'F', dist: 6 },
    { from: 'D', to: 'H', dist: 4 },

    // G
    { from: 'G', to: 'H', dist: 2 }
    ];



    // ============================================
    // 1. NAVEGAÇÃO (SPA)
    // ============================================
    function navigateTo(targetId) {
        // Reseta detalhes do produto se estiver aberto
        if (!productDetails.classList.contains('hidden')) {
            productDetails.classList.add('hidden');
            productList.classList.remove('hidden'); // Garante que a lista volte ao fundo se sair
        }

        // Troca de seção
        sections.forEach(sec => {
            if(sec.id === targetId) sec.classList.remove('hidden');
            else sec.classList.add('hidden');
        });

        // Atualiza Navbar
        navLinks.forEach(link => {
            if(link.dataset.target === targetId) link.classList.add('active');
            else link.classList.remove('active');
        });

        window.scrollTo(0,0);
    }

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navigateTo(e.target.dataset.target);
        });
    });

    // ============================================
    // 2. LÓGICA DA LOJA
    // ============================================
    async function carregarProdutos(){
        // MOCK PRODUTOS (Se sua API estiver off, isso mantém a tela bonita)
        const resposta = await fetch("http://localhost:8080/produtos");
        products = await resposta.json();

        // Dummy data para teste visual imediato:
//        products = [
//            { id: 1, name: 'Caixa Térmica Pro', price: 150.00, srcImg: 'https://via.placeholder.com/300?text=Caixa', description: 'Ideal para congelados.' },
//            { id: 2, name: 'Pallet Padrão BR', price: 45.90, srcImg: 'https://via.placeholder.com/300?text=Pallet', description: 'Madeira reforçada.' },
//            { id: 3, name: 'Filme Stretch', price: 29.90, srcImg: 'https://via.placeholder.com/300?text=Stretch', description: 'Proteção máxima.' }
//        ];

        productList.innerHTML = "";
        products.forEach(product => {
            const div = document.createElement("div");
            div.classList.add("product-card");
            div.dataset.id = product.id;
            div.innerHTML = `
              <img src="${product.srcImg}" alt="${product.name}">
              <h2>${product.name}</h2>
              <p class="price">${product.price.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</p>
              <button class="view-details-btn">Ver Detalhes</button>
            `;
            productList.appendChild(div);
        });
    }

    carregarProdutos();

    productList.addEventListener('click', (event) => {
        if (event.target.classList.contains('view-details-btn')) {
            const card = event.target.closest('.product-card');
            const productId = card.dataset.id;
            const product = products.find(p => p.id == productId);
            numeroPedido = productId;

            document.getElementById('product-title').textContent = product.name;
            document.getElementById('product-description').textContent = product.description;
            document.getElementById('product-price').textContent = product.price.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
            document.getElementById('product-image').src = product.srcImg;

            productList.classList.add('hidden');
            productDetails.classList.remove('hidden');
            window.scrollTo(0, 0);
        }
    });

    backBtn.addEventListener('click', () => {
        productDetails.classList.add('hidden');
        productList.classList.remove('hidden');
    });

    // BOTÃO FAZER ENVIO (Com requisição REAL e redirecionamento)
    sendBtn.addEventListener('click', () => {
        if (!numeroPedido) {
            Swal.fire({
                title: 'Atenção!',
                text: 'Selecione um produto antes de enviar o pedido.',
                icon: 'warning',
                confirmButtonText: 'Ok'
            });
            return;
        }

        fetch(`http://localhost:8080/pedido/${numeroPedido}`, {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
            return response.text();
        })
        .then(numeroRetornado => {
            Swal.fire({
                title: 'Sucesso!',
                text: `Pedido nº ${numeroRetornado} criado. Vamos calcular o frete?`,
                icon: 'success',
                confirmButtonText: 'Ir para Mapa',
                confirmButtonColor: '#3498DB'
            }).then((result) => {
                if (result.isConfirmed) {
                    navigateTo('route-section'); // Redireciona igual ao mock
                }
            });
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            Swal.fire({
                title: 'Erro!',
                text: 'Não foi possível processar o pedido.',
                icon: 'error',
                confirmButtonText: 'Ok',
                confirmButtonColor: '#E74C3C'
            });
        });
    });

    // ============================================
    // 3. LÓGICA DE ROTAS E MAPA
    // ============================================

    // Inicializa selects
    function initMapControls() {
        const points = Object.keys(mapNodes);
        const optionsHtml = points.map(p => `<option value="${p}">${p}</option>`).join('');
        originInput.innerHTML = optionsHtml;
        destInput.innerHTML = optionsHtml;
        // Padrão
        originInput.value = 'A';
        destInput.value = 'F';

        renderMap(); // Desenha o mapa inicial
        renderAdminList(); // Preenche lista de admin
    }

    // Função para desenhar o SVG do Mapa
    function renderMap(activePath = []) {
        mapSvg.innerHTML = ''; // Limpa

        // 1. Desenha Linhas (Conexões)
        mapConnections.forEach(conn => {
            const p1 = mapNodes[conn.from];
            const p2 = mapNodes[conn.to];

            // Verifica se essa linha faz parte da rota ativa
            let isActive = false;
            for(let i=0; i < activePath.length -1; i++){
                if( (activePath[i] === conn.from && activePath[i+1] === conn.to) ||
                    (activePath[i] === conn.to && activePath[i+1] === conn.from) ){
                    isActive = true;
                }
            }

            const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
            line.setAttribute("x1", p1.x);
            line.setAttribute("y1", p1.y);
            line.setAttribute("x2", p2.x);
            line.setAttribute("y2", p2.y);
            line.setAttribute("class", `map-line ${isActive ? 'active-route' : ''}`);
            mapSvg.appendChild(line);

            // Texto da distância (opcional)
            const midX = (p1.x + p2.x) / 2;
            const midY = (p1.y + p2.y) / 2;
            const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
            text.setAttribute("x", midX);
            text.setAttribute("y", midY - 5);
            text.setAttribute("class", "map-text");
            text.textContent = `${conn.dist}km`;
            mapSvg.appendChild(text);
        });

        // 2. Desenha Nós (Cidades)
        Object.keys(mapNodes).forEach(key => {
            const pos = mapNodes[key];
            const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
            circle.setAttribute("cx", pos.x);
            circle.setAttribute("cy", pos.y);
            circle.setAttribute("r", 15);
            circle.setAttribute("class", `map-node ${activePath.includes(key) ? 'highlight' : ''}`);

            const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
            text.setAttribute("x", pos.x);
            text.setAttribute("y", pos.y + 1); // ajuste visual
            text.setAttribute("text-anchor", "middle");
            text.setAttribute("dominant-baseline", "middle");
            text.setAttribute("fill", activePath.includes(key) ? "white" : "#333");
            text.setAttribute("font-weight", "bold");
            text.textContent = key;

            mapSvg.appendChild(circle);
            mapSvg.appendChild(text);
        });
    }

    // Botão Calcular Rota
    calcBtn.addEventListener('click', () => {
        const origin = originInput.value;
        const dest = destInput.value;

        if (origin === dest) {
            Swal.fire('Erro', 'Origem e destino devem ser diferentes', 'error');
            return;
        }

        // Chamada REAL à API
        fetch(`http://localhost:8080/rotas/analise/${origin}/${dest}`)
            .then(response => {
                if (!response.ok) throw new Error(`Erro HTTP ${response.status}`);
                return response.json();
            })
            .then(data => {

                // Exemplo do JSON esperado:
                // {
                //   "caminho": ["A","G","H"],
                //   "pesoTotalCaminho": 5,
                //   ...
                // }

                const path = data.caminho;                // ["A","G","H"]
                const dist = data.pesoTotalCaminho;       // 5
                const weight = data.pesoTotalGrafo;       // 118 (ou outro que queira exibir)

                // Atualiza Interface
                document.getElementById('route-stats').classList.remove('hidden');
                document.getElementById('stat-distance').textContent = `${dist} km`;
                document.getElementById('stat-weight').textContent = `${weight} kg`;
                document.getElementById('stat-path').textContent = path.join(' ➝ ');

                // Desenha caminho no mapa
                renderMap(path);

                Swal.fire({
                    title: 'Rota pronta!',
                    text: `Melhor caminho calculado de ${origin} até ${dest}.`,
                    icon: 'success',
                    confirmButtonColor: '#3498DB'
                });
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Erro', 'Não foi possível calcular a rota.', 'error');
            });
    });



    // ============================================
    // 4. ADMIN (GERENCIAR ROTAS)
    // ============================================
    const routesList = document.getElementById('routes-list');

    function renderAdminList() {
        routesList.innerHTML = '';
        mapConnections.forEach((conn, index) => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span><b>${conn.from}</b> ➝ <b>${conn.to}</b> (${conn.dist}km)</span>
                <span class="delete-route" data-index="${index}"><i class="fas fa-trash"></i></span>
            `;
            routesList.appendChild(li);
        });

        // Adiciona evento de deletar
        document.querySelectorAll('.delete-route').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const idx = e.currentTarget.dataset.index;
                mapConnections.splice(idx, 1);
                renderAdminList();
                renderMap(); // Atualiza o mapa em tempo real
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: 'Rota removida', showConfirmButton: false, timer: 1500
                });
            });
        });
    }

    document.getElementById('add-route-btn').addEventListener('click', () => {
        const pA = document.getElementById('new-point-a').value.toUpperCase();
        const pB = document.getElementById('new-point-b').value.toUpperCase();
        const dist = parseInt(document.getElementById('new-distance').value);

        if(pA && pB && dist) {
            // Adiciona nova conexão visual
            mapConnections.push({ from: pA, to: pB, dist: dist });

            // Se o nó não existe visualmente, cria uma posição aleatória (apenas para o demo)
            if(!mapNodes[pA]) mapNodes[pA] = { x: Math.random()*500, y: Math.random()*300 };
            if(!mapNodes[pB]) mapNodes[pB] = { x: Math.random()*500, y: Math.random()*300 };

            renderAdminList();
            renderMap();
            initMapControls(); // Atualiza selects

            // Limpa inputs
            document.getElementById('new-point-a').value = '';
            document.getElementById('new-point-b').value = '';
            document.getElementById('new-distance').value = '';

            Swal.fire('Sucesso', 'Nova rota adicionada à malha', 'success');
        } else {
            Swal.fire('Erro', 'Preencha todos os campos', 'warning');
        }
    });

    // Inicialização
    initMapControls();
});