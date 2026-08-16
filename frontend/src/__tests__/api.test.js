import { describe, it, expect, beforeEach, vi } from 'vitest';
import { api } from '../services/api';

describe('Frontend API Service Client', () => {
  beforeEach(() => {
    localStorage.clear();
    vi.restoreAllMocks();
  });

  it('should include Bearer token in headers when token exists in localStorage', async () => {
    localStorage.setItem('worksaathi_access_token', 'test-jwt-token');

    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ success: true, data: [{ id: 1, name: 'Electrician' }] })
    });

    const result = await api.services();
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining('/services'),
      expect.objectContaining({
        headers: expect.objectContaining({
          Authorization: 'Bearer test-jwt-token'
        })
      })
    );
    expect(result).toEqual([{ id: 1, name: 'Electrician' }]);
  });

  it('should throw an error with backend message when response is not ok', async () => {
    global.fetch = vi.fn().mockResolvedValue({
      ok: false,
      json: async () => ({ message: 'Invalid credentials' })
    });

    await expect(api.login('user@test.com', 'wrongpass')).rejects.toThrow('Invalid credentials');
  });

  it('should perform customer job creation and return response data', async () => {
    const jobPayload = {
      workerId: 1,
      serviceId: 2,
      title: 'Fix Tap',
      description: 'Leaking bathroom tap',
      scheduledDate: '2026-08-20T10:00:00',
      scheduledTime: '10:00 AM',
      address: 'Test Address',
      estimatedPrice: 400
    };

    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ success: true, data: { id: 99, status: 'REQUESTED', title: 'Fix Tap' } })
    });

    const result = await api.createJob(jobPayload);
    expect(result.id).toBe(99);
    expect(result.status).toBe('REQUESTED');
  });

  it('should call worker status transition endpoints', async () => {
    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ success: true, data: { id: 99, status: 'ACCEPTED' } })
    });

    const res = await api.acceptJob(99);
    expect(res.status).toBe('ACCEPTED');
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining('/jobs/99/accept'),
      expect.objectContaining({ method: 'POST' })
    );
  });

  it('should call admin verify worker endpoint', async () => {
    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ success: true, message: 'Worker approved' })
    });

    const res = await api.verifyWorker(50);
    expect(res.message).toBe('Worker approved');
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining('/admin/workers/50/verify'),
      expect.objectContaining({ method: 'POST' })
    );
  });
});
