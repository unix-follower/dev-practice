import { ComponentFixture, TestBed } from "@angular/core/testing"

import WeaponsAndArmor from "./weapons-and-armor"

describe("WeaponsAndArmor", () => {
  let component: WeaponsAndArmor
  let fixture: ComponentFixture<WeaponsAndArmor>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WeaponsAndArmor],
    }).compileComponents()

    fixture = TestBed.createComponent(WeaponsAndArmor)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
