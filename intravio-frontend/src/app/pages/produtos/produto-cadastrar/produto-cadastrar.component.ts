import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Produto } from 'src/app/models/produto';
import { ProdutoService } from 'src/app/services/produto.service';

@Component({
  selector: 'app-produto-cadastrar',
  templateUrl: './produto-cadastrar.component.html',
  styleUrls: ['./produto-cadastrar.component.css']
})
export class ProdutoCadastrarComponent {

  id: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  codigo: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  fabricante: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  modelo: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  preco: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  peso: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);

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
    private router: Router
  ) { };

  cadastrarProduto(): void {
    if (!this.validaCampos()) {
      return;
    }

    const novoPreco = this.produto.preco.toString().replace(".", "").replace(",", ".");
    this.produto.preco = parseFloat(novoPreco);

    const novoPeso = this.produto.peso.toString().replace(".", "").replace(",", ".");
    this.produto.peso = parseFloat(novoPeso);

    this.service.create(this.produto).subscribe(() => {
      this.toast.success("Produto cadastrada com sucesso", "Cadastro");
      this.router.navigate(["produtos"]);
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
  }

  validaCampos(): boolean {
    return (
      this.nome.valid && this.id.valid && this.codigo.valid && this.fabricante.valid
      && this.modelo.valid && this.preco.valid && this.peso.valid
    );
  }
}

