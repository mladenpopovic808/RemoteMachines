import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleCleanerComponent } from './schedule-cleaner.component';

describe('ScheduleCleanerComponent', () => {
  let component: ScheduleCleanerComponent;
  let fixture: ComponentFixture<ScheduleCleanerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScheduleCleanerComponent]
    });
    fixture = TestBed.createComponent(ScheduleCleanerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
