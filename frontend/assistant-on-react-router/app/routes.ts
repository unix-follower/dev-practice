import { type RouteConfig, route, layout, index, prefix } from "@react-router/dev/routes"

const mathRoutes = prefix("/math", [
  layout("./math/layout.tsx", [
    index("./math/Home.tsx"),
    route("algebra", "./math/Algebra.tsx"),
    route("geometry", "./math/Geometry.tsx"),
    route("trigonometry", "./math/Trigonometry.tsx"),
    route("precalculus", "./math/Precalculus.tsx"),
    route("calculus", "./math/Calculus.tsx"),
    route("linear-algebra", "./math/LinearAlgebra.tsx"),
    route("multivariable-calculus", "./math/MultivariableCalculus.tsx"),
    route("statistics-probability", "./math/StatisticsProbability.tsx"),
    route("differential-equations", "./math/DifferentialEquations.tsx"),
  ]),
])

const chemistryRoutes = prefix("/chemistry", [
  layout("./chemistry/layout.tsx", [index("./chemistry/Home.tsx")]),

  ...prefix("biochemistry", [
    layout("./chemistry/biochemistry/layout.tsx", [index("./chemistry/biochemistry/Home.tsx")]),
  ]),

  ...prefix("organic", [
    layout("./chemistry/organic/layout.tsx", [
      index("./chemistry/organic/Home.tsx"),
      route("dbs/pub-chem/food-additives", "./chemistry/organic/dbs/pub-chem/PubChemFoodAdditives.tsx"),
    ]),
  ]),

  ...prefix("inorganic", [layout("./chemistry/inorganic/layout.tsx", [index("./chemistry/inorganic/Home.tsx")])]),

  ...prefix("physical", [layout("./chemistry/physical/layout.tsx", [index("./chemistry/physical/Home.tsx")])]),
])

const physicsRoutes = prefix("/physics", [
  layout("./physics/layout.tsx", [index("./physics/Home.tsx")]),

  ...prefix("classical-mechanics", [
    layout("./physics/classical-mechanics/layout.tsx", [index("./physics/classical-mechanics/Home.tsx")]),
  ]),

  ...prefix("electromagnetism", [
    layout("./physics/electromagnetism/layout.tsx", [index("./physics/electromagnetism/Home.tsx")]),
  ]),

  ...prefix("thermodynamics-and-statistical-mechanics", [
    layout("./physics/thermodynamics-and-statistical-mechanics/layout.tsx", [
      index("./physics/thermodynamics-and-statistical-mechanics/Home.tsx"),
    ]),
  ]),

  ...prefix("quantum-mechanics", [
    layout("./physics/quantum-mechanics/layout.tsx", [index("./physics/quantum-mechanics/Home.tsx")]),
  ]),

  ...prefix("relativity", [layout("./physics/relativity/layout.tsx", [index("./physics/relativity/Home.tsx")])]),
])

const biologyRoutes = prefix("/biology", [layout("./biology/layout.tsx", [index("./biology/Home.tsx")])])

const financeRoutes = prefix("/finance", [
  layout("./finance/layout.tsx", [index("./finance/Home.tsx"), route("stock-market", "./finance/StockMarket.tsx")]),
])

const langRoutes = prefix("/lang", [
  layout("./lang/layout.tsx", [
    index("./lang/Home.tsx"),
    route("english", "./lang/English.tsx"),
    route("spanish", "./lang/Spanish.tsx"),
    route("chinese", "./lang/Chinese.tsx"),
  ]),
])

const economicsRoutes = prefix("/economics", [layout("./economics/layout.tsx", [index("./economics/Home.tsx")])])

const electronicsRoutes = prefix("/electronics", [
  layout("./electronics/layout.tsx", [index("./electronics/Home.tsx")]),
])

const musicRoutes = prefix("/music", [layout("./music/layout.tsx", [index("./music/Home.tsx")])])

const astronomyRoutes = prefix("/astronomy", [layout("./astronomy/layout.tsx", [index("./astronomy/Home.tsx")])])

const vehiclesRoutes = prefix("/vehicles", [layout("./vehicles/layout.tsx", [index("./vehicles/Home.tsx")])])

const autoMechanicRoutes = prefix("/auto-mechanic", [
  layout("./auto-mechanic/layout.tsx", [index("./auto-mechanic/Home.tsx")]),
])

const weaponsAndArmorRoutes = prefix("/weapons-and-armor", [
  layout("./weapons-and-armor/layout.tsx", [index("./weapons-and-armor/Home.tsx")]),
])

const computerScienceRoutes = prefix("/computer-science", [
  layout("./computer-science/layout.tsx", [index("./computer-science/Home.tsx")]),

  ...prefix("dsa", [layout("./computer-science/dsa/layout.tsx", [index("./computer-science/dsa/Home.tsx")])]),

  ...prefix("programming-lang", [
    layout("./computer-science/programming-lang/layout.tsx", [index("./computer-science/programming-lang/Home.tsx")]),
  ]),

  ...prefix("hardware", [
    layout("./computer-science/hardware/layout.tsx", [index("./computer-science/hardware/Home.tsx")]),
  ]),

  ...prefix("ai", [layout("./computer-science/ai/layout.tsx", [index("./computer-science/ai/Home.tsx")])]),

  ...prefix("cybersecurity", [
    layout("./computer-science/cybersecurity/layout.tsx", [index("./computer-science/cybersecurity/Home.tsx")]),
  ]),

  ...prefix("net", [layout("./computer-science/net/layout.tsx", [index("./computer-science/net/Home.tsx")])]),

  ...prefix("database", [
    layout("./computer-science/database/layout.tsx", [index("./computer-science/database/Home.tsx")]),
  ]),
])

const earthScienceRoutes = prefix("/earth-science", [
  layout("./earth-science/layout.tsx", [index("./earth-science/Home.tsx")]),
])

const cookingRoutes = prefix("/cooking", [layout("./cooking/layout.tsx", [index("./cooking/Home.tsx")])])

export default [
  ...mathRoutes,
  ...chemistryRoutes,
  ...physicsRoutes,
  ...biologyRoutes,
  ...financeRoutes,
  ...langRoutes,
  ...economicsRoutes,
  ...electronicsRoutes,
  ...musicRoutes,
  ...astronomyRoutes,
  ...vehiclesRoutes,
  ...autoMechanicRoutes,
  ...weaponsAndArmorRoutes,
  ...computerScienceRoutes,
  ...earthScienceRoutes,
  ...cookingRoutes,
] satisfies RouteConfig
