package chap3

import chisel3._
import chisel3.experimental.FixedPoint
import chisel3.util._
import Cons._

class Cordic extends Module {
    val io = IO(new Bundle {
        val angle = Input(UInt(wid.W))
        val cos = Output(FixedPoint(wid.W, (wid - 1).BP))
        val sin = Output(FixedPoint(wid.W, (wid - 1).BP))
    })

    val x = Reg(Vec(pip, SInt(wid.W)))
    val y = Reg(Vec(pip, SInt(wid.W)))
    val z = Reg(Vec(pip, SInt(wid.W)))
    val q = Reg(Vec(pip, UInt(2.W)))

    x(0) := xInit
    y(0) := yInit
    z(0) := Cat(0.U(2.W), io.angle(wid - 3, 0)).asSInt()
    q(0) := io.angle(wid - 1, wid - 2)

    for (i <- 1 until pip) {
        when(z(i - 1)(wid - 1).asBool()) {
            x(i) := x(i - 1) + (y(i - 1) >> (i - 1)).asSInt()
            y(i) := y(i - 1) - (x(i - 1) >> (i - 1)).asSInt()
            z(i) := z(i - 1) + Cons.phase(i - 1)
            q(i) := q(i - 1)
        }.otherwise {
            x(i) := x(i - 1) - (y(i - 1) >> (i - 1)).asSInt()
            y(i) := y(i - 1) + (x(i - 1) >> (i - 1)).asSInt()
            z(i) := z(i - 1) - Cons.phase(i - 1)
            q(i) := q(i - 1)
        }
    }

    io.cos := MuxLookup(q(pip - 1), 0.S(wid.W), Array(
        0.U(2.W) ->  x(pip - 1),
        1.U(2.W) -> -y(pip - 1),
        2.U(2.W) -> -x(pip - 1),
        3.U(2.W) ->  y(pip - 1)
    )).asFixedPoint((wid - 1).BP)
    io.sin := MuxLookup(q(pip - 1), 0.S(wid.W), Array(
        0.U(2.W) ->  y(pip - 1),
        1.U(2.W) ->  x(pip - 1),
        2.U(2.W) -> -y(pip - 1),
        3.U(2.W) -> -x(pip - 1)
    )).asFixedPoint((wid - 1).BP)
}

