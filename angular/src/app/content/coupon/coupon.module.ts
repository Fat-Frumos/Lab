import {NgModule} from '@angular/core';
import {CouponComponent} from "./coupon.component";
import {ReactiveFormsModule} from "@angular/forms";
import {DropdownCategoryComponent} from "./dropdown-category/dropdown-category.component";
import {CouponRoutingModule} from "./coupon-routing-module";
import {NgClass, NgForOf} from "@angular/common";
import {ButtonsModule} from "../../components/buttons/buttons.module";

@NgModule({
  declarations: [
    CouponComponent,
    DropdownCategoryComponent
  ],
  imports: [
    ReactiveFormsModule,
    CouponRoutingModule,
    NgForOf,
    NgClass,
    ButtonsModule,
  ],
  exports:[
    CouponComponent,
    DropdownCategoryComponent,
    CouponRoutingModule
  ]
})
export class CouponModule { }
