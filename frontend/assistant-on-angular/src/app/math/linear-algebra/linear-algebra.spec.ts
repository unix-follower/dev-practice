import { ComponentFixture, TestBed } from "@angular/core/testing"

import LinearAlgebra from "./linear-algebra"

describe("LinearAlgebra", () => {
  let component: LinearAlgebra
  let fixture: ComponentFixture<LinearAlgebra>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinearAlgebra],
    }).compileComponents()

    fixture = TestBed.createComponent(LinearAlgebra)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
