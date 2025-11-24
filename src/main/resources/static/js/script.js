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
        'A': { x: 50, y: 200 },
        'B': { x: 150, y: 100 },
        'C': { x: 150, y: 300 },
        'D': { x: 300, y: 100 },
        'E': { x: 300, y: 300 },
        'F': { x: 450, y: 200 },
        'G': { x: 550, y: 350 }
    };

    let mapConnections = [
        { from: 'A', to: 'B', dist: 10 },
        { from: 'A', to: 'C', dist: 15 },
        { from: 'B', to: 'D', dist: 20 },
        { from: 'C', to: 'E', dist: 10 },
        { from: 'D', to: 'F', dist: 15 },
        { from: 'E', to: 'F', dist: 10 },
        { from: 'C', to: 'B', dist: 5 },
        { from: 'F', to: 'G', dist: 25 }
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
        // const resposta = await fetch("http://localhost:8080/produtos");
        // products = await resposta.json();

        // Dummy data para teste visual imediato:
        products = [
            { id: 1, name: 'Caixa Térmica Pro', price: 150.00, srcImg: 'https://via.placeholder.com/300?text=Caixa', description: 'Ideal para congelados.' },
            { id: 2, name: 'Pallet Padrão BR', price: 45.90, srcImg: 'https://via.placeholder.com/300?text=Pallet', description: 'Madeira reforçada.' },
            { id: 3, name: 'Filme Stretch', price: 29.90, srcImg: 'https://via.placeholder.com/300?text=Stretch', description: 'Proteção máxima.' }
        ];

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

    // BOTÃO FAZER ENVIO (Com Redirecionamento)
    sendBtn.addEventListener('click', () => {
        if (!numeroPedido) return;

        // Simula Fetch do Pedido
        new Promise((resolve) => setTimeout(() => resolve("12345"), 500)) // Mock delay
        .then(numeroRetornado => {
            Swal.fire({
                title: 'Sucesso!',
                text: `Pedido nº ${numeroRetornado} criado. Vamos calcular o frete?`,
                icon: 'success',
                confirmButtonText: 'Ir para Mapa',
                confirmButtonColor: '#3498DB'
            }).then((result) => {
                if (result.isConfirmed) {
                    // REDIRECIONA PARA A TELA DE ROTAS
                    navigateTo('route-section');
                }
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

        if(origin === dest) {
            Swal.fire('Erro', 'Origem e destino devem ser diferentes', 'error');
            return;
        }

        // Simula chamada API de rota
        // fetch(`http://api/route?from=${origin}&to=${dest}`)...

        // LOGICA MOCKADA: Gera uma rota aleatória válida para demonstração
        // Na vida real, a API retornaria o array ["A", "B", "D", "F"]

        // Vamos forçar um resultado visual interessante
        let mockPath = [];
        let mockDist = 0;
        let mockWeight = 0;

        // Simulação básica para A -> F
        if(origin === 'A' && dest === 'F') {
            mockPath = ['A', 'B', 'D', 'F'];
            mockDist = 45;
            mockWeight = 120;
        } else if (origin === 'A' && dest === 'E') {
             mockPath = ['A', 'C', 'E'];
             mockDist = 25;
             mockWeight = 80;
        } else {
            // Fallback: Rota direta simples visual
            mockPath = [origin, dest];
            mockDist = Math.floor(Math.random() * 100);
            mockWeight = Math.floor(Math.random() * 500);
        }

        // Atualiza Interface
        document.getElementById('route-stats').classList.remove('hidden');
        document.getElementById('stat-distance').textContent = `${mockDist} km`;
        document.getElementById('stat-weight').textContent = `${mockWeight} kg`;
        document.getElementById('stat-path').textContent = mockPath.join(' ➝ ');

        // Redesenha mapa com a rota
        renderMap(mockPath);
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