import { Component, OnInit, Renderer2 } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
  selector: 'app-pedido-criar',
  templateUrl: './pedido-criar.component.html',
  styleUrls: ['./pedido-criar.component.css']
})

export class PedidoCriarComponent implements OnInit {

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
    acompanhaStatus: null

  };

  constructor(
    private service: PedidoService,
    private funcionarioService: FuncionarioService,
    private filialService: FilialService,
    private produtoService: ProdutoService,
    private toast: ToastrService,
    private router: Router,
    private formBuilder: FormBuilder,
    private renderer: Renderer2
  ) { };

  ngOnInit(): void {
    this.funcionarioService.findAll().subscribe((response) => {
      this.funcionarioList = response
    });

    this.filialService.findAll().subscribe((response) => {
      this.filialList = response
    });

    this.produtoService.findAll().subscribe((response) => {
      this.produtoList = response
    });
  };

  handleValueSelected(value: string, formControlName: string) {
    this.pedido[formControlName] = value;
    this.firstFormGroup.get(formControlName).setValue(value);
  }

  finalizarPedido(): void {
    this.pedido.itens = this.itens;
    this.service.create(this.pedido).subscribe(
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
        this.toast.success("Pedido realizado com sucesso", "Cadastro");
        this.router.navigate(["pedidos"])
      }, error => {
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
  };

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    event.dataTransfer.dropEffect = 'copy';
    this.renderer.addClass(event.target, 'drag-over');
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
    const index = this.arquivos.indexOf(arquivo);
    if (index !== -1) {
      URL.revokeObjectURL(arquivo.url);
      this.arquivos.splice(index, 1);
    }
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
}

