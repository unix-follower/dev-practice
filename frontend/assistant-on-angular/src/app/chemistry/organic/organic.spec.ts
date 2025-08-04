import { ComponentFixture, TestBed } from "@angular/core/testing"

import Organic from "./organic"

describe("Organic", () => {
  let component: Organic
  let fixture: ComponentFixture<Organic>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Organic],
    }).compileComponents()

    fixture = TestBed.createComponent(Organic)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
