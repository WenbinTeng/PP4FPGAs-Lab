package chap5

import Cons._
import chisel3._
import chisel3.util._
import types._

class Fft extends Module {
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

    val stage = Reg(UInt(log2Ceil(dim).W))

    switch(state) {
        is(idle) {
            stage := 0.U
        }
        is(work) {
            stage := stage + 1.U
        }
    }

    io.ready := stage === log2Ceil(dim).U

    def ButterflyUnit(in1: Complex, in2: Complex, wn: Complex): (Complex, Complex) = {
        val temp = Wire(new Complex)
        val out1 = Wire(new Complex)
        val out2 = Wire(new Complex)
        temp := in2 * wn
        out1 := in1 + temp
        out2 := in1 - temp
        (out1, out2)
    }

    val res = Reg(Vec(dim, new Complex))

    switch(state) {
        is(idle) {
            res := io.in
        }
        is(work) {
            for (group <- 0 until dim / 2) {
                val groupNumber = group.U(log2Ceil(dim).W) & (~0.U(log2Ceil(dim).W) << stage).asUInt()
                val groupOffset = group.U(log2Ceil(dim).W) & (~(~0.U(log2Ceil(dim).W) << stage << 1.U)).asUInt()
                val index = Wire(UInt(log2Ceil(dim).W))
                val angle = Wire(UInt(log2Ceil(dim).W))
                when(groupOffset < (1.U << stage).asUInt()) {
                    index := (groupNumber << 1.U).asUInt() + groupOffset
                    angle := groupOffset
                }.otherwise {
                    index := (groupNumber << 1.U).asUInt() + groupOffset - (1.U << stage).asUInt()
                    angle := groupOffset - (1.U << stage).asUInt()
                }
                val in1 = res(reverseIndex(index))
                val in2 = res(reverseIndex(index + (1.U << stage).asUInt()))
                val wn = Wire(new Complex)
                wn.re := cosPhase(angle)
                wn.im := sinPhase(angle)
                val t = ButterflyUnit(in1, in2, wn)
                res(reverseIndex(index)) := t._1
                res(reverseIndex(index + (1.U << stage).asUInt())) := t._2
            }
        }
    }

    for (k <- 0 until dim) {
        io.out(k) := res(reverseIndex(k.U))
    }
}

