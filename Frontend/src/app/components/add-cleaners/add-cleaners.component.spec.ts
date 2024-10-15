import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCleanersComponent } from './add-cleaners.component';

describe('AddCleanersComponent', () => {
  let component: AddCleanersComponent;
  let fixture: ComponentFixture<AddCleanersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddCleanersComponent]
    });
    fixture = TestBed.createComponent(AddCleanersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
