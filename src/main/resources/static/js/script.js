document.addEventListener('DOMContentLoaded', () => {

    const productList = document.getElementById('product-list');
    const productDetails = document.getElementById('product-details');
    const backBtn = document.getElementById('back-btn');
    const sendBtn = document.getElementById('send-btn');

    // 🔹 variável global para armazenar o ID do produto selecionado
    let numeroPedido = null;

    const products = {
        '1': {
            name: 'Produto Moderno 1',
            price: 'R$ 199,90',
            description: 'Uma descrição detalhada sobre o Produto Moderno 1...',
            image: '/img/7_product-mockup.jpg'
        },
        '2': {
            name: 'Produto Elegante 2',
            price: 'R$ 249,90',
            description: 'O Produto Elegante 2 combina funcionalidade...',
            image: '/img/30_product-mockups.jpg'
        },
        '3': {
            name: 'Produto Minimalista 3',
            price: 'R$ 149,90',
            description: 'Com um design limpo e focado no essencial...',
            image: '/img/skin-products-arrangement-wooden-blocks_23-2148761445.jpg'
        }
    };

    // 🟢 Evento para mostrar os detalhes do produto
    productList.addEventListener('click', (event) => {
        if (event.target.classList.contains('view-details-btn')) {
            const card = event.target.closest('.product-card');
            const productId = card.dataset.productId; // ← ID do produto
            const product = products[productId];

            // guarda o ID selecionado
            numeroPedido = productId;

            // Atualiza os detalhes
            document.getElementById('product-title').textContent = product.name;
            document.getElementById('product-description').textContent = product.description;
            document.getElementById('product-price').textContent = product.price;
            document.getElementById('product-image').src = product.image;

            // Troca de tela
            productList.classList.add('hidden');
            productDetails.classList.remove('hidden');
            window.scrollTo(0, 0);
        }
    });

    // 🔙 Botão voltar
    backBtn.addEventListener('click', () => {
        productDetails.classList.add('hidden');
        productList.classList.remove('hidden');
    });

    // 🚀 Botão "Fazer Envio"
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
