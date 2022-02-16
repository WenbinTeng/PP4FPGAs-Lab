package chap4

import chisel3._
import chisel3.experimental.FixedPoint
import scala.math._
import types._

object Cons {
    val dim = 256
    val wid = 16
    val blk = 16

    val cosPhase = VecInit(
        (0 until dim)
            .map(k => -2 * Pi * k / dim.toDouble)
            .map(t => FixedPoint.fromDouble(cos(t), ComplexConfig.Width.W, ComplexConfig.BinaryPoint.BP))
    )
    val sinPhase = VecInit(
        (0 until dim)
            .map(k => -2 * Pi * k / dim.toDouble)
            .map(t => FixedPoint.fromDouble(sin(t), ComplexConfig.Width.W, ComplexConfig.BinaryPoint.BP))
    )
}
