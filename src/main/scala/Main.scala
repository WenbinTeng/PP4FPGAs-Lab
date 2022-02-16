import chisel3._
import chap2.Fir
import chap3.Cordic
import chap4.Dft
import chap5.Fft
import utils._

class TopModule extends MultiIOModule {

}


object Main extends App {
    Driver.execute(Array[String]("--target-dir", "build"), () => new Fft)
}

