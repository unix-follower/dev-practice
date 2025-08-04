import { ComponentFixture, TestBed } from "@angular/core/testing"

import ThermodynamicsAndStatisticalMechanics from "./thermodynamics-and-statistical-mechanics"

describe("ThermodynamicsAndStatisticalMechanics", () => {
  let component: ThermodynamicsAndStatisticalMechanics
  let fixture: ComponentFixture<ThermodynamicsAndStatisticalMechanics>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ThermodynamicsAndStatisticalMechanics],
    }).compileComponents()

    fixture = TestBed.createComponent(ThermodynamicsAndStatisticalMechanics)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
