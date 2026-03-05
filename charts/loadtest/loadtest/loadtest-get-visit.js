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
        http_req_failed: ['rate<0.20'],
        http_req_duration: ['p(95)<10000'],
    },
};

const params = {
    headers: {
        'Accept': 'application/json',
    },
};

export default function () {
    const visitId = 1;
    const getSingleRes = http.get(`${BASE_URL}/visits/${visitId}`, params);
    check(getSingleRes, {
        'get single visit status is 200': (r) => r.status === 200,
        'get single visit has correct id': (r) => r.json() && r.json().id === visitId,
    });
}
