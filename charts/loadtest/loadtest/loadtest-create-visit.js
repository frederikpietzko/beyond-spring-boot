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

const payload = JSON.stringify({
    description: "My Cat is barfing a lot",
    dateTime: "2025-10-01T12:00:00Z",
    pet: {
        name: "Thor",
        age: 5,
        type: "CAT"
    }
});

const params = {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
};

export default function () {
    const postRes = http.post(`${BASE_URL}/visits`, payload, params);
    check(postRes, {
        'create visit status is 201 or 200': (r) => r.status === 201 || r.status === 200,
        'create visit has id': (r) => r.json() && r.json().id !== undefined,
    });
}
