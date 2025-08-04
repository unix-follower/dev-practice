import { ComponentFixture, TestBed } from "@angular/core/testing"

import Biology from "./biology"

describe("Biology", () => {
  let component: Biology
  let fixture: ComponentFixture<Biology>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Biology],
    }).compileComponents()

    fixture = TestBed.createComponent(Biology)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
