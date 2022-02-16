package utils

import chisel3._

class VecAdd(dim: Int, wid: Int) extends Module {
    val io = IO(new Bundle {
        val a = Input(Vec(dim, SInt(wid.W)))
        val b = Input(Vec(dim, SInt(wid.W)))
        val c = Output(Vec(dim, SInt(wid.W)))
    })

    for (i <- 0 until dim) {
        io.c(i) := io.a(i) + io.b(i)
    }

}

