import { AfterViewInit, Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.css']
})
export class SelectComponent implements AfterViewInit {

  @Output() valueSelected = new EventEmitter<string>();
  @Input() list: any = [];
  @Input() filter: any = [];
  @Input() valueOption: string = null;
  @Input() className: string = '';
  @Input() option1: string = '';
  @Input() option2: string = '';
  @Input() label: string = '';
  @Input() findBy: string = '';
  @Input() ngName: string = '';
  @Input() isRequired: boolean;
  @Input() valorInicial: String = '';
  selectedValue: any;

  value = '';

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.selectedValue = this.valorInicial
    }, 700)
  }

  applyFilter() {
    const filterValue = (this.value);
    this.filter = this.list.filter(dado =>
      dado[this.findBy].trim().toLowerCase().includes(filterValue.toLocaleLowerCase())
    );
  };

  emitValue() {
    this.valueSelected.emit(this.selectedValue);
  }

  remove() {
    this.value = '';
    this.applyFilter();
  }
}
