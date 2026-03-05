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

export function setup() {
    console.log(`Seeding 100 pet visits to ${BASE_URL}/visits`);
    for (let i = 0; i < 100; i++) {
        const postRes = http.post(`${BASE_URL}/visits`, payload, params);
        if (postRes.status !== 201 && postRes.status !== 200) {
            console.error(`Failed to seed visit ${i}: ${postRes.status} ${postRes.body}`);
        }
    }
}

export default function () {
    const getAllRes = http.get(`${BASE_URL}/visits`, {
        headers: {
            'Accept': 'application/json',
        }
    });
    check(getAllRes, {
        'get all visits status is 200': (r) => r.status === 200,
        'get all visits is array': (r) => Array.isArray(r.json()),
    });
}
