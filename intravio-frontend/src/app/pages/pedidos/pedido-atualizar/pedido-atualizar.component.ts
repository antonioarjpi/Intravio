import { Component, OnInit, Renderer2 } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Filial } from 'src/app/models/filial';
import { Funcionario } from 'src/app/models/funcionario';
import { Item } from 'src/app/models/item';
import { PedidoInput } from 'src/app/models/pedido';
import { Produto } from 'src/app/models/produto';
import { FilialService } from 'src/app/services/filial.service';
import { FuncionarioService } from 'src/app/services/funcionario.service';
import { PedidoService } from 'src/app/services/pedido.service';
import { ProdutoService } from 'src/app/services/produto.service';

@Component({
  selector: 'app-pedido-atualizar',
  templateUrl: './pedido-atualizar.component.html',
  styleUrls: ['./pedido-atualizar.component.css']
})
export class PedidoAtualizarComponent implements OnInit {

  firstFormGroup = this.formBuilder.group({
    origem: ['', Validators.required],
    destino: ['', Validators.required],
    remetente: ['', Validators.required],
    destinatario: ['', Validators.required],
    prioridade: ['', Validators.required],
    acompanhaStatus: ['', Validators.required],
  });

  secondFormGroup = this.formBuilder.group({
    itens: [[], Validators.nullValidator],
  });

  produto: Produto;
  descricao: string;
  quantidade: number;
  itens: Item[] = [];
  arquivos: { file: File, url: string }[] = [];
  filialList: Filial[] = [];
  funcionarioList: Funcionario[] = [];
  produtoList: Produto[] = [];

  pedido: PedidoInput = {
    id: "",
    itens: this.itens,
    numeroPedido: null,
    fotos: [],
    origem: "",
    destino: "",
    remetente: "",
    destinatario: "",
    prioridade: null,
    acompanhaStatus: null,
  };

  priorityOptions = [
    { label: 'Baixa', value: 0 },
    { label: 'Média', value: 1 },
    { label: 'Alta', value: 2 },
    { label: 'Urgente', value: 3 }
  ];

  acompanhaStatusOptions = [
    { label: 'Ninguém', value: 0 },
    { label: 'Destinatário', value: 1 },
    { label: 'Remetente', value: 2 },
    { label: 'Ambos', value: 3 }
  ];

  constructor(
    private service: PedidoService,
    private funcionarioService: FuncionarioService,
    private filialService: FilialService,
    private produtoService: ProdutoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private renderer: Renderer2
  ) { };

  ngOnInit(): void {
    this.pedido.id = this.route.snapshot.paramMap.get("id")

    this.produtoService.findAll().subscribe((response) => {
      this.produtoList = response
    });

    this.funcionarioService.findAll().subscribe((response) => {
      this.funcionarioList = response
    });

    this.filialService.findAll().subscribe((response) => {
      this.filialList = response
    });

    this.buscarPedidoPorId();

  };

  buscarPedidoPorId(): void {
    this.service.findById(this.pedido.id).subscribe((response) => {
      this.pedido = response;
      this.pedido.prioridade = this.returnPriority(response.prioridade);
      this.pedido.acompanhaStatus = this.returnAcompanhaStatus(response.acompanhaStatus);

      // Fotos
      for (let i = 0; i < response.fotos.length; i++) {
        const link = response.fotos[i];
        this.service.exibirImagem(link).subscribe(blob => {
          const reader = new FileReader();
          reader.onload = () => {
            const dataUrl = reader.result as string;
            const file = new File([blob], link, { lastModified: new Date().getTime(), type: blob.type });
            this.arquivos.push({ file, url: dataUrl });
            console.log(this.arquivos)
          };
          reader.readAsDataURL(blob);
        }, error => {
          console.error(`Erro ao exibir a imagem ${link}: ${error}`);
        });
      }

      for (let i = 0; i <= response.itens.length; i++) {
        // Adicionando os response nos itens
        const item: Item = {
          produto: response.itens[i].produto,
          descricao: response.itens[i].descricao,
          quantidade: response.itens[i].quantidade,
        };

        this.itens.push(item);
        // Remove produtos da lista
        this.produtoList = this.produtoList.filter(produto =>
          !this.pedido.itens.some(item => item.produto.id === produto.id)
        );
      }
    })
  };

  finalizarPedido(): void {
    this.pedido.itens = this.itens;
    if (this.arquivos.length < 1) {
      this.pedido.fotos = null;
    }
    this.service.update(this.pedido).subscribe(
      (response) => {
        if (this.arquivos.length < 1) {
          this.toast.success("Pedido realizado com sucesso", "Cadastro");
          this.router.navigate(["pedidos"]);
          return;
        }
        this.pedido.id = response.id;
        this.adicionarArquivo(this.pedido.id);
      },
      (ex) => {
        if (ex.error.errors) {
          ex.error.errors.forEach((element) => {
            this.toast.error(element.message);
          });
        } else {
          this.toast.error(ex.error.message);
        }
      }
    )
  };

  adicionarArquivo(id: any) {
    const files = this.arquivos.map(a => a.file);
    this.service.uploadFiles(id, files)
      .subscribe(() => {
        this.toast.success("Pedido atualizado com sucesso", "Atualização");
        this.router.navigate(["pedidos"])
      }, () => {
        this.toast.error("O pedido foi salvo, porém houve erro ao envia as imagens. Tente novamente", "Erro");
      });
  }

  onFileChange(event) {
    const files = event.target.files;
    const urls = [];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const url = URL.createObjectURL(file);
      urls.push(url);
      this.arquivos.push({ file, url });
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    event.dataTransfer.dropEffect = 'copy';
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const files = event.dataTransfer.files;
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      this.arquivos.push({ file: file, url: URL.createObjectURL(file) });
    }
  }

  adicionarItem() {
    const item: Item = {
      produto: this.produto,
      descricao: this.descricao,
      quantidade: this.quantidade
    };
    this.itens.push(item);

    this.removeProdutoDaLista();
    this.zeraAtributosParaNovoItem();
  };

  removerItem(item: Item) {
    this.produtoList.push(item.produto)
    const index = this.itens.indexOf(item);
    this.itens.splice(index, 1);
  };

  removerArquivo(arquivo) {
    console.log(arquivo)
    const index = this.arquivos.indexOf(arquivo);
    if (index !== -1) {
      URL.revokeObjectURL(arquivo.url);
      this.arquivos.splice(index, 1);
    }
  }

  removeProdutoDaLista(): void {
    const index = this.produtoList.indexOf(this.produto);
    if (index >= 0) {
      this.produtoList.splice(index, 1);
    }
  };

  zeraAtributosParaNovoItem(): void {
    this.produto = null;
    this.descricao = '';
    this.quantidade = null;
  }

  validarItens(): boolean {
    if (this.quantidade === null || this.produto === null || this.quantidade < 1 || this.produto == undefined || this.quantidade == undefined) {
      return true;
    } else {
      return false
    };
  }

  validarListaDeItens(): boolean {
    if (this.itens.length > 0) {
      return false;
    } else {
      return true;
    };
  }

  returnPriority(status: any): Number {
    if (status === "BAIXA") {
      return 0;
    } else if (status === "MEDIA") {
      return 1;
    } else if (status === "ALTA") {
      return 2;
    } else {
      return 3;
    }
  }

  returnAcompanhaStatus(status: any): Number {
    if (status === "NAO") {
      return 0;
    } else if (status === "SIM_DESTINATARIO") {
      return 1;
    } else if (status === "SIM_REMETENTE") {
      return 2;
    } else {
      return 3;
    }
  }
}


