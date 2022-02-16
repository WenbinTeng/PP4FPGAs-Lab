# Chapter 3 - CORDIC
Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- 16 pipeline stages
- 16-bit fixed point with 15 binary points



## Quantization

- Angle: <img src="https://latex.codecogs.com/svg.image?[0,360)&space;\rightarrow&space;[0,&space;2^{16})" title="[0,360) \rightarrow [0, 2^{16})" />
- Cosine & Sine: <img src="https://latex.codecogs.com/svg.image?[0,&space;1)&space;\rightarrow&space;[0,&space;2^{15}),&space;\&space;[-1,&space;0]&space;\rightarrow&space;[2^{15},&space;2^{16})" title="[0, 1) \rightarrow [0, 2^{15}), \ (-1, 0] \rightarrow [2^{15}, 2^{16})" />



## Utilization

|                     | LUT  | FF   | BRAM | DSP  |
| ------------------- | ---- | ---- | ---- | ---- |
| HLS CORDIC Baseline | 421  | 107  | 0    | 0    |
| Chisel CORDIC       | 686  | 639  | 0    | 0    |



## Performance

|                     | Latency(ns) | Period(ns) | Interval |
| ------------------- | ----------- | ---------- | -------- |
| HLS CORDIC Baseline | 500         | 51         | 49       |
| Chisel CORDIC       | 160         | 10         | 1        |



## Accuracy

|                     | Cosine total error | Sine total error |
| ------------------- | ------------------ | ---------------- |
| HLS CORDIC Baseline | 157.947048         | 91.995215        |
| Chisel CORDIC       | 0.071231           | 0.023446         |

