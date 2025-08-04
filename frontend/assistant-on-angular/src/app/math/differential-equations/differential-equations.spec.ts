import { ComponentFixture, TestBed } from "@angular/core/testing"

import DifferentialEquations from "./differential-equations"

describe("DifferentialEquations", () => {
  let component: DifferentialEquations
  let fixture: ComponentFixture<DifferentialEquations>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DifferentialEquations],
    }).compileComponents()

    fixture = TestBed.createComponent(DifferentialEquations)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
