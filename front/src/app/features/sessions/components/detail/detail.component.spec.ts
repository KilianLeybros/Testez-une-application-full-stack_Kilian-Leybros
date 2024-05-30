import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { DatePipe, TitleCasePipe, UpperCasePipe } from '@angular/common';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockRouter = {
    navigate: jest
      .fn()
      .mockImplementation(() => new Promise<Boolean>(() => true)),
  };
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
  const teacher: Teacher = {
    id: 2,
    lastName: 'TestTeacherLastName',
    firstName: 'TestTeacherFirstName',
    createdAt: new Date('2024-05-20'),
    updatedAt: new Date('2024-05-21'),
  };

  let titlePipe: TitleCasePipe;
  let datePipe: DatePipe;
  let upperCasePipe: UpperCasePipe;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };
  const mockSnackBar = {
    open: jest.fn(),
  };
  const mockApiSessionService = {
    all: jest.fn().mockReturnValue(of([])),
    detail: jest.fn().mockReturnValue(of(session)),
    delete: jest.fn().mockReturnValue(of([])),
    create: jest.fn().mockReturnValue(of(session)),
    update: jest.fn().mockReturnValue(of(session)),
    participate: jest.fn().mockReturnValue(of([])),
    unParticipate: jest.fn().mockReturnValue(of([])),
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([])),
    detail: jest.fn().mockReturnValue(of(teacher)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        TitleCasePipe,
        UpperCasePipe,
        DatePipe,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockApiSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
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
    }).compileComponents();

    titlePipe = new TitleCasePipe();
    upperCasePipe = new UpperCasePipe();
    datePipe = new DatePipe('en-EN');
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should display delete button when user connected is admin', () => {
    component.isAdmin = true;
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector("button[color='warn']")).not.toBeNull();
  });

  it('shouldnt display delete button when user connected is not admin', () => {
    component.isAdmin = false;
    component.isParticipate = false;
    fixture.detectChanges();
    console.log(component.isAdmin);
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector("button[color='warn']")).toBeNull();
  });

  it('should display session name', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toBe(
      titlePipe.transform(component.session!.name)
    );
  });

  it('should display teacher name', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('mat-card-subtitle span')?.textContent).toBe(
      `${component.teacher?.firstName} ${upperCasePipe.transform(
        component.teacher?.lastName
      )}`
    );
  });

  it('should display description', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.description')?.textContent?.trim()).toBe(
      `Description: ${component.session?.description}`
    );
  });

  it('should display created date', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    console.log(component.session?.createdAt?.toString());
    expect(compiled.querySelector('.created')?.textContent?.trim()).toBe(
      `Create at:  ${datePipe.transform(
        component.session?.createdAt,
        'longDate'
      )}`
    );
  });

  it('should display updated date', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.updated')?.textContent?.trim()).toBe(
      `Last update:  ${datePipe.transform(
        component.session?.updatedAt,
        'longDate'
      )}`
    );
  });

  it('should call window.history.back when clicking on back button', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector(
      'button[mat-icon-button]'
    ) as HTMLButtonElement;
    button.click();
    expect(backSpy).toBeCalled();
  });

  it('fetchSession should be called', () => {
    const sessionApiSpy = jest.spyOn(mockApiSessionService, 'detail');
    fixture.detectChanges(); // trigger ngOnInit here
    expect(sessionApiSpy).toHaveBeenCalled();
  });

  it('should delete session and open snack bar', async () => {
    const sessionApiSpy = jest.spyOn(mockApiSessionService, 'delete');
    const snackBarSpy = jest.spyOn(mockSnackBar, 'open');
    component?.delete();
    expect(sessionApiSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
  });

  it('should navigate to /session after delete', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component?.delete();
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should should participate to session', async () => {
    const sessionApiSpy = jest.spyOn(mockApiSessionService, 'participate');
    component?.participate();
    expect(sessionApiSpy).toHaveBeenCalledTimes(1);
  });

  it('should unparticipate to session', async () => {
    const sessionApiSpy = jest.spyOn(mockApiSessionService, 'unParticipate');
    component?.unParticipate();
    expect(sessionApiSpy).toHaveBeenCalledTimes(1);
  });

  it('should fetch session on init', async () => {
    const sessionApiSpy = jest.spyOn(mockApiSessionService, 'detail');
    const teacherServiceSpy = jest.spyOn(mockTeacherService, 'detail');

    fixture.detectChanges();

    expect(sessionApiSpy).toHaveBeenCalled();
    expect(teacherServiceSpy).toHaveBeenCalled();
  });
});
