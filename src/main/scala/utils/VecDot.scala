package utils

import chisel3._

class VecDot(dim: Int, wid: Int) extends Module {
    val io = IO(new Bundle {
        val a = Input(Vec(dim, SInt(wid.W)))
        val b = Input(Vec(dim, SInt(wid.W)))
        val c = Output(SInt((wid * 2).W))
    })

    val adderTree = Module(new AdderTree(dim, wid * 2))

    for (i <- 0 until dim) {
        adderTree.io.x(i) := io.a(i) * io.b(i)
    }

    io.c := adderTree.io.y
}

