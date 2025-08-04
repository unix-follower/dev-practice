import { ComponentFixture, TestBed } from "@angular/core/testing"

import Algebra from "./algebra"

describe("Algebra", () => {
  let component: Algebra
  let fixture: ComponentFixture<Algebra>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Algebra],
    }).compileComponents()

    fixture = TestBed.createComponent(Algebra)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
