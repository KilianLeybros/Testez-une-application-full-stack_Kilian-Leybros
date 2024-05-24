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
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent | null;
  let fixture: ComponentFixture<LoginComponent>;

  let mockRouter = {
    navigate: Function,
  };

  interface MockedAuthService {
    login: Function;
  }
  interface MockedSessionService {
    logIn: Function;
  }

  const sessionService: MockedSessionService = {
    logIn: jest.fn().mockReturnValue(of([])),
  };
  const authService: MockedAuthService = {
    login: jest.fn().mockReturnValue(of([])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: sessionService },
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: mockRouter },
      ],
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
    component?.form.get('email')!.setValue('test@test.com');
    component?.form.get('password')!.setValue('aaa');
    fixture.detectChanges();
  });

  afterEach(async () => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  // Unit

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

  // Integration

  it('submit should login, create a session and redirect to /sessions', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component?.submit();
    expect(authService.login).toHaveBeenCalled();
    expect(sessionService.logIn).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('onError should be true when authService throw an error', async () => {
    try {
      const loginMock = jest.spyOn(authService, 'login' as never);
      loginMock.mockImplementation(() => {
        throw new Error();
      });
    } catch (err) {
      expect(component?.onError).toBeTruthy();
    }
  });
});
