import { ComponentFixture, TestBed } from "@angular/core/testing"

import StatisticsProbability from "./statistics-probability"

describe("StatisticsProbability", () => {
  let component: StatisticsProbability
  let fixture: ComponentFixture<StatisticsProbability>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticsProbability],
    }).compileComponents()

    fixture = TestBed.createComponent(StatisticsProbability)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it("should create", () => {
    expect(component).toBeTruthy()
  })
})
