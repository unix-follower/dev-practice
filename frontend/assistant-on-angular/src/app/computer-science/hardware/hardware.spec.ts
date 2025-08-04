import { ComponentFixture, TestBed } from "@angular/core/testing"

import Hardware from "./hardware"

describe("Hardware", () => {
  let component: Hardware
  let fixture: ComponentFixture<Hardware>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Hardware],
    }).compileComponents()

    fixture = TestBed.createComponent(Hardware)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
