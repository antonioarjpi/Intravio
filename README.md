<body>
  
  # ⚡ Intravio
  
  <p>Este é o repositório do projeto Intravio, uma aplicação que visa proporcionar um controle eficiente do envio de encomendas entre filiais, facilitando a gestão e rastreabilidade desse processo. Suas funcionalidades incluem operações CRUD para itens como Departamentos, Filiais, Transportadores, Produtos, Pedidos e Romaneios.</p>
  
  ## 📄 Sobre o projeto
  
  <p>O objetivo principal do Intravio é fornecer um sistema completo para o controle de envio de encomendas entre filiais, com ênfase na gestão e rastreabilidade do processo. Com o Intravio, os usuários poderão realizar operações como criar, editar e excluir informações sobre departamentos, filiais, transportadores, produtos, pedidos e romaneios.</p>
  <p>O rastreamento das encomendas é realizado através de um código gerado durante a criação do pedido. O sistema permite que os usuários realizem um pré-cadastro com informações do remetente e destinatário, além de especificar as filiais de origem e destino. Também é possível definir se o remetente e/ou destinatário receberão avisos por e-mail. É obrigatório incluir pelo menos um produto no pedido, podendo também anexar fotos.</p>
  <p>Após a criação do pedido, o status é definido como "Pendente" e aguarda a inclusão no romaneio. Para criar um romaneio, é necessário ter pelo menos um pedido e associar pelo menos um transportador. Após a criação do romaneio, o status do pedido é automaticamente alterado para "Separado" se o usuário tiver selecionado a opção de processamento do romaneio durante a criação, ou para "Em trânsito" se o romaneio já tiver sido processado. Após o processamento do romaneio, o usuário deve realizar o fechamento do mesmo e informar quais pedidos foram entregues ou não, assim finalizando o status do Pedido.</p>
  
  ## 💻 Principais funções

  <ul>
    <li>CRUD (Create, Read, Update, Delete) completo para todos os objetos: Departamentos, Filiais, Transportadores, Produtos, Pedidos e Romaneios.</li>
    <li>Autenticação e autorização: Permite que os usuários façam login no sistema e definam suas permissões de acesso</li>
    <li>Gerenciamento de usuários: Permite criar, editar e excluir usuários, bem como atribuir perfil permissões a eles</li>
    <li>Pesquisa e filtragem: Possibilita buscar e filtrar informações com base em critérios específicos.</li>
    <li>Envio de e-mails para o remetente e/ou destinatário, com notificações sobre o status da encomenda.</li>
    <li>Anexo de imagens nos pedidos, permitindo que os usuários enviem fotos dos produtos a serem enviados.</li>
    <li>Download zipado de fotos relacionados aos pedidos.</li>   
    <li>Aplicação responsiva, adaptando-se a diferentes dispositivos e tamanhos de tela.</li>
  </ul>
  
  
  ## 🚀 Tecnologias utilizadas
  
  <ul>
    <li>Linguagem de programação: Java</li>
    <li>Framework: Spring Boot</li>
    <li>Persistência de dados: JPA (Java Persistence API)</li>
    <li>Front-end: Angular</li>
  </ul>
  
  ## 📷 Screenshots

<div align='center'>
  <h3>Intravio em Desktop</h3>
  <img src="https://github.com/antonioarjpi/Intravio/blob/main/intravio-frontend/img/Intravio%20Desktop.gif" alt="Intravio Desktop"/>

  <h3>Intravio em dispositivos móveis</h3>
  <img src="https://github.com/antonioarjpi/Intravio/blob/main/intravio-frontend/img/Intravio%20mobile.gif" alt="Intravio Mobile"/>

  <h3>Tela de rastreamento</h3>
  <img src="https://github.com/antonioarjpi/Intravio/blob/main/intravio-frontend/img/rastreio.jpg" alt="Tela de rastreio"/>
</div>
  
  <h2>Como executar a aplicação</h2>
  <p>Para executar a aplicação, siga as instruções abaixo:</p>
  <ol>
    <li>Certifique-se de ter o Java e o Node.js instalados em sua máquina.</li>
    <li>Clone este repositório para o seu ambiente local:</li>
  </ol>
  <pre><code>git clone https://github.com/antonioarjpi/Intravio.git</code></pre>
  <ol start="3">
    <li>Acesse o diretório do projeto:</li>
  </ol>
  <pre><code>cd intravio</code></pre>
  <ol start="4">
    <li>Inicie o backend do Spring Boot:</li>
  </ol>
  <pre><code>./mvnw spring-boot:run</code></pre>
  <ol start="5">
    <li>Em outro terminal, acesse o diretório do frontend:</li>
  </ol>
  <pre><code>cd intravio-frontend</code></pre>
  <ol start="6">
    <li>Instale as dependências do frontend:</li>
  </ol>
  <pre><code>npm install</code></pre>
  <ol start="7">
    <li>Inicie o servidor de desenvolvimento do Angular:</li>
  </ol>
  <pre><code>ng serve</code></pre>
  <ol start="8">
    <li>Acesse a aplicação em seu navegador, utilizando o endereço: <a href="http://localhost:4200/">http://localhost:4200/</a></li>
  </ol>
 
  ## 🤝 Colaboradores
<div align="left" style="width:10px">
  <div>
    <kbd>
      <a href="https://github.com/antonioarjpi/Filmes-scoreXP"><img src="https://avatars.githubusercontent.com/u/89957734?v=4" height="auto" width="80" style="border-radius:50%" /></a>
    </kbd>
  </div>
  <a margin="10px" href="https://github.com/antonioarjpi"><label>Antônio</label></a>
</div>

</body>