import {NgModule} from '@angular/core';
import {ModalComponent} from "./modal.component";
import {ModalService} from "./modal.service";
import {NgClass} from "@angular/common";

@NgModule({
  declarations: [ModalComponent],
  exports: [ModalComponent],
  imports: [
    NgClass
  ],
  providers: [ModalService]
})
export class ModalModule {
}
