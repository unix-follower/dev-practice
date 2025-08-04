import { ComponentFixture, TestBed } from "@angular/core/testing"

import Electromagnetism from "./electromagnetism"

describe("Electromagnetism", () => {
  let component: Electromagnetism
  let fixture: ComponentFixture<Electromagnetism>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Electromagnetism],
    }).compileComponents()

    fixture = TestBed.createComponent(Electromagnetism)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
