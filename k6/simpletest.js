import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
  http.get(__ENV.LAMBDA_ENDPOINT );
  sleep(1);
}