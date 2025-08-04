import { ComponentFixture, TestBed } from "@angular/core/testing"

import Economics from "./economics"

describe("Economics", () => {
  let component: Economics
  let fixture: ComponentFixture<Economics>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Economics],
    }).compileComponents()

    fixture = TestBed.createComponent(Economics)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
