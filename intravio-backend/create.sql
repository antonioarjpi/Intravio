
    create table departamento (
       id varchar(255) not null,
        nome varchar(255),
        primary key (id)
    );

    create table filial (
       id bigint not null,
        nome varchar(255),
        primary key (id)
    );

    create table funcionario (
       id varchar(255) not null,
        email varchar(255),
        nome varchar(255),
        departamento_id varchar(255),
        primary key (id)
    );

    create table historico_pedido (
       id bigserial not null,
        comentario varchar(255),
        data_atualizacao timestamp(6),
        status_anterior smallint,
        status_atual smallint,
        pedido_id bigint,
        primary key (id)
    );

    create table itens (
       descricao varchar(255),
        peso float(53),
        preco float(53),
        quantidade integer,
        pedido_id bigint not null,
        produto_id varchar(255) not null,
        primary key (pedido_id, produto_id)
    );

    create table pedido (
       id bigserial not null,
        acompanha_status smallint,
        codigo_rastreio varchar(255),
        data_atualizacao timestamp(6),
        data_pedido timestamp(6),
        numero_pedido integer,
        prioridade smallint,
        status_pedido smallint,
        destinatario_id varchar(255),
        destino_id bigint,
        origem_id bigint,
        remetente_id varchar(255),
        romaneio_id bigint,
        primary key (id)
    );

    create table pedido_imagens (
       pedido_id bigint not null,
        imagens varchar(255)
    );

    create table produto (
       id varchar(255) not null,
        data_atualizacao timestamp(6),
        data_criacao timestamp(6),
        descricao varchar(255),
        fabricante varchar(255),
        modelo varchar(255),
        nome varchar(255),
        peso float(53),
        preco float(53),
        primary key (id)
    );

    create table romaneio (
       id bigserial not null,
        status varchar(255),
        data_atualizacao timestamp(6),
        data_criacao timestamp(6),
        observacao varchar(255),
        taxa_frete float(53),
        trasportador_id varchar(255),
        primary key (id)
    );

    create table transportador (
       id varchar(255) not null,
        cnpj varchar(255),
        motorista varchar(255),
        nome varchar(255),
        observacao varchar(255),
        placa varchar(255),
        veiculo varchar(255),
        primary key (id)
    );

    alter table if exists departamento 
       add constraint UK_d0gdbc1oh2ffvl6f54xvxghrr unique (nome);

    alter table if exists filial 
       add constraint UK_ooig40h8obp7hjod6c9d0yh0a unique (nome);

    alter table if exists funcionario 
       add constraint UK_t45qja1wnv0hu1cdw6vqjljgy unique (email);

    alter table if exists pedido 
       add constraint UK_j0l80ge0fmklge54qlnx8ovur unique (numero_pedido);

    alter table if exists produto 
       add constraint UK_hdot1xprktyi4sf2onvllkmkd unique (nome);

    alter table if exists transportador 
       add constraint UK_6y645y17574fm5biup72dlt0m unique (cnpj);

    alter table if exists transportador 
       add constraint UK_huxhprtg4mpq3b430f4xvhjr2 unique (nome);

    alter table if exists funcionario 
       add constraint FK6txpbkcvg8ybbgl4ou4utb3iu 
       foreign key (departamento_id) 
       references departamento;

    alter table if exists historico_pedido 
       add constraint FKkatwk02f82dxb8k9pb03ftfne 
       foreign key (pedido_id) 
       references pedido;

    alter table if exists itens 
       add constraint FKohprhovooqogulum51yd9wdao 
       foreign key (pedido_id) 
       references pedido;

    alter table if exists itens 
       add constraint FKbn9nsa5dkqqaqs2bqb6yj9jhq 
       foreign key (produto_id) 
       references produto;

    alter table if exists pedido 
       add constraint FKywg4ujcqku4cew182s05xfgj 
       foreign key (destinatario_id) 
       references funcionario;

    alter table if exists pedido 
       add constraint FK79reu8flvu7qhrrk3rcxvidow 
       foreign key (destino_id) 
       references filial;

    alter table if exists pedido 
       add constraint FKj79uaiu4k65mj7w9niyhlbg64 
       foreign key (origem_id) 
       references filial;

    alter table if exists pedido 
       add constraint FKlgui275o10n2m4s3oe8u3fa1a 
       foreign key (remetente_id) 
       references funcionario;

    alter table if exists pedido 
       add constraint FKar9cw51vs4o3fqbf4t70p08hx 
       foreign key (romaneio_id) 
       references romaneio;

    alter table if exists pedido_imagens 
       add constraint FK2af8cpsrtji8xfcuoapqxy1jt 
       foreign key (pedido_id) 
       references pedido;

    alter table if exists romaneio 
       add constraint FKifae0jc3dfkylireaan4uh5bb 
       foreign key (trasportador_id) 
       references transportador;
