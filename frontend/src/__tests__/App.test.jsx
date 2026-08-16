import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { Header, Home, FindWorkers, Profile, Booking, CustomerDashboard, WorkerDashboard, AdminDashboard, AuthPage } from '../main';
import { api } from '../services/api';

describe('WorkSaathi UI Component Suites', () => {
  beforeEach(() => {
    localStorage.clear();
    vi.restoreAllMocks();
  });

  describe('Header Component', () => {
    it('should render brand and navigation buttons', () => {
      const setPage = vi.fn();
      render(<Header page="home" setPage={setPage} role="customer" setRole={() => {}} auth={null} onLogout={() => {}} />);
      
      expect(screen.getByText(/Find workers/i)).toBeInTheDocument();
      expect(screen.getByText(/How it works/i)).toBeInTheDocument();
      expect(screen.getByText(/About/i)).toBeInTheDocument();
    });

    it('should show logged in user info and log out button', () => {
      const onLogout = vi.fn();
      const auth = { name: 'Ranvir', role: 'CUSTOMER' };
      render(<Header page="home" setPage={() => {}} role="customer" setRole={() => {}} auth={auth} onLogout={onLogout} />);
      
      expect(screen.getByText(/👤 Ranvir \(CUSTOMER\)/i)).toBeInTheDocument();
      const logoutBtn = screen.getByText(/Log out/i);
      fireEvent.click(logoutBtn);
      expect(onLogout).toHaveBeenCalled();
    });
  });

  describe('Home Component', () => {
    it('should render hero title, search bar, and service categories', () => {
      const setPage = vi.fn();
      const setQuery = vi.fn();
      render(<Home setPage={setPage} setQuery={setQuery} servicesList={[{ id: 1, name: 'Electrician', description: 'Wiring and repairs' }]} />);

      expect(screen.getByText(/The right person/i)).toBeInTheDocument();
      expect(screen.getByPlaceholderText(/What service do you need\?/i)).toBeInTheDocument();
      expect(screen.getAllByText(/Electrician/i).length).toBeGreaterThan(0);
    });
  });

  describe('FindWorkers Component', () => {
    const mockWorkers = [
      { id: 1, name: 'Raj Kumar', role: 'Electrician', rating: 4.8, reviews: 10, rate: 500, distance: 2, exp: 5, available: true, skills: ['Electrician'] },
      { id: 2, name: 'Sanjay Verma', role: 'Plumber', rating: 4.9, reviews: 15, rate: 450, distance: 1.5, available: true, skills: ['Plumber'] }
    ];

    it('should display worker cards and filter by query', () => {
      const setSelected = vi.fn();
      const setPage = vi.fn();
      render(
        <FindWorkers
          query="Raj"
          setQuery={() => {}}
          setPage={setPage}
          setSelected={setSelected}
          workerList={mockWorkers}
          servicesList={[{ id: 1, name: 'Electrician' }, { id: 2, name: 'Plumber' }]}
          loading={false}
          error=""
        />
      );

      expect(screen.getByText(/Raj Kumar/i)).toBeInTheDocument();
      expect(screen.queryByText(/Sanjay Verma/i)).not.toBeInTheDocument();
    });
  });

  describe('CustomerDashboard Component', () => {
    it('should render customer bookings list and handle cancellations', async () => {
      vi.spyOn(api, 'customerJobs').mockResolvedValue([
        { id: 10, title: 'Ceiling Fan Repair', serviceName: 'Electrician', status: 'REQUESTED', address: 'Delhi', estimatedPrice: 500 }
      ]);
      vi.spyOn(api, 'cancelJob').mockResolvedValue({ id: 10, status: 'CANCELLED' });

      await act(async () => {
        render(<CustomerDashboard setRole={() => {}} setPage={() => {}} setSelected={() => {}} workerList={[]} auth={{ name: 'Customer Test' }} />);
      });

      await waitFor(() => {
        expect(screen.getByText(/Ceiling Fan Repair/i)).toBeInTheDocument();
      });

      await act(async () => {
        const cancelBtn = screen.getByText(/Cancel/i);
        fireEvent.click(cancelBtn);
      });
      expect(api.cancelJob).toHaveBeenCalledWith(10);
    });
  });

  describe('WorkerDashboard Component', () => {
    it('should render pending requests and handle accept action', async () => {
      vi.spyOn(api, 'workerJobs').mockResolvedValue([
        { id: 25, title: 'Pipe Leakage', serviceName: 'Plumber', status: 'REQUESTED', address: 'Noida', customerName: 'Amit', estimatedPrice: 400 }
      ]);
      vi.spyOn(api, 'acceptJob').mockResolvedValue({ id: 25, status: 'ACCEPTED' });

      await act(async () => {
        render(<WorkerDashboard auth={{ name: 'Raj Kumar' }} setRole={() => {}} />);
      });

      await waitFor(() => {
        expect(screen.getByText(/Pipe Leakage/i)).toBeInTheDocument();
      });

      await act(async () => {
        const acceptBtn = screen.getByText(/Accept/i);
        fireEvent.click(acceptBtn);
      });
      expect(api.acceptJob).toHaveBeenCalledWith(25);
    });
  });

  describe('AdminDashboard Component', () => {
    it('should render platform statistics and approve pending workers', async () => {
      vi.spyOn(api, 'adminDashboard').mockResolvedValue({
        totalUsers: 12,
        totalWorkers: 5,
        verifiedWorkers: 4,
        pendingVerifications: 1
      });
      vi.spyOn(api, 'adminPendingWorkers').mockResolvedValue([
        { id: 88, name: 'New Plumber', verificationStatus: 'PENDING', bio: 'Expert plumber' }
      ]);
      vi.spyOn(api, 'verifyWorker').mockResolvedValue({ success: true, message: 'Worker approved' });

      await act(async () => {
        render(<AdminDashboard auth={{ name: 'Admin' }} />);
      });

      await waitFor(() => {
        expect(screen.getByText(/New Plumber/i)).toBeInTheDocument();
        expect(screen.getByText(/Total platform users/i)).toBeInTheDocument();
      });

      await act(async () => {
        const approveBtn = screen.getByText(/Approve Pro/i);
        fireEvent.click(approveBtn);
      });
      expect(api.verifyWorker).toHaveBeenCalledWith(88);
    });
  });

  describe('AuthPage Component', () => {
    it('should handle quick demo login autofill', () => {
      render(<AuthPage onAuthenticated={() => {}} />);

      const customerDemoBtn = screen.getByText(/👨 Customer/i);
      fireEvent.click(customerDemoBtn);

      const emailInput = screen.getByLabelText(/Email address/i);
      expect(emailInput.value).toBe('customer@worksaathi.com');
    });
  });
});
