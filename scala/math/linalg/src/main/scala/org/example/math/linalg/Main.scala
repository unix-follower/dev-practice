package org.example.math.linalg

import org.example.math.linalg.example.Example2
import org.example.math.linalg.example.Example3

object Main {
    def main(args: Array[String]): Unit = {
        val isExecuteAll = () => args(0).toBoolean
        if (args.length > 0 && isExecuteAll()) {
            Example2.executeWithCommonsMathLUDecomposition()
            Example3.executeWithCommonsMathLUDecomposition()
        } else {
            Example3.executeWithCommonsMathLUDecomposition()
        }
    }
}
