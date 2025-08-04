import { ComponentFixture, TestBed } from "@angular/core/testing"

import ComputerScience from "./computer-science"

describe("ComputerScience", () => {
  let component: ComputerScience
  let fixture: ComponentFixture<ComputerScience>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComputerScience],
    }).compileComponents()

    fixture = TestBed.createComponent(ComputerScience)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
