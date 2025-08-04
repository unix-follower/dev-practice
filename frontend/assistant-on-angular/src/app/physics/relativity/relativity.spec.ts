import { ComponentFixture, TestBed } from "@angular/core/testing"

import Relativity from "./relativity"

describe("Relativity", () => {
  let component: Relativity
  let fixture: ComponentFixture<Relativity>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Relativity],
    }).compileComponents()

    fixture = TestBed.createComponent(Relativity)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
