package chap4

import Cons._
import chisel3._
import chisel3.util._
import types.{Complex, ComplexConfig}
import utils.AdderTree

class Dft extends Module {
    val io = IO(new Bundle {
        val valid = Input(Bool())
        val ready = Output(Bool())
        val in = Input(Vec(dim, new Complex))
        val out = Output(Vec(dim, new Complex))
    })

    val idle :: work :: Nil = Enum(2)
    val state = RegInit(idle)

    switch(state) {
        is(idle) {
            when(io.valid) {
                state := work
            }
        }
        is(work) {
            when(io.ready) {
                state := idle
            }
        }
    }

    val row = RegInit(0.U(32.W))
    val col = RegInit(0.U(32.W))

    switch(state) {
        is(idle) {
            row := 0.U
        }
        is(work) {
            when (row === dim.U) {
                row := 0.U
            }.elsewhen(col + blk.U >= dim.U) {
                row := row + 1.U
            }
        }
    }

    switch(state) {
        is(idle) {
            col := 0.U
        }
        is(work) {
            when (col + blk.U >= dim.U) {
                col := 0.U
            }.otherwise {
                col := col + blk.U
            }
        }
    }

    val pipIndex = Reg(Vec(log2Ceil(blk), UInt(32.W)))
    for (k <- 0 until log2Ceil(blk)) {
        if (k == 0) {
            pipIndex(k) := row
        }
        else {
            pipIndex(k) := pipIndex(k - 1)
        }
    }
    io.ready := pipIndex.last === dim.U

    val angle = Wire(Vec(blk, new Complex))
    for (k <- 0 until blk) {
        angle(k).re := cosPhase(row * (col + k.U))
        angle(k).im := sinPhase(row * (col + k.U))
    }

    val realAdder = Module(new AdderTree(blk, wid))
    val imagAdder = Module(new AdderTree(blk, wid))
    when (state === work) {
        for (k <- 0 until blk) {
            val mul = io.in(col + k.U) * angle(k)
            realAdder.io.x(k) := mul.re.asSInt()
            imagAdder.io.x(k) := mul.im.asSInt()
        }
    }.otherwise {
        realAdder.io.x := VecInit(Seq.fill(blk)(0.S(wid.W)))
        imagAdder.io.x := VecInit(Seq.fill(blk)(0.S(wid.W)))
    }

    val res = Reg(Vec(dim, new Complex))
    switch(state) {
        is(idle) {
            res := VecInit(Seq.fill(dim)(0.U.asTypeOf(new Complex)))
        }
        is(work) {
            res(pipIndex.last).re := res(pipIndex.last).re + realAdder.io.y.asFixedPoint(ComplexConfig.BinaryPoint.BP)
            res(pipIndex.last).im := res(pipIndex.last).im + imagAdder.io.y.asFixedPoint(ComplexConfig.BinaryPoint.BP)
        }
    }
    io.out := res
}

