import { ComponentFixture, TestBed } from "@angular/core/testing"

import Chinese from "./chinese"

describe("Chinese", () => {
  let component: Chinese
  let fixture: ComponentFixture<Chinese>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Chinese],
    }).compileComponents()

    fixture = TestBed.createComponent(Chinese)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
