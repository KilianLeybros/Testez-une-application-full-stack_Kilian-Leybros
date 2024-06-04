import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { AuthService } from './auth.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Observable, of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpTesting: HttpTestingController;
  let httpClient: HttpClient;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
    //httpClient = TestBed.inject(HttpClient);
  });

  it('should register', () => {
    service
      .register({
        email: 'test@test.com',
        firstName: 'Test',
        lastName: 'Test',
        password: 'Test',
      })
      .subscribe((data: void) => {
        expect(data).toBe({});
      });

    const req = httpTesting.expectOne('api/auth/register');

    expect(req.request.method).toBe('POST');

    req.flush(new Observable<void>());

    httpTesting.verify();
  });

  it('should login', () => {
    const loginResponse: SessionInformation = {
      token: 'token',
      type: 'bearer',
      id: 1,
      username: 'Test',
      firstName: 'Test',
      lastName: 'Test',
      admin: true,
    };

    service
      .login({
        email: 'test@test.com',
        password: 'Test',
      })
      .subscribe((data: SessionInformation) => {
        expect(data).toBe(loginResponse);
      });

    const req = httpTesting.expectOne('api/auth/login');

    expect(req.request.method).toBe('POST');

    req.flush(loginResponse);

    httpTesting.verify();
  });
});
