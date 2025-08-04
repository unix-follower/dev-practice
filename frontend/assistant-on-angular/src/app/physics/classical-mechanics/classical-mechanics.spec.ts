import { ComponentFixture, TestBed } from "@angular/core/testing"

import ClassicalMechanics from "./classical-mechanics"

describe("ClassicalMechanics", () => {
  let component: ClassicalMechanics
  let fixture: ComponentFixture<ClassicalMechanics>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassicalMechanics],
    }).compileComponents()

    fixture = TestBed.createComponent(ClassicalMechanics)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
