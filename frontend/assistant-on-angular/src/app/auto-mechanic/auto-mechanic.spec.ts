import { ComponentFixture, TestBed } from "@angular/core/testing"

import AutoMechanic from "./auto-mechanic"

describe("AutoMechanic", () => {
  let component: AutoMechanic
  let fixture: ComponentFixture<AutoMechanic>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AutoMechanic],
    }).compileComponents()

    fixture = TestBed.createComponent(AutoMechanic)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
