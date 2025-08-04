import { ComponentFixture, TestBed } from "@angular/core/testing"

import Net from "./net"

describe("Net", () => {
  let component: Net
  let fixture: ComponentFixture<Net>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Net],
    }).compileComponents()

    fixture = TestBed.createComponent(Net)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
