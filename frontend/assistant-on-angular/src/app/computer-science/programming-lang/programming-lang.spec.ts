import { ComponentFixture, TestBed } from "@angular/core/testing"

import ProgrammingLang from "./programming-lang"

describe("ProgrammingLang", () => {
  let component: ProgrammingLang
  let fixture: ComponentFixture<ProgrammingLang>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgrammingLang],
    }).compileComponents()

    fixture = TestBed.createComponent(ProgrammingLang)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
