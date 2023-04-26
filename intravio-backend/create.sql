
    create table departamento (
       id bigserial not null,
        nome varchar(255),
        primary key (id)
    );

    create table filial (
       id bigint not null,
        nome varchar(255),
        primary key (id)
    );

    create table funcionario (
       id bigserial not null,
        email varchar(255),
        nome varchar(255),
        departamento_id bigint,
        primary key (id)
    );

    create table itens (
       descricao varchar(255),
        peso float(53),
        preco float(53),
        quantidade integer,
        pedido_id bigint not null,
        produto_id bigint not null,
        primary key (pedido_id, produto_id)
    );

    create table pedido (
       id bigserial not null,
        data_atualizacao timestamp(6),
        data_pedido timestamp(6),
        fotos varchar(255) array,
        prioridade integer,
        status varchar(255),
        destinatario_id bigint,
        destino_id bigint,
        origem_id bigint,
        remetente_id bigint,
        primary key (id)
    );

    create table produto (
       id bigserial not null,
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

    alter table if exists departamento 
       add constraint UK_d0gdbc1oh2ffvl6f54xvxghrr unique (nome);

    alter table if exists filial 
       add constraint UK_ooig40h8obp7hjod6c9d0yh0a unique (nome);

    alter table if exists funcionario 
       add constraint UK_t45qja1wnv0hu1cdw6vqjljgy unique (email);

    alter table if exists funcionario 
       add constraint FK6txpbkcvg8ybbgl4ou4utb3iu 
       foreign key (departamento_id) 
       references departamento;

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
