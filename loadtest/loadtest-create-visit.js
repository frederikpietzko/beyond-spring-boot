import http from 'k6/http';
import {check} from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const DURATION = __ENV.DURATION || '10m';
const MAX_VUS = __ENV.MAX_VUS || '10';
const WARMUP_DURATION = __ENV.WARMUP_DURATION || '30s';

export const options = {
    stages: [
        {duration: WARMUP_DURATION, target: 5},
        {duration: '5m', target: parseInt(MAX_VUS)},
        {duration: DURATION, target: parseInt(MAX_VUS)},
        {duration: '1m', target: 0},
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
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
