package chap3

import chisel3._

object Cons {
    val pip = 16
    val wid = 16
    val xInit = 0x4dba.S(wid.W)
    val yInit = 0x0000.S(wid.W)
    val phase = VecInit(
        0x2000.S(wid.W),
        0x12e4.S(wid.W),
        0x09fb.S(wid.W),
        0x0511.S(wid.W),
        0x028b.S(wid.W),
        0x0145.S(wid.W),
        0x00a3.S(wid.W),
        0x0051.S(wid.W),
        0x0028.S(wid.W),
        0x0014.S(wid.W),
        0x000a.S(wid.W),
        0x0005.S(wid.W),
        0x0003.S(wid.W),
        0x0001.S(wid.W),
        0x0001.S(wid.W),
        0x0000.S(wid.W)
    )
}

