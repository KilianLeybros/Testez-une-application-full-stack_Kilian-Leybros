import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const session: Session = {
    id: 1,
    name: 'testSession',
    description: 'testDescription',
    date: new Date(),
    teacher_id: 2,
    users: [1],
    createdAt: new Date('2024-05-22'),
    updatedAt: new Date('2024-05-23'),
  };

  let mockSessionService = {
    sessionInformation: {
      admin: false,
    },
  };

  let mockRouter = {
    navigate: jest
      .fn()
      .mockImplementation(() => new Promise<Boolean>(() => true)),
    url: 'test',
  };

  const mockSnackBar = {
    open: jest.fn(),
  };

  let mockApiSessionService = {
    all: jest.fn().mockReturnValue(of([])),
    detail: jest.fn().mockReturnValue(of(session)),
    delete: jest.fn().mockReturnValue(of([])),
    create: jest.fn().mockReturnValue(of(session)),
    update: jest.fn().mockReturnValue(of(session)),
    participate: jest.fn().mockReturnValue(of([])),
    unParticipate: jest.fn().mockReturnValue(of([])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        MatSnackBarModule,
      ],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockApiSessionService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({
                id: '1',
              }),
            },
          },
        },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should redirect to /session if user is not admin', () => {
    const routerSpy = jest.spyOn(mockRouter, 'navigate');
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should be on update', () => {
    mockRouter.url = 'update';
    const spyApiSessionService = jest.spyOn(mockApiSessionService, 'detail');
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(spyApiSessionService).toHaveBeenCalled();
    expect(component.sessionForm?.get('name')?.value).toBe(session.name);
    expect(component.sessionForm?.get('date')?.value).toBe(
      new Date(session.date).toISOString().split('T')[0]
    );
    expect(component.sessionForm?.get('teacher_id')?.value).toBe(
      session.teacher_id
    );
    expect(component.sessionForm?.get('description')?.value).toBe(
      session.description
    );
  });

  it('should be on create', () => {
    component.onUpdate = false;
    mockRouter.url = 'test';
    component.ngOnInit();
    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm?.get('name')?.value).toBe('');
    expect(component.sessionForm?.get('date')?.value).toBe('');
    expect(component.sessionForm?.get('teacher_id')?.value).toBe('');
    expect(component.sessionForm?.get('description')?.value).toBe('');
  });

  it('should update session on update', () => {
    component.onUpdate = true;
    const spyUpdateApiSessionService = jest.spyOn(
      mockApiSessionService,
      'update'
    );
    const routerSpy = jest.spyOn(mockRouter, 'navigate');

    component.submit();

    expect(spyUpdateApiSessionService).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should create session on create', () => {
    component.onUpdate = false;
    const spyCreateApiSessionService = jest.spyOn(
      mockApiSessionService,
      'create'
    );
    const routerSpy = jest.spyOn(mockRouter, 'navigate');

    component.submit();

    expect(spyCreateApiSessionService).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should redirect to /sessions after exitPage', () => {
    const routerSpy = jest.spyOn(mockRouter, 'navigate');

    component['exitPage']('');
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });
});
