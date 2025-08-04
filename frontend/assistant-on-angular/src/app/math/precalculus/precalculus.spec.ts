import { ComponentFixture, TestBed } from "@angular/core/testing"

import Precalculus from "./precalculus"

describe("Precalculus", () => {
  let component: Precalculus
  let fixture: ComponentFixture<Precalculus>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Precalculus],
    }).compileComponents()

    fixture = TestBed.createComponent(Precalculus)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
