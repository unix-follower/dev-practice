import { ComponentFixture, TestBed } from "@angular/core/testing"

import Biochemistry from "./biochemistry"

describe("Biochemistry", () => {
  let component: Biochemistry
  let fixture: ComponentFixture<Biochemistry>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Biochemistry],
    }).compileComponents()

    fixture = TestBed.createComponent(Biochemistry)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
