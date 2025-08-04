import { ComponentFixture, TestBed } from "@angular/core/testing"

import Physics from "./physics"

describe("Physics", () => {
  let component: Physics
  let fixture: ComponentFixture<Physics>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Physics],
    }).compileComponents()

    fixture = TestBed.createComponent(Physics)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
