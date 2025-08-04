import { ComponentFixture, TestBed } from "@angular/core/testing"

import EarthScience from "./earth-science"

describe("EarthScience", () => {
  let component: EarthScience
  let fixture: ComponentFixture<EarthScience>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EarthScience],
    }).compileComponents()

    fixture = TestBed.createComponent(EarthScience)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
