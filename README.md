### Example app to Demonstrate AWS lambda SnapStart with AWS SAM

Official documentation ; https://aws.amazon.com/blogs/compute/starting-up-faster-with-aws-lambda-snapstart/

## What's going on? 

Since the birth of AWS lambda, "Cold starts" have been a major headache for developers. A lot of creative people have tried to make 
creative solutions to work around this problem - The core of the problem being that some Lambda invocations take significantly more time than 
others because the runtime environment needs to be fired up. For Java, this has been especially painfull since the Java VM is on the heavy side 
compared to more lean languages like Go.

At Re:Invent 2022 with the release of SnapStart a lot of this pain is taken away - the function’s initialization is done ahead of time when you publish a function version. 
Lambda takes a Firecracker microVM snapshot of the memory and disk state of the initialized execution environment, 
encrypts the snapshot, and caches it for low-latency access.

To use Java SnapStart, you only have to add these two lines in your CloudFormation- or Serverless template

```text
  SnapStart:
    ApplyOn: PublishedVersions
```

## What is in this repository

This repo contains a SAM application with a Java Lambda that is "SnapStarted", and a GitHub Actions workflow to Deploy that into your AWS account if you provide 
repo secrets in your fork. 

It also contains a simple load test using the K6 framework for load testing that can be started from the GitHub actions UI. 



In addition to this feature, you can now also utilize the Java "Crac" library to cache state in the snapshots stored 
by Lambda SnapStart to further optimization. 

The lambda in this demo repository creates 100 million random numbers and sums them, just to have some time consuning task.
When you run the load test, you'll se that the Lambda performs well, w

## How deploy the lambda

* Make a fork of this repository. Go to GitHub actions. 
* Make two repository secrets in your fork; 

* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY
* SAM_ARTIFACT_BUCKET - (The name of any bucket that can be used for Lambda deployment) 

Run the provision_lambda workflow from the GitHub actions UI. 

## running the load test

Look at the output of the provision_lambda job and get the URL of the lambda function endpoint. 

* Change the LAMBDA_ENDPOINT environment variable in ```k6-loadtest.yml``` to this URL and commit. 

Run the Load test from the GitHub actions UI. This is an example of K6 output. Look at the line ```iteration_duration```  
to see statistics for the entire response. 


## Java on Crac

In this repo I have also played around with Crac. That you can use together with SnapStart, to load up state into you 
"snapshotted" Lambda runties ahead of time. For some usecases this can be very useful. I talked to a guy at the conference
that made a lambda to generate PDFs. he pre-loaded all data for binary fonts into the Lambda snapshot saved tons of init time 
on Lambda strtups. 

In this repo I simulate a "have init" by just making 100 million random numbers between 0 and 42, and then sum them. 

Try to remove SnapsTart from thee ```template.yml``` file and see the response times 10x 

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
