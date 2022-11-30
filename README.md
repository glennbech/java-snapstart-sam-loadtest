
first run 

```sh
 k6 run --vus 50 --duration 10s simpletest.js
```

Gives a P90 at 6 seconds. Not good!! but not Unexpected since we have only given the Poor lambda 512mb of memory and delayed 
an execution for at least 1 second, so that the incoming requests will pile up and causing cold starts. 

```text
     data_received..................: 378 kB 30 kB/s
     data_sent......................: 33 kB  2.6 kB/s
     http_req_blocked...............: avg=118ms    min=0s    med=1µs   max=389.67ms p(90)=369.56ms p(95)=379.14ms
     http_req_connecting............: avg=14.21ms  min=0s    med=0s    max=47.02ms  p(90)=44.3ms   p(95)=45.7ms
     http_req_duration..............: avg=2.81s    min=1.67s med=2.05s max=5.64s    p(90)=4.61s    p(95)=4.64s
       { expected_response:true }...: avg=2.81s    min=1.67s med=2.05s max=5.64s    p(90)=4.61s    p(95)=4.64s
     http_req_failed................: 0.00%  ✓ 0         ✗ 149
     http_req_receiving.............: avg=5.12ms   min=25µs  med=156µs max=260.27ms p(90)=8.08ms   p(95)=8.7ms
     http_req_sending...............: avg=269.51µs min=18µs  med=69µs  max=6.22ms   p(90)=512.6µs  p(95)=1ms
     http_req_tls_handshaking.......: avg=45.29ms  min=0s    med=0s    max=170.3ms  p(90)=152.19ms p(95)=161.57ms
     http_req_waiting...............: avg=2.81s    min=1.67s med=2.04s max=5.64s    p(90)=4.61s    p(95)=4.63s
     http_reqs......................: 149    11.753699/s
     iteration_duration.............: avg=3.93s    min=2.67s med=3.05s max=6.95s    p(90)=5.94s    p(95)=6s
     iterations.....................: 149    11.753699/s
     vus............................: 6      min=6       max=50
     vus_max........................: 50     min=50      max=50
```
A Second run the the same parameters - no code change, right after the initial run cuts the P90 3.26s - and P95 to 3.43s


