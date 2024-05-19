import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';

describe('LoginComponent', () => {
  let component: LoginComponent | null;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    component?.form.get('email')!.setValue('dsqds@dsq.com');
    component?.form.get('password')!.setValue('aaa');
    fixture.detectChanges();
  });

  afterEach(async () => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should show an error message when onError is true', () => {
    component!.onError = true;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.error')).not.toBeNull();
  });

  it('login button should be disabled when login and password field are empty', () => {
    component!.form.controls['email'].setValue('');
    component!.form.controls['password'].setValue('');
    fixture.detectChanges();
    expect(
      fixture.debugElement.nativeElement.querySelector('button[type="submit"]')
        .disabled
    ).toBe(true);
  });

  it('login button shouldnt be disabled when login and password field are valid', () => {
    component!.form.controls['email'].setValue('test@test.com');
    component!.form.controls['password'].setValue('password');
    fixture.detectChanges();
    expect(
      fixture.debugElement.nativeElement.querySelector('button[type="submit"]')
        .disabled
    ).toBe(false);
  });

  it('email field should have invalid css class', () => {
    component!.form.controls['email'].setValue('');
    component!.form.controls['email'].markAsTouched();
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    console.log(fixture.debugElement.query(By.css('mat-form-field')).classes);
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });

  it('password field should have invalid css class', () => {
    component!.form.controls['password'].setValue('');
    component!.form.controls['password'].markAsTouched();
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    console.log(fixture.debugElement.query(By.css('mat-form-field')).classes);
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });
});
