# Chapter 2 - Finite Impulse Response Filters
Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- 4-wide sample window
- 10-bit integer represent
- Non-blocking result



## More Optimization

- Pipeline adder (chap4, chap5)
- Pipeline adder tree (chap4, chap5)
- Complex number computation (chap4, chap5)



## Utilization

|                  | LUT  | FF   | BRAM | DSP  |
| ---------------- | ---- | ---- | ---- | ---- |
| HLS FIR Baseline | 354  | 281  | 0    | 3    |
| Chisel FIR       | 234  | 40   | 0    | 0    |



## Performance

|                  | Latency(ns) | Period(ns) | Interval |
| ---------------- | ----------- | ---------- | -------- |
| HLS FIR Baseline | 170         | 10         | 18       |
| Chisel FIR       | 10          | 10         | 1        |
