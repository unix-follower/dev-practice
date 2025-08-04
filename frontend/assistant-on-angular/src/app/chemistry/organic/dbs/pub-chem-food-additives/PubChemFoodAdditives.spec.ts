import { ComponentFixture, TestBed } from "@angular/core/testing"

import PubChemFoodAdditives from "./PubChemFoodAdditives"

describe("PubChemFoodAdditives", () => {
  let component: PubChemFoodAdditives
  let fixture: ComponentFixture<PubChemFoodAdditives>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PubChemFoodAdditives],
    }).compileComponents()

    fixture = TestBed.createComponent(PubChemFoodAdditives)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
