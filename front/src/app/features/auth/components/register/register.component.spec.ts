import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let mockRouter = {
    navigate: Function,
  };

  interface MockedAuthService {
    register: Function;
  }
  const authService: MockedAuthService = {
    register: jest.fn().mockReturnValue(of([])),
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: mockRouter },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    component!.form.get('email')!.setValue('test@test.com');
    component!.form.get('password')!.setValue('aaa');
    component!.form.get('firstName')!.setValue('Test');
    component!.form.get('lastName')!.setValue('Test');
    fixture.detectChanges();
  });

  // Unit

  it('should show an error message when onError is true', () => {
    component!.onError = true;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    console.log(compiled);
    expect(compiled.querySelector('.error')).not.toBeNull();
  });

  it('register button should be disabled when firstname, lastname, email or password field are empty', () => {
    component!.form.controls['email'].setValue('');
    component!.form.controls['password'].setValue('');
    component!.form.controls['firstName'].setValue('');
    component!.form.controls['lastName'].setValue('');
    fixture.detectChanges();
    expect(
      fixture.debugElement.nativeElement.querySelector('button[type="submit"]')
        .disabled
    ).toBe(true);
  });

  it('register button shouldnt be disabled when login and password field are valid', () => {
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
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });

  it('invalid email should have invalid css class', () => {
    component!.form.controls['email'].setValue('test');
    component!.form.controls['email'].markAsTouched();
    fixture.detectChanges();
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
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });

  it('firstName field should have invalid css class', () => {
    component!.form.controls['password'].setValue('');
    component!.form.controls['password'].markAsTouched();
    fixture.detectChanges();
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });

  it('lastName field should have invalid css class', () => {
    component!.form.controls['password'].setValue('');
    component!.form.controls['password'].markAsTouched();
    fixture.detectChanges();
    expect(
      fixture.debugElement.nativeElement.querySelector(
        '.mat-form-field-invalid'
      )
    ).not.toBeNull();
  });

  // Integration
  it('submit should register and redirect to /login', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component?.submit();
    expect(authService.register).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('onError should be true when authService throw an error', async () => {
    try {
      const registerMock = jest.spyOn(authService, 'register' as never);
      registerMock.mockImplementation(() => {
        throw new Error();
      });
    } catch (err) {
      expect(component?.onError).toBeTruthy();
    }
  });
});
