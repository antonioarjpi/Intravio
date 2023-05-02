import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Produto } from 'src/app/models/produto';
import { ProdutoService } from 'src/app/services/produto.service';

@Component({
  selector: 'app-produto-deletar',
  templateUrl: './produto-deletar.component.html',
  styleUrls: ['./produto-deletar.component.css']
})
export class ProdutoDeletarComponent implements OnInit{

  produto: Produto = {
    id: null,
    codigo: null,
    nome: "",
    descricao: "",
    fabricante: "",
    modelo: "",
    peso: null,
    preco: null,
  };

  constructor(
    private service: ProdutoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit() {
    this.produto.id = this.route.snapshot.paramMap.get('id');
    this.buscarPorId();
  }
  
  buscarPorId(): void {
    this.service.findById(this.produto.id).subscribe(response => {
      this.produto = response
    })
  }

  deletarProduto(): void {
    this.service.delete(this.produto.id).subscribe(
      () => {
        this.toast.success("Produto deletado com sucesso", "Exclusão");
        this.router.navigate(["produtos"]);
      },
      (ex) => {
        this.toast.error(ex.error.message);
      }
    );
  }

}
