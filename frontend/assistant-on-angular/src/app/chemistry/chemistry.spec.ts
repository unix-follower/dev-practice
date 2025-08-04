import { ComponentFixture, TestBed } from "@angular/core/testing"

import Chemistry from "./chemistry"

describe("Chemistry", () => {
  let component: Chemistry
  let fixture: ComponentFixture<Chemistry>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Chemistry],
    }).compileComponents()

    fixture = TestBed.createComponent(Chemistry)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
