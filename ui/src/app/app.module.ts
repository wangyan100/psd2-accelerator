import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { AppComponent } from './app.component';
import { GenerateCertificatePageComponent } from './generate-certificate-page/generate-certificate-page.component';
import { AppRoutingModule } from './app-routing.module';
import {RouterModule} from '@angular/router';



@NgModule({
  declarations: [
    AppComponent,
    GenerateCertificatePageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }