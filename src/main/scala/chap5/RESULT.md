# Chapter 5 - Fast Fourier Transform

Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- Log2(n)-deep pipeline, n/2 Butterfly Units for a pipeline stage (v1)
- Valid-Ready handshake mode,  n/2 Butterfly Units in total (v2)
- 256-wide sample window
- Fixed point represent: 16-bit wide, 1-bit sign, 10-bit binary point
- Precomputed cos/sin values
- Precomputed reverse indexes
- Gauss Complex Multiplication



## Todo List

- Memory access optimization: iterative computation
- Memory access optimization: distributed BRAM
- Hardware revert bits



## Utilization

|                  | LUT    | FF    | BRAM | DSP  |
| ---------------- | ------ | ----- | ---- | ---- |
| HLS FFT Baseline | 6927   | 2818  | 0    | 36   |
| Chisel FFT v1    | 426336 | 90112 | 0    | 185  |
| Chisel FFT v2    | 462360 | 8340  | 0    | 384  |



## Performance

|                  | Latency(ns) | Period(ns) | Interval |
| ---------------- | ----------- | ---------- | -------- |
| HLS FFT Baseline | >39800      | 50         | >796     |
| Chisel FFT v1    | 450         | 50         | 9        |
| Chisel FFT v2    | 450         | 50         | 9        |

