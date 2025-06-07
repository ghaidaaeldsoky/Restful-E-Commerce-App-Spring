import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { AdminOrdersComponent } from './components/admin/admin-orders/admin-orders.component';
import { AdminUsersComponent } from './components/admin/admin-users/admin-users.component';
import { AdminProductsComponent } from './components/admin/admin-products/admin-products.component';
import { FormsModule } from '@angular/forms';
import { AdminProductEditComponent } from './components/admin/admin-product-edit/admin-product-edit.component';
import { AdminProductAddComponent } from './components/admin/admin-product-add/admin-product-add.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    AdminLayoutComponent,
    AdminOrdersComponent,
    AdminUsersComponent,
    AdminProductsComponent,
    AdminProductEditComponent,
    AdminProductAddComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
