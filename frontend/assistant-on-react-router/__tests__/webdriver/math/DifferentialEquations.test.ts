import { Builder, Browser, By, until, WebDriver } from "selenium-webdriver"
import { baseUrl } from "/__tests__/constants"

describe("DifferentialEquations Chrome WebDriver Tests", () => {
  let driver: WebDriver

  beforeAll(async () => {
    driver = await new Builder().forBrowser(Browser.CHROME).build()
  })

  afterAll(async () => {
    await driver.quit()
  })

  test("should render the DifferentialEquations placeholder text", async () => {
    await driver.get(`${baseUrl}/math/differential-equations`)

    const placeholder = "DifferentialEquations placeholder"

    const placeholderElement = await driver.wait(
      until.elementLocated(By.xpath(`//span[text()='${placeholder}']`)),
      5000, // 5 seconds timeout
    )

    expect(await placeholderElement.isDisplayed()).toBe(true)

    const text = await placeholderElement.getText()
    expect(text).toBe(placeholder)
  })
})
