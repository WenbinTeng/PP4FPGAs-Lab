# Chapter 7 -  Sparse Matrix Vector Multiplication

Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- 16-bit values
- 4-dim matrix and vector
- Execute a multiplication and a accumulation in a single cycle
- The hardware structure is closely related to the shape of matrix and the values in matrix



## Utilization

|                   | LUT  | FF   | BRAM | DSP  |
| ----------------- | ---- | ---- | ---- | ---- |
| HLS SPMV Baseline | 1274 | 811  | 0    | 8    |
| Chisel SPMV       | 127  | 72   | 0    | 1    |



## Performance

|                   | Latency(ns) | Period(ns) | Interval |
| ----------------- | ----------- | ---------- | -------- |
| HLS SPMV Baseline | >2350       | 50         | >47      |
| Chisel SPMV       | 450         | 50         | 9        |

