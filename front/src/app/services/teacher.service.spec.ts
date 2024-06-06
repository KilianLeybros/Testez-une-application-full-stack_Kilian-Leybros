import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should get all teacher', () => {
    const mockData: Teacher[] = [
      {
        id: 1,
        lastName: 'TeacherLastName1',
        firstName: 'TeacherFirstName1',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        lastName: 'TeacherLastName2',
        firstName: 'TeacherFirstName2',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    service.all().subscribe((teachers: Teacher[]) => {
      expect(teachers).toBe(mockData);
    });

    const req = httpTesting.expectOne('api/teacher');

    expect(req.request.method).toBe('GET');

    req.flush(mockData);

    httpTesting.verify();
  });

  it('should get teacher details', () => {
    const mockData: Teacher = {
      id: 1,
      lastName: 'TeacherLastName1',
      firstName: 'TeacherFirstName1',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.detail(mockData.id.toString()).subscribe((teacher: Teacher) => {
      expect(teacher).toBe(mockData);
    });

    const req = httpTesting.expectOne(`api/teacher/${mockData.id}`);

    expect(req.request.method).toBe('GET');

    req.flush(mockData);

    httpTesting.verify();
  });
});
