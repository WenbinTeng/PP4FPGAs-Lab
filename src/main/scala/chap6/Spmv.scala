package chap6

import Cons._
import chisel3._
import chisel3.util._

class Spmv extends Module {
    val io = IO(new Bundle {
        val valid = Input(Bool())
        val ready = Output(Bool())
        val rowPtr = Input(Vec(nRows + 1, UInt(wid.W)))
        val colIdx = Input(Vec(nCols, UInt(wid.W)))
        val values = Input(Vec(nCols, SInt(wid.W)))
        val x = Input(Vec(nRows, SInt(wid.W)))
        val y = Output(Vec(nRows, SInt(wid.W)))
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

    val ri = Reg(UInt(log2Ceil(nRows + 1).W))
    val ci = Reg(UInt(log2Ceil(nCols).W))
    switch(state) {
        is(idle) {
            ri := 0.U
        }
        is(work) {
            when ((ci + 1.U) === io.rowPtr(ri + 1.U)) {
                ri := ri + 1.U
            }
        }
    }
    switch(state) {
        is(idle) {
            ci := 0.U
        }
        is(work) {
            ci := ci + 1.U
        }
    }
    io.ready := ri === nRows.U && ci === nCols.U

    val res = Reg(Vec(nRows, SInt(wid.W)))
    switch(state) {
        is(idle) {
            res := VecInit(Seq.fill(nRows)(0.S(wid.W)))
        }
        is(work) {
            res(ri) := res(ri) + (io.values(ci) * io.x(io.colIdx(ci)))
        }
    }
    io.y := res

}

