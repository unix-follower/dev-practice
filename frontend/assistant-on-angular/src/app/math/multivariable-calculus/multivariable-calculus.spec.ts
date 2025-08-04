import { ComponentFixture, TestBed } from "@angular/core/testing"

import MultivariableCalculus from "./multivariable-calculus"

describe("MultivariableCalculus", () => {
  let component: MultivariableCalculus
  let fixture: ComponentFixture<MultivariableCalculus>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MultivariableCalculus],
    }).compileComponents()

    fixture = TestBed.createComponent(MultivariableCalculus)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
