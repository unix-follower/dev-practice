import { ComponentFixture, TestBed } from "@angular/core/testing"

import Lang from "./lang"

describe("Lang", () => {
  let component: Lang
  let fixture: ComponentFixture<Lang>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Lang],
    }).compileComponents()

    fixture = TestBed.createComponent(Lang)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
