package chap5

import chisel3._
import chisel3.experimental.FixedPoint
import chisel3.util._
import types._

import scala.math._

object Cons {
    val dim = 256
    val wid = 16

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

    val reverseIndex = VecInit((0 until dim).map(i => revertBits(i).U(log2Ceil(dim).W)))

    def revertBits(n: Int): Int = {
        val len = log2Ceil(dim)
        var src = n
        var dst = 0
        var ptr = 1 << (len - 1)
        for (_ <- 0 until len) {
            if ((src & 1) != 0) {
                dst = dst | ptr
            }
            src = src >> 1
            ptr = ptr >> 1
        }
        dst
    }
}
