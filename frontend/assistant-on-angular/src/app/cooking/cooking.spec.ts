import { ComponentFixture, TestBed } from "@angular/core/testing"

import Cooking from "./cooking"

describe("Cooking", () => {
  let component: Cooking
  let fixture: ComponentFixture<Cooking>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Cooking],
    }).compileComponents()

    fixture = TestBed.createComponent(Cooking)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
