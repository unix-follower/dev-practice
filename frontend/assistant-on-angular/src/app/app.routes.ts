import { Routes } from "@angular/router"
import { App } from "./app"
import MathHome from "@/app/math/math-home"
import Algebra from "@/app/math/algebra/algebra"
import Geometry from "@/app/math/geometry/geometry"
import Geometry3dEditor from "@/app/math/geometry/geometry3d-editor/geometry3d-editor"
import Trigonometry from "@/app/math/trigonometry/trigonometry"
import Precalculus from "@/app/math/precalculus/precalculus"
import Calculus from "@/app/math/calculus/calculus"
import LinearAlgebra from "@/app/math/linear-algebra/linear-algebra"
import MultivariableCalculus from "@/app/math/multivariable-calculus/multivariable-calculus"
import StatisticsProbability from "@/app/math/statistics-probability/statistics-probability"
import DifferentialEquations from "@/app/math/differential-equations/differential-equations"
import Chemistry from "@/app/chemistry/chemistry"
import Biochemistry from "@/app/chemistry/biochemistry/biochemistry"
import Inorganic from "@/app/chemistry/inorganic/inorganic"
import Organic from "@/app/chemistry/organic/organic"
import PubChemFoodAdditives from "@/app/chemistry/organic/dbs/pub-chem-food-additives/PubChemFoodAdditives"
import Physical from "@/app/chemistry/physical/physical"
import Physics from "@/app/physics/physics"
import ClassicalMechanics from "@/app/physics/classical-mechanics/classical-mechanics"
import Electromagnetism from "@/app/physics/electromagnetism/electromagnetism"
import ThermodynamicsAndStatisticalMechanics from "@/app/physics/thermodynamics-and-statistical-mechanics/thermodynamics-and-statistical-mechanics"
import QuantumMechanics from "@/app/physics/quantum-mechanics/quantum-mechanics"
import Relativity from "@/app/physics/relativity/relativity"
import Biology from "@/app/biology/biology"
import Finance from "@/app/finance/finance"
import StockMarket from "@/app/finance/stock-market/stock-market"
import Lang from "@/app/lang/lang"
import English from "@/app/lang/english/english"
import Spanish from "@/app/lang/spanish/spanish"
import Chinese from "@/app/lang/chinese/chinese"
import Economics from "@/app/economics/economics"
import Electronics from "@/app/electronics/electronics"
import Music from "@/app/music/music"
import Astronomy from "@/app/astronomy/astronomy"
import Vehicles from "@/app/vehicles/vehicles"
import AutoMechanic from "@/app/auto-mechanic/auto-mechanic"
import WeaponsAndArmor from "@/app/weapons-and-armor/weapons-and-armor"
import ComputerScience from "@/app/computer-science/computer-science"
import Dsa from "@/app/computer-science/dsa/dsa"
import ProgrammingLang from "@/app/computer-science/programming-lang/programming-lang"
import Hardware from "@/app/computer-science/hardware/hardware"
import Ai from "@/app/computer-science/ai/ai"
import Cybersecurity from "@/app/computer-science/cybersecurity/cybersecurity"
import Net from "@/app/computer-science/net/net"
import Database from "@/app/computer-science/database/database"
import EarthScience from "@/app/earth-science/earth-science"
import Cooking from "@/app/cooking/cooking"

const mathRoute = {
  path: "math",
  component: MathHome,
  children: [
    {
      path: "algebra",
      component: Algebra,
    },
    {
      path: "geometry",
      component: Geometry,
      children: [
        {
          path: "3d-editor",
          component: Geometry3dEditor,
        },
      ],
    },
    {
      path: "trigonometry",
      component: Trigonometry,
    },
    {
      path: "precalculus",
      component: Precalculus,
    },
    {
      path: "calculus",
      component: Calculus,
    },
    {
      path: "linear-algebra",
      component: LinearAlgebra,
    },
    {
      path: "multivariable-calculus",
      component: MultivariableCalculus,
    },
    {
      path: "statistics-probability",
      component: StatisticsProbability,
    },
    {
      path: "differential-equations",
      component: DifferentialEquations,
    },
  ],
}

const chemistryRoute = {
  path: "chemistry",
  component: Chemistry,
  children: [
    {
      path: "biochemistry",
      component: Biochemistry,
    },
    {
      path: "organic",
      component: Organic,
      children: [
        {
          path: "dbs/pub-chem/food-additives",
          component: PubChemFoodAdditives,
        },
      ],
    },
    {
      path: "inorganic",
      component: Inorganic,
    },
    {
      path: "physical",
      component: Physical,
    },
  ],
}

const physicsRoute = {
  path: "physics",
  component: Physics,
  children: [
    {
      path: "classical-mechanics",
      component: ClassicalMechanics,
      children: [],
    },
    {
      path: "electromagnetism",
      component: Electromagnetism,
    },
    {
      path: "thermodynamics-and-statistical-mechanics",
      component: ThermodynamicsAndStatisticalMechanics,
    },
    {
      path: "quantum-mechanics",
      component: QuantumMechanics,
    },
    {
      path: "relativity",
      component: Relativity,
    },
  ],
}

const biologyRoute = {
  path: "biology",
  component: Biology,
}

const financeRoute = {
  path: "finance",
  component: Finance,
  children: [
    {
      path: "stock-market",
      component: StockMarket,
      children: [],
    },
  ],
}

const langRoute = {
  path: "lang",
  component: Lang,
  children: [
    {
      path: "english",
      component: English,
    },
    {
      path: "spanish",
      component: Spanish,
    },
    {
      path: "chinese",
      component: Chinese,
    },
  ],
}

const economicsRoute = {
  path: "economics",
  component: Economics,
}

const electronicsRoute = {
  path: "electronics",
  component: Electronics,
}

const musicRoute = {
  path: "music",
  component: Music,
}

const astronomyRoute = {
  path: "astronomy",
  component: Astronomy,
}

const vehiclesRoute = {
  path: "vehicles",
  component: Vehicles,
}

const autoMechanicRoute = {
  path: "auto-mechanic",
  component: AutoMechanic,
}

const weaponsAndArmorRoute = {
  path: "weapons-and-armor",
  component: WeaponsAndArmor,
}

const computerScienceRoute = {
  path: "computer-science",
  component: ComputerScience,
  children: [
    {
      path: "dsa",
      component: Dsa,
    },
    {
      path: "programming-lang",
      component: ProgrammingLang,
    },
    {
      path: "hardware",
      component: Hardware,
    },
    {
      path: "ai",
      component: Ai,
    },
    {
      path: "cybersecurity",
      component: Cybersecurity,
    },
    {
      path: "net",
      component: Net,
    },
    {
      path: "database",
      component: Database,
    },
  ],
}

const earthScienceRoute = {
  path: "earth-science",
  component: EarthScience,
}

const cookingRoute = {
  path: "cooking",
  component: Cooking,
}

export const routes: Routes = [
  {
    path: "",
    component: App,
    title: "Assistant on Angular",
  },
  mathRoute,
  chemistryRoute,
  physicsRoute,
  biologyRoute,
  financeRoute,
  langRoute,
  economicsRoute,
  electronicsRoute,
  musicRoute,
  astronomyRoute,
  vehiclesRoute,
  autoMechanicRoute,
  weaponsAndArmorRoute,
  computerScienceRoute,
  earthScienceRoute,
  cookingRoute,
]
