import { ComponentFixture, TestBed } from "@angular/core/testing"

import QuantumMechanics from "./quantum-mechanics"

describe("QuantumMechanics", () => {
  let component: QuantumMechanics
  let fixture: ComponentFixture<QuantumMechanics>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuantumMechanics],
    }).compileComponents()

    fixture = TestBed.createComponent(QuantumMechanics)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
