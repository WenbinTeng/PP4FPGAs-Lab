package utils

import chisel3._

class AdderTree(dim: Int, wid: Int) extends Module {
    val io = IO(new Bundle {
        val x = Input(Vec(dim, SInt(wid.W)))
        val y = Output(SInt(wid.W))
    })

    if (dim == 1) {
        io.y := io.x(0)
    }
    else {
        val aLen = dim >> 1
        val bLen = dim - aLen

        val aVec = RegInit(VecInit(Seq.fill(aLen)(0.S(wid.W))))
        val bVec = RegInit(VecInit(Seq.fill(bLen)(0.S(wid.W))))

        for (i <- 0 until aLen) {
            aVec(i) := io.x(i)
        }
        for (i <- 0 until bLen) {
            bVec(i) := io.x(i + aLen)
        }

        val aAdder = Module(new AdderTree(aLen, wid))
        val bAdder = Module(new AdderTree(bLen, wid))

        aAdder.io.x := aVec
        bAdder.io.x := bVec

        io.y := aAdder.io.y + bAdder.io.y
    }

}

