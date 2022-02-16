# Chapter 4 - Discrete Fourier Transform

Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- Valid-Ready handshake mode
- 256-wide sample window
- Fixed point represent: 16-bit wide, 1-bit sign, 10-bit binary point
- Precomputed cos/sin values
- Gauss Complex Multiplication
- Use n-dim pipelined adder trees to compute vector inner products (v1)
- Use a 16-dim pipelined adder trees to compute vector inner products iteratively (v2)



## Todo List

- Memory access optimization: change loop order
- Memory access optimization: distributed BRAM
- Cosine/Sine computation optimization: reuse values instead of using ROM because of the same rotate angle of each row/column



## Utilization

|                  | LUT    | FF    | BRAM  | DSP  |
| ---------------- | ------ | ----- | ----- | ---- |
| HLS DFT Baseline | 41222  | 7044  | 20    | 186  |
| Chisel DFT v1    | 113876 | 44324 | 128.5 | 880  |
| Chisel DFT v2    | 12244  | 9432  | 0     | 48   |



## Performance

|                  | Latency(ns) | Period(ns) | Interval |
| ---------------- | ----------- | ---------- | -------- |
| HLS DFT Baseline | 3290750     | 50         | 65815    |
| Chisel DFT v1    | 13250       | 50         | 265      |
| Chisel DFT v2    | 205100      | 50         | 4102     |
