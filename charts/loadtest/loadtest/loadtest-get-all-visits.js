import http from 'k6/http';
import {check} from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
    stages: [
        {duration: '1m', target: 5},  // Warmup: ramp up to 5 VUs
        {duration: '5m', target: 20}, // Stress: ramp up to 20 VUs
        {duration: '1m', target: 0},  // Cooldown: ramp down to 0
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
}
