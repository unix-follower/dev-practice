import { ComponentFixture, TestBed } from "@angular/core/testing"

import Trigonometry from "./trigonometry"

describe("Trigonometry", () => {
  let component: Trigonometry
  let fixture: ComponentFixture<Trigonometry>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Trigonometry],
    }).compileComponents()

    fixture = TestBed.createComponent(Trigonometry)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
