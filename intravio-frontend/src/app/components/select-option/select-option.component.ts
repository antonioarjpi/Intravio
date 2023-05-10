import { Component, EventEmitter, Input,OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-select-option',
  templateUrl: './select-option.component.html',
  styleUrls: ['./select-option.component.css']
})
export class SelectOptionComponent implements OnInit {
  @Input() list: any = [];
  @Input() filter: any = [];
  @Input() value: any;
  @Input() option1: string = '';
  @Input() option2: string = '';
  @Output() valueSelected = new EventEmitter<string>();

  ngOnInit(): void {
console.log(this.list);
console.log(this.filter)
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filter = this.list.filter(dado =>
      dado.nome.toLowerCase().includes(filterValue)
    );
  };
}
