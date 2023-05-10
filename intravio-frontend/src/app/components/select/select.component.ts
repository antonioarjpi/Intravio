import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.css']
})
export class SelectComponent {

  @Input() list: any = [];
  @Input() filter: any = [];
  @Input() valueOption: any;
  @Input() className: string = '';
  @Input() option1: string = '';
  @Input() option2: string = '';
  @Input() label: string = '';
  @Input() findBy: string = '';
  @Input() isRequired: boolean;
  @Output() valueSelected = new EventEmitter<string>();
  selectedValue: string;

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filter = this.list.filter(dado =>
      dado[this.findBy].toLowerCase().includes(filterValue)
    );
  };

  emitValue() {
    this.valueSelected.emit(this.selectedValue);
  }
}
