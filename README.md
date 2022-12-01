### Example app to Demonstrate AWS lambda SnapStart with AWS SAM

## How deploy the lambda

Make a fork of this repository. Go to GitHub actions, Make two repository secrets in your fork; 

* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY

Run the provision_lambda workflow. 

Use the output of the provision_lambda workflow - and get the URL of the lambda function endpoint. 

## running the load test

```text
  data_received..................: 190 kB 18 kB/s
     data_sent......................: 17 kB  1.6 kB/s
     http_req_blocked...............: avg=6.76ms   min=300ns   med=600ns    max=59.97ms  p(90)=50.66ms  p(95)=54.34ms 
     http_req_connecting............: avg=881.94µs min=0s      med=0s       max=7.88ms   p(90)=6.54ms   p(95)=7.05ms  
     http_req_duration..............: avg=300.66ms min=84.08ms med=289.91ms max=940.95ms p(90)=781.4ms  p(95)=862.07ms
       { expected_response:true }...: avg=300.66ms min=84.08ms med=289.91ms max=940.95ms p(90)=781.4ms  p(95)=862.07ms
     http_req_failed................: 0.00%  ✓ 0         ✗ 160 
     http_req_receiving.............: avg=64.98µs  min=25.8µs  med=52.45µs  max=194µs    p(90)=107.81µs p(95)=126.73µs
     http_req_sending...............: avg=80.81µs  min=26.8µs  med=67µs     max=626.8µs  p(90)=120.95µs p(95)=139.1µs 
     http_req_tls_handshaking.......: avg=3.12ms   min=0s      med=0s       max=30.86ms  p(90)=21.89ms  p(95)=25.53ms 
     http_req_waiting...............: avg=300.51ms min=83.99ms med=289.76ms max=940.19ms p(90)=781.17ms p(95)=861.76ms
     http_reqs......................: 160    14.771168/s
     iteration_duration.............: avg=1.3s     min=1.08s   med=1.29s    max=2s       p(90)=1.83s    p(95)=1.92s   
     iterations.....................: 160    14.771168/s
     vus............................: 20     min=20      max=20
     vus_max........................: 20     min=20      max=20

```


