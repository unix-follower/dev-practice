import express from "express"
import { Request, Response, NextFunction } from "express"

const router = express.Router()

// eslint-disable-next-line @typescript-eslint/no-unused-vars
router.get("/", function (req: Request, res: Response, next: NextFunction) {
  // res.render('index', { title: 'Express' });
  res.send("respond with a resource")
})

export default router
