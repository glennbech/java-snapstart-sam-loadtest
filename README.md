# Lambda SnapStart Example using SAM


## How to replicate this sample  


```sh
 k6 run --vus 50 --duration 10s simpletest.js
```

Gives a P90 at around 6 seconds. but not Unexpected since we have only given the Poor lambda 512mb of memory and delayed 
an execution for at least 1 second, so that the incoming requests will pile up- and cause cold starts. 

```text
default ✓ [======================================] 50 VUs  10s

     data_received..................: 367 kB 29 kB/s
     data_sent......................: 33 kB  2.6 kB/s
     http_req_blocked...............: avg=295.38ms min=0s    med=2µs     max=1.21s    p(90)=1.18s    p(95)=1.2s
     http_req_connecting............: avg=153.76ms min=0s    med=0s      max=1.04s    p(90)=1.04s    p(95)=1.04s
     http_req_duration..............: avg=2.89s    min=1.67s med=2.04s   max=5.81s    p(90)=4.69s    p(95)=4.71s
       { expected_response:true }...: avg=2.89s    min=1.67s med=2.04s   max=5.81s    p(90)=4.69s    p(95)=4.71s
     http_req_failed................: 0.00%  ✓ 0         ✗ 138
     http_req_receiving.............: avg=6.92ms   min=17µs  med=87.5µs  max=193.52ms p(90)=3.31ms   p(95)=45.2ms
     http_req_sending...............: avg=317.27µs min=33µs  med=148.5µs max=2.49ms   p(90)=694.7µs  p(95)=984.09µs
     http_req_tls_handshaking.......: avg=112.79ms min=0s    med=0s      max=555.73ms p(90)=460.06ms p(95)=531.11ms
     http_req_waiting...............: avg=2.88s    min=1.67s med=2.01s   max=5.66s    p(90)=4.69s    p(95)=4.71s
     http_reqs......................: 138    10.836417/s
     iteration_duration.............: avg=4.18s    min=2.67s med=3.05s   max=7.36s    p(90)=6.66s    p(95)=6.91s
     iterations.....................: 138    10.836417/s
     vus............................: 19     min=19      max=50
     vus_max........................: 50     min=50      max=50
```

I then deleted the SAM application with ````sam delete```` and did a new build and  to get a new Lambda environment up and running 
With SnapStart. Wait? This is not as promised...

```text

     data_received..................: 359 kB 28 kB/s
     data_sent......................: 33 kB  2.6 kB/s
     http_req_blocked...............: avg=184.83ms min=0s    med=2µs   max=750.39ms p(90)=625.99ms p(95)=682.45ms
     http_req_connecting............: avg=17.9ms   min=0s    med=0s    max=54.29ms  p(90)=54.05ms  p(95)=54.15ms
     http_req_duration..............: avg=2.89s    min=1.67s med=1.98s max=5.39s    p(90)=4.95s    p(95)=5s
       { expected_response:true }...: avg=2.89s    min=1.67s med=1.98s max=5.39s    p(90)=4.95s    p(95)=5s
     http_req_failed................: 0.00%  ✓ 0         ✗ 149
     http_req_receiving.............: avg=1.7ms    min=18µs  med=174µs max=116.75ms p(90)=1.77ms   p(95)=2.53ms
     http_req_sending...............: avg=563.09µs min=34µs  med=164µs max=20.79ms  p(90)=868.4µs  p(95)=1.19ms
     http_req_tls_handshaking.......: avg=149.39ms min=0s    med=0s    max=644.91ms p(90)=520.68ms p(95)=576.63ms
     http_req_waiting...............: avg=2.89s    min=1.67s med=1.97s max=5.39s    p(90)=4.95s    p(95)=5s
     http_reqs......................: 149    11.817893/s
     iteration_duration.............: avg=4.08s    min=2.68s med=2.98s max=6.9s     p(90)=6.52s    p(95)=6.65s
     iterations.....................: 149    11.817893/s
     vus............................: 41     min=41      max=50
     vus_max........................: 50     min=50      max=50
```

After a minor code change and a re-deployment, I now have two versions; If the second run was faster due to warm lambdas- or 
due to SnapStart is not good to know. 

```text

     data_received..............: 423 kB  36 kB/s
     data_sent..................: 39 kB   3.3 kB/s
     http_req_blocked...........: avg=70.98ms  min=0s       med=1µs      max=1.51s    p(90)=462.68ms p(95)=491.55ms
     http_req_connecting........: avg=27.15ms  min=0s       med=0s       max=1.04s    p(90)=74.34ms  p(95)=151.78ms
     http_req_duration..........: avg=632.79ms min=171.39ms med=536.5ms  max=2.31s    p(90)=1.32s    p(95)=1.36s
     http_req_failed............: 100.00% ✓ 307       ✗ 0
     http_req_receiving.........: avg=2.32ms   min=14µs     med=112µs    max=214.39ms p(90)=2.98ms   p(95)=4.1ms
     http_req_sending...........: avg=555.71µs min=24µs     med=134µs    max=17.95ms  p(90)=1.01ms   p(95)=2.2ms
     http_req_tls_handshaking...: avg=35.17ms  min=0s       med=0s       max=414.77ms p(90)=173.12ms p(95)=291.32ms
     http_req_waiting...........: avg=629.91ms min=170.84ms med=535.79ms max=2.3s     p(90)=1.31s    p(95)=1.36s
     http_reqs..................: 307     26.260053/s
     iteration_duration.........: avg=1.7s     min=1.17s    med=1.54s    max=3.66s    p(90)=2.69s    p(95)=2.84s
     iterations.................: 307     26.260053/s
     vus........................: 5       min=5       max=50
     vus_max....................: 50      min=50      max=50

```

There is one thing we can do actually - we can bump up the vusers to 100 and 

```text
     data_received..............: 839 kB  66 kB/s
     data_sent..................: 78 kB   6.1 kB/s
     http_req_blocked...........: avg=254.9ms  min=0s       med=1µs      max=2.48s  p(90)=1.68s    p(95)=1.74s
     http_req_connecting........: avg=164.52ms min=0s       med=0s       max=1.21s  p(90)=1.21s    p(95)=1.21s
     http_req_duration..........: avg=514.73ms min=233.11ms med=448.9ms  max=2.64s  p(90)=742.41ms p(95)=869.55ms
     http_req_failed............: 100.00% ✓ 606       ✗ 0
     http_req_receiving.........: avg=3.69ms   min=7µs      med=52.5µs   max=1.02s  p(90)=1.24ms   p(95)=7.12ms
     http_req_sending...........: avg=289µs    min=14µs     med=72µs     max=8.49ms p(90)=584µs    p(95)=1.45ms
     http_req_tls_handshaking...: avg=75.85ms  min=0s       med=0s       max=1.18s  p(90)=431.3ms  p(95)=471.59ms
     http_req_waiting...........: avg=510.75ms min=220.83ms med=444.05ms max=2.64s  p(90)=741.89ms p(95)=866.57ms
     http_reqs..................: 606     47.803131/s
     iteration_duration.........: avg=1.77s    min=1.24s    med=1.53s    max=4.19s  p(90)=3.09s    p(95)=3.34s
     iterations.................: 606     47.803131/s
     vus........................: 3       min=3       max=100
     vus_max....................: 100     min=100     max=100
```