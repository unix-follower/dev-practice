import Footer from "~/components/common/Footer"
import "~/components/physics/newtons-cradle.css"
import "~/components/physics/breaking-circle.css"

function Navbar() {
  return (
    <header>
      <nav>
        <span>Conversion of momentum</span>
      </nav>
    </header>
  )
}

export default function Page() {
  return (
    <>
      <Navbar />
      <div className="pendulum">
        <div className="pendulum-box">
          <div className="ball first"></div>
          <div className="ball last"></div>
        </div>
      </div>
      <div className="circle-container">
        <div className="circle">
          <div className="circle-half-left"></div>
          <div className="circle-half-right"></div>
        </div>
      </div>
      <Footer />
    </>
  )
}
