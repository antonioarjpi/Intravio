import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Transportador } from 'src/app/models/transportador';
import { TransportadorService } from 'src/app/services/transportador.service';

@Component({
  selector: 'app-transportador-deletar',
  templateUrl: './transportador-deletar.component.html',
  styleUrls: ['./transportador-deletar.component.css']
})
export class TransportadorDeletarComponent implements OnInit {

  transportador: Transportador = {
    id: "",
    nome: "",
    motorista: "",
    placa: "",
    veiculo: "",
    observacao: "",
    cnpj: ""
  };

  constructor(
    private service: TransportadorService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit(): void {
    this.transportador.id = this.route.snapshot.paramMap.get('id');
    this.buscarPorId();
  }

  buscarPorId(): void {
    this.service.findById(this.transportador.id).subscribe((response) => {
      this.transportador = response;
    })
  }

  deletarTransportador(): void {
    this.service.delete(this.transportador.id).subscribe(
      () => {
        this.toast.success("Transportador deletado com sucesso", "Exclusão");
        this.router.navigate(["transportadores"]);
      },
      (ex) => {
        this.toast.error(ex.error.message);
      }
    );
  }
}