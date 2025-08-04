import { ComponentFixture, TestBed } from "@angular/core/testing"

import Inorganic from "./inorganic"

describe("Inorganic", () => {
  let component: Inorganic
  let fixture: ComponentFixture<Inorganic>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Inorganic],
    }).compileComponents()

    fixture = TestBed.createComponent(Inorganic)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
