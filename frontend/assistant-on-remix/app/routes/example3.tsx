import type { MetaFunction } from "@remix-run/node"
import { Link, Outlet } from "@remix-run/react"

export const meta: MetaFunction = () => {
  return [{ title: "Example 3" }, { name: "description", content: "Linear Algebra" }]
}

const exampleUrls = [
  "/example3/canvasjs",
  "/example3/observable-plot",
  "/example3/plotly"
]

export default function Example_1_7() {
  return (
    <div>
      <ul>
        {exampleUrls.map((text) => (
          <Link to={text} key={text}>
            <li>{text}</li>
          </Link>
        ))}
      </ul>
      <Outlet />
    </div>
  )
}
