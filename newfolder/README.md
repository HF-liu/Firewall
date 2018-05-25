# Firewall
## 1.How I solve this problem
The key part of this solution is the data structure. I use four arrays to serve as a “map” to record the valid range of ports and ip addresses. In this way, after I parse all the input information, I can find whether it’s valid with O(1) complexity. Also, it leaves spaces for future maintenance which is quite easy to adjust the valid range.

## 2.How would I test this solution
Due to time limitation, I just include a basic test case in the solution. Given more time, I shall add the following tests as well:
* Edge cases
  - all four kinds of inputs to the rule of port: [0,0]
  - all four kinds of inputs to the rule of port: [65535,65535]
  - all four kinds of inputs to the rule of port: [0, 65535]
  - all four kinds of inputs to the rule of ip: [0.0.0.0 - 0.0.0.0]
  - all four kinds of inputs to the rule of ip: [255.255.255.255 - 255.255.255.255]
  - all four kinds of inputs to the rule of ip: [0.0.0.0 - 255.255.255.255]
* Performance Test
  - 500 inputs with 1-500 rules
  - 500 inputs with 500 - 50,000 rules
  - 500 inputs with 50,000+ rules
  - 50,000 inputs with 1-500 rules
  - 50,000 inputs with 500 - 50,000 rules
  - 50,000 inputs with 50,000+ rules

## 3.How this solution can be improved
Error handling is definitely a critical part in production, so this part should be taken serious care of.

4.Team that I am interested in
I am quite interested in Data & Platform teams, because I am proficient in both back-end and front-end skills, enjoying both writing clean code with concise algorithm and visualizing what I've done with front-end skills.

