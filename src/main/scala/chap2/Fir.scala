package chap2

import Cons._
import chisel3._

class Fir extends Module {
    val io = IO(new Bundle {
        val c = Input(Vec(len, SInt(wid.W)))
        val x = Input(SInt(wid.W))
        val y = Output(SInt(wid.W))
    })

    val shiftReg = Reg(Vec(len, SInt(wid.W)))

    shiftReg(0) := io.x
    for (i <- 1 until len) {
        shiftReg(i) := shiftReg(i - 1)
    }

    io.y := shiftReg.zip(io.c).map { case (a: SInt, b: SInt) => a * b }.reduce((a: SInt, b: SInt) => a + b)

}

