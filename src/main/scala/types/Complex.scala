package types

import chisel3._
import chisel3.experimental.FixedPoint
import ComplexConfig._

object ComplexConfig {
    val Width = 16
    val BinaryPoint = 10
}

class Complex extends Bundle {
    val re = FixedPoint(Width.W, BinaryPoint.BP)
    val im = FixedPoint(Width.W, BinaryPoint.BP)

    def +(operand: Complex): Complex = {
        val res = Wire(new Complex)
        res.re := re + operand.re
        res.im := im + operand.im
        res
    }

    def -(operand: Complex): Complex = {
        val res = Wire(new Complex)
        res.re := re - operand.re
        res.im := im - operand.im
        res
    }

    def *(operand: Complex): Complex = {
        val res = Wire(new Complex)
        val k1 = operand.re * (re + im)
        val k2 = re * (operand.im - operand.re)
        val k3 = im * (operand.re + operand.im)
        res.re := k1 - k3
        res.im := k1 + k2
        res
    }

}

