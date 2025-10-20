document.addEventListener('DOMContentLoaded', () => {

    const productList = document.getElementById('product-list');
    const productDetails = document.getElementById('product-details');
    const backBtn = document.getElementById('back-btn');
    const sendBtn = document.getElementById('send-btn');
    let products = [];

    // Carregar as informações dos produtos do banco de dados
    async function carregarProdutos(){
        const resposta = await fetch("http://localhost:8080/produtos");

        products = await resposta.json();

        productList.innerHTML = "";

        products.forEach(product => {
            const div = document.createElement("div");
            div.classList.add("product-card");
            div.dataset.id = product.id;
            div.innerHTML = `
              <img src="${product.srcImg}" alt="Produto ${product.id}">
              <h2>${product.name}</h2>
              <p class="price">${product.price.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</p>
              <button class="view-details-btn">Ver Detalhes</button>
            `;
            productList.appendChild(div);
        })
    }

    carregarProdutos();

    // Evento para mostrar os detalhes do produtoa
    productList.addEventListener('click', (event) => {
        if (event.target.classList.contains('view-details-btn')) {
            const card = event.target.closest('.product-card');
            const productId = card.dataset.id;
            const product = products.find(p => p.id == productId);

            // guarda o ID selecionado
            numeroPedido = productId;

            // Atualiza os detalhes
            document.getElementById('product-title').textContent = product.name;
            document.getElementById('product-description').textContent = product.description;
            document.getElementById('product-price').textContent =
                product.price.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
            document.getElementById('product-image').src = product.srcImg;

            // Troca de tela
            productList.classList.add('hidden');
            productDetails.classList.remove('hidden');
            window.scrollTo(0, 0);
        }
    });

    // Botão voltar
    backBtn.addEventListener('click', () => {
        productDetails.classList.add('hidden');
        productList.classList.remove('hidden');
    });

    // Botão "Fazer Envio"
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
                title: 'Pedido a Caminho!',
                text: `Pedido nº ${numeroRetornado} já foi embalado e está a caminho do seu endereço.`,
                icon: 'success',
                confirmButtonText: 'Ótimo!',
                confirmButtonColor: '#3498DB'
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
});
