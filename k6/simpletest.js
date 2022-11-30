import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
  http.get('https://szemxh8vgf.execute-api.eu-west-1.amazonaws.com/Prod/hello');
  sleep(1);
}