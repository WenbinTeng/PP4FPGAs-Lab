package chap7

import Cons._
import chisel3._
import chisel3.util._

class MatMul extends Module {
    val io = IO(new Bundle {
        val a = Input(Vec(dim, SInt(wid.W)))
        val b = Input(Vec(dim, SInt(wid.W)))
        val c = Output(Vec(dim, Vec(dim, SInt(wid.W))))
    })

    val pe = for (_ <- 0 until dim) yield {
        for (_ <- 0 until dim) yield {
            Module(new PE)
        }
    }

    for (i <- 0 until dim) {
        for (j <- 0 until dim) {
            if (i == 0 && j == 0) {
                pe(i)(j).io.ai := io.a(i)
                pe(i)(j).io.bi := io.b(j)
                pe(i)(j).io.ci := 1.U
            }
            else if (i == 0) {
                pe(i)(j).io.ai := pe(i)(j - 1).io.ao
                pe(i)(j).io.bi := io.b(j)
                pe(i)(j).io.ci := pe(i)(j - 1).io.co
            }
            else if (j == 0) {
                pe(i)(j).io.ai := io.a(i)
                pe(i)(j).io.bi := pe(i - 1)(j).io.bo
                pe(i)(j).io.ci := pe(i - 1)(j).io.co
            }
            else {
                pe(i)(j).io.ai := pe(i)(j - 1).io.ao
                pe(i)(j).io.bi := pe(i - 1)(j).io.bo
                pe(i)(j).io.ci := pe(i)(j - 1).io.co & pe(i - 1)(j).io.co
            }
        }
    }

    for (i <- 0 until dim) {
        for (j <- 0 until dim) {
            io.c(i)(j) := pe(i)(j).io.acc
        }
    }

}

class PE extends Module {
    val io = IO(new Bundle {
        val ai = Input(SInt(wid.W))
        val bi = Input(SInt(wid.W))
        val ci = Input(UInt(1.W))
        val ao = Output(SInt(wid.W))
        val bo = Output(SInt(wid.W))
        val co = Output(UInt(1.W))
        val acc = Output(SInt(wid.W))
    })

    val acc = RegInit(0.S(wid.W))
    val cnt = RegInit(0.U(log2Ceil(dim).W))
    val ar = Reg(SInt(wid.W))
    val br = Reg(SInt(wid.W))
    val cr = Reg(UInt(1.W))

    when (reset.asBool()) {
        ar := 0.S
        br := 0.S
        cr := 0.U
    }.otherwise {
        ar := io.ai
        br := io.bi
        cr := io.ci
    }

    when (cnt === (dim - 1).U) {
        cnt := 0.U
    }.otherwise {
        cnt := cnt + io.ci
    }

    when (cnt === 0.U) {
        acc := io.ai * io.bi
    }.otherwise {
        acc := acc + io.ai * io.bi
    }

    io.acc := acc
    io.ao := ar
    io.bo := br
    io.co := cr

}

