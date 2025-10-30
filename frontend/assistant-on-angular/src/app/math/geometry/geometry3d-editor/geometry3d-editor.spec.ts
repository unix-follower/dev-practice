import { ComponentFixture, TestBed } from "@angular/core/testing"

import Geometry3dEditor from "./geometry3d-editor"

describe("Geometry3dEditor", () => {
  let component: Geometry3dEditor
  let fixture: ComponentFixture<Geometry3dEditor>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Geometry3dEditor],
    }).compileComponents()

    fixture = TestBed.createComponent(Geometry3dEditor)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
