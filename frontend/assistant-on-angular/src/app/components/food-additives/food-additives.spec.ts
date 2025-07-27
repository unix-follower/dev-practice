import { ComponentFixture, TestBed } from "@angular/core/testing"

import FoodAdditives from "./food-additives"

describe("FoodAdditives", () => {
  let component: FoodAdditives
  let fixture: ComponentFixture<FoodAdditives>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FoodAdditives],
    }).compileComponents()

    fixture = TestBed.createComponent(FoodAdditives)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
