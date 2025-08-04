import { ComponentFixture, TestBed } from "@angular/core/testing"

import Astronomy from "./astronomy"

describe("Astronomy", () => {
  let component: Astronomy
  let fixture: ComponentFixture<Astronomy>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Astronomy],
    }).compileComponents()

    fixture = TestBed.createComponent(Astronomy)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
