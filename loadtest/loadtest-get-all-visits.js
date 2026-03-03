import http from 'k6/http';
import {check, sleep} from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const DURATION = __ENV.DURATION || '1m';
const MAX_VUS = __ENV.MAX_VUS || '100';
const WARMUP_DURATION = __ENV.WARMUP_DURATION || '30s';

export const options = {
    stages: [
        {duration: WARMUP_DURATION, target: 10},
        {duration: '1m', target: parseInt(MAX_VUS)},
        {duration: DURATION, target: parseInt(MAX_VUS)},
        {duration: '30s', target: 0},
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const params = {
    headers: {
        'Accept': 'application/json',
    },
};

export default function () {
    const getAllRes = http.get(`${BASE_URL}/visits`, params);
    check(getAllRes, {
        'get all visits status is 200': (r) => r.status === 200,
        'get all visits is array': (r) => Array.isArray(r.json()),
    });

    sleep(1);
}
