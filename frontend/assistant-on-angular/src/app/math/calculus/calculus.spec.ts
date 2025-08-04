import { ComponentFixture, TestBed } from "@angular/core/testing"

import Calculus from "./calculus"

describe("Calculus", () => {
  let component: Calculus
  let fixture: ComponentFixture<Calculus>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Calculus],
    }).compileComponents()

    fixture = TestBed.createComponent(Calculus)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
