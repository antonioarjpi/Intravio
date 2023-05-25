import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Filial } from 'src/app/models/filial';
import { FilialService } from 'src/app/services/filial.service';

@Component({
  selector: 'app-filial-deletar',
  templateUrl: './filial-deletar.component.html',
  styleUrls: ['./filial-deletar.component.css']
})
export class FilialDeletarComponent implements OnInit {

  filial: Filial = {
    id: null,
    nome: "",
    rua: "",
    cep: "",
    bairro: "",
    numero: null,
    complemento: "",
    estado: "",
    cidade: "",
  };

  constructor(
    private service: FilialService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.filial.id = Number(this.route.snapshot.paramMap.get('id'));
    this.buscaPorId();
  };

  buscaPorId(): void {
    this.service.findById(this.filial.id).subscribe((response) => {
      this.filial = response;
    })
  }

  deletarFilial(): void {
    this.service.delete(this.filial.id).subscribe(() => {
      this.toast.success("Filial deletada com sucesso", "Exclusão");
      this.router.navigate(["filiais"])
    },
      (ex) => {
        this.toast.error(ex.error.message);
      })
  }
}