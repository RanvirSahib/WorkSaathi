const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

async function request(path, options = {}) {
  const token = localStorage.getItem('worksaathi_access_token');
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  });

  const body = await response.json().catch(() => ({}));
  if (!response.ok || body.success === false) {
    throw new Error(body.message || `Request failed with status ${response.status}`);
  }
  return body.data !== undefined ? body.data : body;
}

export const api = {
  // Auth
  login: (email, password) => request('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }),
  register: (payload) => request('/auth/register', { method: 'POST', body: JSON.stringify(payload) }),
  
  // Public Data
  workers: (params = '') => request(`/workers${params ? `?${params}` : '?size=50&sortBy=createdAt&sortDir=desc'}`),
  workerById: (id) => request(`/workers/${id}`),
  services: () => request('/services'),
  
  // Customer Jobs
  createJob: (payload) => request('/jobs', { method: 'POST', body: JSON.stringify(payload) }),
  customerJobs: () => request('/jobs'),
  jobById: (id) => request(`/jobs/${id}`),
  cancelJob: (id) => request(`/jobs/${id}/cancel`, { method: 'POST' }),
  reviewJob: (id, payload) => request(`/jobs/${id}/review`, { method: 'POST', body: JSON.stringify(payload) }),

  // Worker Jobs
  workerJobs: () => request('/jobs/worker'),
  workerRequestedJobs: () => request('/jobs/worker/requested'),
  acceptJob: (id) => request(`/jobs/${id}/accept`, { method: 'POST' }),
  rejectJob: (id) => request(`/jobs/${id}/reject`, { method: 'POST' }),
  startJob: (id) => request(`/jobs/${id}/start`, { method: 'POST' }),
  onTheWay: (id) => request(`/jobs/${id}/on-the-way`, { method: 'POST' }),
  arrived: (id) => request(`/jobs/${id}/arrived`, { method: 'POST' }),
  completeJob: (id) => request(`/jobs/${id}/complete`, { method: 'POST' }),
  
  // Admin
  adminDashboard: () => request('/admin/dashboard'),
  adminUsers: (page = 0, size = 20) => request(`/admin/users?page=${page}&size=${size}`),
  adminWorkers: (page = 0, size = 20) => request(`/admin/workers?page=${page}&size=${size}`),
  adminPendingWorkers: () => request('/admin/workers/pending'),
  verifyWorker: (workerId) => request(`/admin/workers/${workerId}/verify`, { method: 'POST' }),
  rejectWorker: (workerId, reason = 'Documents invalid') => request(`/admin/workers/${workerId}/reject?reason=${encodeURIComponent(reason)}`, { method: 'POST' }),
};
