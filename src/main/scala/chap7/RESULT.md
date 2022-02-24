# Chapter 7 -   Matrix Multiplication

Comparation between HLS code and Chisel code.



## Environment

- Vitis HLS 2021.1
- Vivado 2021.1
- Xilinx xc7a200tfbg676-2



## Architecture

- 16-bit values
- 16-dim matrix
- Pipelined Systolic Array referring to Google TPU



## Utilization

|                                    | LUT  | FF   | BRAM | DSP  |
| ---------------------------------- | ---- | ---- | ---- | ---- |
| HLS Matrix Multiplication Baseline | 1015 | 30   | 0    | 48   |
| Chisel  Matrix Multiplication      | 1280 | 7486 | 0    | 512  |

## Performance

|                                     | Latency(ns) | Period(ns) | Interval |
| ----------------------------------- | ----------- | ---------- | -------- |
| HLS  Matrix Multiplication Baseline | 12900       | 50         | 259      |
| Chisel  Matrix Multiplication       | 1600        | 50         | 16       |

