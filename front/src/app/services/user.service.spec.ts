import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';
import { Observable } from 'rxjs';

describe('UserService', () => {
  let service: UserService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(UserService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should get user by id', () => {
    const mockData: User = {
      id: 1,
      email: 'test@test.com',
      lastName: 'TestLastName',
      firstName: 'TestFirstName',
      admin: true,
      password: 'test',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.getById(mockData.id.toString()).subscribe((user: User) => {
      expect(user).toBe(mockData);
    });

    const req = httpTesting.expectOne(`api/user/${mockData.id}`);

    expect(req.request.method).toBe('GET');

    req.flush(mockData);

    httpTesting.verify();
  });

  it('should delete user', () => {
    const userId = '1';

    service.delete(userId).subscribe((data: any) => {
      expect(data).toBe({});
    });

    const req = httpTesting.expectOne(`api/user/${userId}`);

    expect(req.request.method).toBe('DELETE');

    req.flush(new Observable<any>());

    httpTesting.verify();
  });
});
